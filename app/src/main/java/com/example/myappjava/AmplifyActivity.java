package com.example.myappjava;

import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myappjava.databinding.ActivityAmplifyBinding;
import com.google.android.material.navigation.NavigationView;

public class AmplifyActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAmplifyBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAmplifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavHostFragment fragment = binding.mainNavHost.getFragment();
        navController = fragment.getNavController();

        setupNavigation();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void setupNavigation() {
        setSupportActionBar(binding.toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
        navigationView.setItemIconTintList(null);

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar,
                R.string.app_name, R.string.app_name);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        toggle.setToolbarNavigationClickListener(v -> {
            if (binding.drawer.isDrawerVisible(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START);
            } else {
                binding.drawer.openDrawer(GravityCompat.START);
            }
        });

        // Auth category is selected by default
        navigationView.setCheckedItem(R.id.nav_auth);
        navigationView.getMenu().performIdentifierAction(R.id.nav_auth, 0);
    }
}