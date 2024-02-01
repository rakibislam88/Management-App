package com.example.managementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {


    public static ArrayList<String> accountArr = new ArrayList<>();
    EditText nameEd, numberEd;
    Button createBtn;

    public static SharedPreferences sharedPre;
    public static SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        nameEd = findViewById(R.id.name);
        numberEd = findViewById(R.id.phone_number);
        createBtn = findViewById(R.id.Create_btn);

        sharedPre = getSharedPreferences("accountData", MODE_PRIVATE);
        editor = sharedPre.edit();

        /**
         * create account data load function
         */
        accountLoad();



        /**
         * start create account
         */
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * start get name and number
                 */
                String name = nameEd.getText().toString();
                String number = numberEd.getText().toString();

                /**
                 * start account successfully
                 */
                if (accountArr.contains(number)){
                    Toast.makeText(CreateAccountActivity.this, "already use this number", Toast.LENGTH_SHORT).show();
                }if (name.length()==0){
                    Toast.makeText(CreateAccountActivity.this, "unvalid name", Toast.LENGTH_SHORT).show();
                }if (number.length()!=11){
                    Toast.makeText(CreateAccountActivity.this, "unvalid number", Toast.LENGTH_SHORT).show();
                }else if (name.length() > 0 && number.length()==11 && !accountArr.contains(number)){
                    editor.putString("token", number);
                    editor.apply();
                    String createUrl = "https://raquib.000webhostapp.com/apps/create_account_management.php?userid="+ number +"&username="+ name +"&userphonenumber="+ number;
                    StringRequest createString = new StringRequest(Request.Method.POST, createUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            startActivity(new Intent(CreateAccountActivity.this, HomeActivity.class));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CreateAccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("userid", number);
                            data.put("username", name);
                            data.put("userphonenumber", number);
                            return data;
                        }
                    };
                    nameEd.setText("");
                    numberEd.setText("");
                    RequestQueue createQueue = Volley.newRequestQueue(CreateAccountActivity.this);
                    createQueue.add(createString);
                }
            }
        });

    }

    /**
     * start account data loading
     */
    public void accountLoad(){
        accountArr = new ArrayList<>();
        JsonArrayRequest accountRequest = new JsonArrayRequest(Request.Method.GET, "https://raquib.000webhostapp.com/apps/array_of_managementaccount.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String ph = jsonObject.getString("userphonenumber");
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

        RequestQueue queue = Volley.newRequestQueue(CreateAccountActivity.this);
        queue.add(accountRequest);
    }
}