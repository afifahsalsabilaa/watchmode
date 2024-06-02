package com.example.projectfinal;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.projectfinal.fragment.FavoriteFragment;
import com.example.projectfinal.fragment.MovieFragment;
import com.example.projectfinal.fragment.ProfileFragment;
import com.example.projectfinal.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements FavoriteFragment.FavoriteFragmentListener{

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        if (isNetworkAvailable()) {
            showProgressBarAndLoadFragment();
        } else {
            showNetworkError();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.movie) {
                selectedFragment = new MovieFragment();
            } else if (itemId == R.id.search) {
                selectedFragment = new SearchFragment();
            } else if (itemId == R.id.fav) {
                selectedFragment = new FavoriteFragment();
            } else if (itemId == R.id.profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flFragment, selectedFragment)
                        .commit();

                return true;
            } else {
                return false;
            }
        });
    }

    private void showProgressBarAndLoadFragment() {
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(() -> {
            progressBar.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            MovieFragment movieFragment = new MovieFragment();
            Fragment fragment = fragmentManager.findFragmentByTag(MovieFragment.class.getSimpleName());
            if (!(fragment instanceof MovieFragment)) {
                fragmentManager
                        .beginTransaction()
                        .add(R.id.flFragment, movieFragment)
                        .commit();
            }
        }, 2000);
    }

    private void showNetworkError() {
        new AlertDialog.Builder(this)
                .setTitle("Network Error")
                .setMessage("No internet connection. Please check your network settings.")
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onFavoriteUpdated() {
        // Handle the update here
        // This method will be called whenever the favorites are updated in ProfileFragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flFragment);
        if (currentFragment instanceof FavoriteFragment) {
            ((FavoriteFragment) currentFragment).onFavoriteUpdated();
        }
    }
}