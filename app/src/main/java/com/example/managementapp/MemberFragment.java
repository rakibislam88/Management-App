package com.example.managementapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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


public class MemberFragment extends Fragment {

    ArrayList<String> memberListArr = new ArrayList<>();
    RecyclerView recyclerView;
    TextView memeberSize;
    Adapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_member, container, false);
        recyclerView = v.findViewById(R.id.memeber_recycle);
        memeberSize = v.findViewById(R.id.member_size);

        fun();



        return v;
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.friendView>{

        @NonNull
        @Override
        public friendView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View v = layoutInflater.inflate(R.layout.memeber_layout, parent, false);
            return new friendView(v);
        }

        @Override
        public void onBindViewHolder(@NonNull friendView holder, int position) {
            holder.memberName.setText(memberListArr.get(position));

        }

        @Override
        public int getItemCount() {
            return memberListArr.size();
        }

        public class friendView extends RecyclerView.ViewHolder{

            ImageView memberImg;
            TextView memberName;
            public friendView(@NonNull View itemView) {
                super(itemView);

                memberImg  = itemView.findViewById(R.id.member_img);
                memberName = itemView.findViewById(R.id.member_name);
            }
        }
    }

    private void fun() {
        memberListArr = new ArrayList<>();
        JsonArrayRequest accountRequest = new JsonArrayRequest(Request.Method.GET, "https://raquib.000webhostapp.com/apps/array_of_friend_of_admin_data.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String friendid = jsonObject.getString("friendname");
                        memberListArr.add(friendid);
                        memeberSize.setText("Member / "+ memberListArr.size());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }

                adapter = new Adapter();
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue2 = Volley.newRequestQueue(getContext());
        queue2.add(accountRequest);
    }




}