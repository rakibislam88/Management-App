package com.example.managementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp1, sp2;
    Button accountBtn, loginBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountBtn = findViewById(R.id.create_account);
        loginBtn   = findViewById(R.id.login_in);


        /**
         * start get user account
         */
        sp1  = getSharedPreferences("accountData", MODE_PRIVATE);
        sp2  = getSharedPreferences("loginData", MODE_PRIVATE);
        String f = sp1.getString("token", "");
        String s = sp2.getString("token", "");

        if (f.length() > 0 || s.length() > 0){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }


        /**
         * start create account
         */
        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));
            }
        });

        /**
         * start login button
         *
         */
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}