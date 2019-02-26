package com.example.rplrus10.moviebaru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class control extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (username.equals("")) {
            Intent intent = new Intent(control.this, LoginScreen.class);
            startActivity(intent);
            finish();
        } else if (username == username) {
            Intent intent = new Intent(control.this, HomeScreen.class);
            startActivity(intent);
            finish();
        }
    }
}
