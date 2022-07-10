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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        BottomNavigationView navigation = findViewById(R.id.nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        processDialog = new ProgressDialog(this);

        setupDrawerContent(nvDrawer);
        getSupportActionBar().setTitle("Monitor");
        String username = Amplify.Auth.getCurrentUser().getUsername();
        getSupportActionBar().setSubtitle("Hello "+username);
        loadFragment(new MonitorFragment());
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
            case R.id.monitor_navdrawer:
                fragment = new MonitorFragment();
                getSupportActionBar().setTitle("Monitor");
                loadFragment(fragment);
                break;
            case R.id.devices_navdrawer:
                fragment = new DevicesFragment();
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
            switch (item.getItemId()) {
                case R.id.monitor_nav:
                    fragment = new MonitorFragment();
                    getSupportActionBar().setTitle("Monitor");
                    loadFragment(fragment);
                    return true;
                case R.id.device_nav:
                    fragment = new DevicesFragment();
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