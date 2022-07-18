package com.example.testrestapi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;

import com.amplifyframework.api.rest.RestOptions;
import com.amplifyframework.core.Amplify;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private final String TAG = AdminActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private CardviewAdapter cardviewAdapter;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private String username1, username2;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setTitle("Admin");

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);


        recyclerView = (RecyclerView) findViewById(R.id.rcview);
        //List<InfoCardview> device = createList();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

//        cardviewAdapter = new CardviewAdapter(AdminActivity.this,device,AdminActivity.this);
//        recyclerView.setAdapter(cardviewAdapter);
        mHandler.post(runnable);
    }
    private final Runnable runnable =
            new Runnable() {
                public void run() {
                    get_user();
                    List<InfoCardview> device = createList();
                    cardviewAdapter = new CardviewAdapter(AdminActivity.this,device,AdminActivity.this);
                    recyclerView.setAdapter(cardviewAdapter);
                    mHandler.postDelayed(this, 1000);
                }
            };
    private void get_user() {
        RestOptions options = RestOptions.builder()
                .addPath("/todo/user")
                .build();
        Amplify.API.get(options,
                restResponse -> {
                    try {
                        //get info
                        Log.i(TAG, "GET succeeded: " + restResponse.getData().asJSONObject().getJSONArray("users").getJSONObject(1).getString("Username"));
                        username1=restResponse.getData().asJSONObject().getJSONArray("users").getJSONObject(1).getString("Username");
                        username2=restResponse.getData().asJSONObject().getJSONArray("users").getJSONObject(2).getString("Username");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                apiFailure -> Log.e(TAG, "GET failed.", apiFailure)
        );
    }
    private List<InfoCardview> createList() {
        List<InfoCardview> list = new ArrayList<InfoCardview>();
        InfoCardview User1 = new InfoCardview("User "+username1,"user",3);
        InfoCardview User2 = new InfoCardview("User "+username2,"user",3);

        list.add(User1);
        list.add(User2);
        return list;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment;
        switch(menuItem.getItemId()) {
            case R.id.logout_navdrawer:
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure logout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Amplify.Auth.signOut(
                                        () -> {
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                                            Log.i("Logout", "Signed out successfully");
                                        },
                                        error -> Log.e("Logout", error.toString())
                                );
                                finish();
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            default:
                getSupportActionBar().setTitle("Admin");
        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }
}