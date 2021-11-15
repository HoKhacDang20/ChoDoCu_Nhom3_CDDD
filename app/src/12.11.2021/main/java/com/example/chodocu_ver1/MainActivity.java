package com.example.chodocu_ver1;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chodocu_ver1.ui.bangdieukhien.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    static public Intent intent;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongtin_canhan_layout);
        drawerLayout = findViewById(R.id.drawer);
        toolbar  = findViewById(R.id.toolsBar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView= findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;
                switch (id)
                {
                    case R.id.search:
                        fragment = new SettingsFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.basket:
                        break;
                    case R.id.favorites:
                        break;
                    case R.id.procode:
                        break;
                    case R.id.order:
                        break;
                    case R.id.support:
                        break;
                    case R.id.setting:
                        break;

                }
                return true;
            }

            private void loadFragment(Fragment fragment) {
                FragmentManager fragmentManager =   getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame,fragment).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentTransaction.addToBackStack(null);
            }
        });
    }




}
