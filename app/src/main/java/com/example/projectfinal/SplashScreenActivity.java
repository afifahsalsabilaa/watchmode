package com.example.projectfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Delay for splash screen (e.g., 2 seconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is logged in
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false);

                Log.d(TAG, "isLoggedIn: " + isLoggedIn); // Log login status

                // If user is logged in, go to MainActivity, otherwise go to LoginActivity
                Intent intent;
                if (isLoggedIn) {
                    Log.d(TAG, "Navigating to MainActivity");
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                } else {
                    Log.d(TAG, "Navigating to LoginActivity");
                    intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 2000); // 2000 milliseconds delay for splash screen
    }
}
