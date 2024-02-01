package com.example.managementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GroupInfoActivity extends AppCompatActivity {

    public static String frist_digit_get = "";
    public static String group_name_get = "";
    TextView txt_frist_digit, txt_group_name;
    TextView recent_boardBtn, memberBtn;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        txt_frist_digit = findViewById(R.id.group_frist_digit);
        txt_group_name = findViewById(R.id.group_name_txt);
        recent_boardBtn = findViewById(R.id.recent_board);
        memberBtn     = findViewById(R.id.member);

        txt_group_name.setText(group_name_get);
        txt_frist_digit.setText(frist_digit_get);

        /**
         *
         *
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, new RecentboardFragment()).commit();
        recent_boardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, new RecentboardFragment()).commit();
            }
        });


        /**
         *
         *
         *
         */
        memberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.framLayout, new MemberFragment()).commit();
            }
        });
    }
}