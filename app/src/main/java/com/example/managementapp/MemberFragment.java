package com.example.managementapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class MemberFragment extends Fragment {

    ArrayList<HashMap<String, String>> memberListArr = new ArrayList<>();
    HashMap<String, String> hashMap, map;
    RecyclerView recyclerView;
    TextView memeberSize;
    Adapter adapter;
    public static String groupname = "";
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
            map = memberListArr.get(position);
            holder.memberName.setText(map.get("name"));

            Dialog memberInfoDialog = new Dialog(getContext());
            holder.memeberLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "toast", Toast.LENGTH_SHORT).show();
                    memberInfoDialog.setContentView(R.layout.member_info_layout);
                    memberInfoDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView name = memberInfoDialog.findViewById(R.id.name);
                    TextView id  = memberInfoDialog.findViewById(R.id.id);
                    TextView delete = memberInfoDialog.findViewById(R.id.delete);

                    name.setText(map.get("name"));
                    id.setText(map.get("id"));

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String id = map.get("id");
                            String url = "https://raquib.000webhostapp.com/apps/delete_friend_of_admin_data.php?friendid="+id;
                            StringRequest deleteString = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    fun();
                                    memberInfoDialog.dismiss();
                                    Toast.makeText(getContext(), "delete", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                            RequestQueue deleteQueue = Volley.newRequestQueue(getContext());
                            deleteQueue.add(deleteString);

                        }
                    });

                    memberInfoDialog.show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return memberListArr.size();
        }

        public class friendView extends RecyclerView.ViewHolder{

            ImageView memberImg;
            TextView memberName;
            LinearLayout memeberLayout;
            public friendView(@NonNull View itemView) {
                super(itemView);

                memberImg  = itemView.findViewById(R.id.member_img);
                memberName = itemView.findViewById(R.id.member_name);
                memeberLayout = itemView.findViewById(R.id.memeber_layout);
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
                        String friendname = jsonObject.getString("friendname");
                        String friendid   = jsonObject.getString("friendid");
                        String groupnm = jsonObject.getString("groupname");
                        if (groupname.equals(groupnm)){
                            hashMap = new HashMap<>();
                            hashMap.put("name", friendname);
                            hashMap.put("id", friendid);
                            memberListArr.add(hashMap);
                            memeberSize.setText("Member / "+ memberListArr.size());
                        }
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