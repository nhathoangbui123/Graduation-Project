package com.example.testrestapi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amplifyframework.core.Amplify;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ProgressDialog processDialog;
    private String username;
    private String Uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent().getStringExtra("username")!=null){
            Uname = getIntent().getStringExtra("username");
        }else{
            Uname = "h";
        }
        Log.i(TAG, Uname);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        BottomNavigationView navigation = findViewById(R.id.nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        username = Amplify.Auth.getCurrentUser().getUsername();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(!username.equals("admin"))
        {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        processDialog = new ProgressDialog(this);

        setupDrawerContent(nvDrawer);
        getSupportActionBar().setTitle("Monitor");
        getSupportActionBar().setSubtitle("Hello "+username);

        Bundle bundle = new Bundle();
        bundle.putString("username", Uname);

        Fragment fragment;
        fragment = new MonitorFragment();
        fragment.setArguments(bundle);

        loadFragment(fragment);
        Intent myIntent = new Intent(MainActivity.this, NotiService.class);

        // Call startService with Intent parameter.
        this.startService(myIntent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(!username.equals("admin"))
                {
                    mDrawer.openDrawer(GravityCompat.START);
                }else{
                    startActivity(new Intent(MainActivity.this, AdminActivity.class));
                }
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
        Bundle bundle = new Bundle();
        bundle.putString("username", Uname);
        switch(menuItem.getItemId()) {
            case R.id.monitor_navdrawer:
                fragment = new MonitorFragment();
                fragment.setArguments(bundle);
                getSupportActionBar().setTitle("Monitor");
                loadFragment(fragment);
                break;
            case R.id.devices_navdrawer:
                fragment = new DevicesFragment();
                fragment.setArguments(bundle);
                getSupportActionBar().setTitle("Devices");
                loadFragment(fragment);
                break;
            case R.id.settings_navdrawer:
                fragment = new SettingsFragment();
                getSupportActionBar().setTitle("Settings");
                loadFragment(fragment);
                break;
            case R.id.help_navdrawer:
                fragment = new HelpFragment();
                getSupportActionBar().setTitle("Help");
                loadFragment(fragment);
                break;
            case R.id.profile_navdrawer:
                fragment = new ProfileFragment();
                getSupportActionBar().setTitle("Profile");
                loadFragment(fragment);
                break;
            case R.id.logout_navdrawer:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure logout?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                processDialog.setMessage("Please Wait For Logout");
                                processDialog.show();
                                Amplify.Auth.signOut(
                                        () -> {
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            Log.i("Logout", "Signed out successfully");
                                        },
                                        error -> {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    processDialog.cancel();
                                                    Log.e("Logout", error.toString());
                                                }
                                            });
                                        }
                                );
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
                fragment = new MonitorFragment();
                getSupportActionBar().setTitle("Monitor");
                loadFragment(fragment);
        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            Bundle bundle = new Bundle();
            bundle.putString("username", Uname);
            switch (item.getItemId()) {
                case R.id.monitor_nav:
                    fragment = new MonitorFragment();
                    fragment.setArguments(bundle);
                    getSupportActionBar().setTitle("Monitor");
                    loadFragment(fragment);
                    return true;
                case R.id.device_nav:
                    fragment = new DevicesFragment();
                    fragment.setArguments(bundle);
                    getSupportActionBar().setTitle("Devices");
                    loadFragment(fragment);
                    return true;
                case R.id.setting_nav:
                    fragment = new SettingsFragment();
                    getSupportActionBar().setTitle("Settings");
                    loadFragment(fragment);
                    return true;
                case R.id.help_nav:
                    fragment = new HelpFragment();
                    getSupportActionBar().setTitle("Help");
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}