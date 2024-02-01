package com.example.managementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public ArrayList<String> accountArr = new ArrayList<>();
    EditText nameEd, numberEd;
    Button loginBtn;
    public static SharedPreferences loginsharedPre;
    public static SharedPreferences.Editor logineditor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameEd = findViewById(R.id.name);
        numberEd = findViewById(R.id.phone_number);
        loginBtn = findViewById(R.id.login_btn);

        accountLoad();

        loginsharedPre = getSharedPreferences("loginData", MODE_PRIVATE);
        logineditor = loginsharedPre.edit();


        /**
         * start login btn
         */
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEd.getText().toString();
                String number = numberEd.getText().toString();
                if (accountArr.contains(name) && accountArr.contains(number)){
                    logineditor.putString("token", number);
                    logineditor.apply();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this, "you have not account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void accountLoad(){
        accountArr = new ArrayList<>();
        JsonArrayRequest accountRequest = new JsonArrayRequest(Request.Method.GET, "https://raquib.000webhostapp.com/apps/array_of_managementaccount.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String nm = jsonObject.getString("username");
                        String ph = jsonObject.getString("userphonenumber");
                        accountArr.add(nm);
                        accountArr.add(ph);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(accountRequest);
    }
}