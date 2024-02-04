package com.example.managementapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.accountView> {

    ArrayList<String> friendOrArr = new ArrayList<>();
    Context context;
    int added = 0;
    ArrayList<AccountModel> arrayList;

    public AccountAdapter(Context context, ArrayList<AccountModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public accountView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.people_layout, parent, false);
        return new accountView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull accountView holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(arrayList.get(position).getUserName());


        fun();

        String url = "https://raquib.000webhostapp.com/apps/friend_of_admin_data.php?adminid="+ arrayList.get(position).getAdminid() +"&friendid="+ arrayList.get(position).getUserId() +"&friendname="+ arrayList.get(position).getUserName()+"&groupname="+arrayList.get(position).getGroupname();
        holder.addPeopleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (friendOrArr.contains(arrayList.get(position).getUserId())){
                    Toast.makeText(context, "Already added in group", Toast.LENGTH_SHORT).show();
                }else {
                    StringRequest createString = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            fun();
                            Toast.makeText(context, "add successfully in group", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    RequestQueue createQueue = Volley.newRequestQueue(context);
                    createQueue.add(createString);
                }
            }
        });
    }

    private void fun() {
        friendOrArr = new ArrayList<>();
        JsonArrayRequest accountRequest = new JsonArrayRequest(Request.Method.GET, "https://raquib.000webhostapp.com/apps/array_of_friend_of_admin_data.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String friendid = jsonObject.getString("friendid");
                        friendOrArr.add(friendid);

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

        RequestQueue queue2 = Volley.newRequestQueue(context);
        queue2.add(accountRequest);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class accountView extends RecyclerView.ViewHolder{

        TextView textView;
        LinearLayout addPeopleLayout;
        public accountView(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.people_name);
            addPeopleLayout = itemView.findViewById(R.id.add_people_linearlayout);
        }
    }
}
