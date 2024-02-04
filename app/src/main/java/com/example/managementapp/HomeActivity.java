package com.example.managementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    public ArrayList<HashMap<String, String>> groupNameArr = new ArrayList<>();
    public ArrayList<BorderNameModel> boardArr = new ArrayList<>();
    public ArrayList<AccountModel> accountNameArr = new ArrayList<>();
    public HashMap<String, String> hashMap, map2;
    public ArrayList<String> checkgroupNameArr = new ArrayList<>();
    SharedPreferences shared1, shared2;
    ImageView userProfile, createGroup, closeDialog;
    public TextView groupFristDigit;
    public EditText groupEditTxt;
    public Button createGroupBtn;
    public String admingroupid = "";
    RecyclerView groupNameRecyl, accountRecyl;
    groupNameAdapter adapter;
    //accountNameAdapter nameAdapter;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        createGroup = findViewById(R.id.add_group);
        userProfile = findViewById(R.id.user_profile);
        groupNameRecyl = findViewById(R.id.group_recycle);




        /**
         * start user information
         */
        shared1 = getSharedPreferences("loginData", MODE_PRIVATE);
        shared2 = getSharedPreferences("accountData", MODE_PRIVATE);

        if (!shared1.equals("")){
            admingroupid = shared1.getString("token", "");
        }else{
            admingroupid = shared2.getString("token", "");
        }

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, admingroupid, Toast.LENGTH_SHORT).show();
            }
        });



        /**
         * start group name load function
         */
        groupArrLoad();
        boardArrLoad();
        //accountArrLoad();

        //Toast.makeText(this, checkgroupNameArr.get(0), Toast.LENGTH_SHORT).show();



        /**
         * start retreive group name this user
         *
         *
         */


        /**
         * start create group
         */

        Dialog dialog = new Dialog(HomeActivity.this);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.create_group_layout);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                closeDialog = dialog.findViewById(R.id.close_group_dialog);
                groupFristDigit = dialog.findViewById(R.id.group_frist_digit);
                groupEditTxt = dialog.findViewById(R.id.group_name);
                createGroupBtn = dialog.findViewById(R.id.create_group_btn);

                createGroupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String groupName = groupEditTxt.getText().toString();
                        String firstDigit = "G";
                        String url = "https://raquib.000webhostapp.com/apps/group_name_data.php?userid="+ admingroupid + "&groupname="+ groupName + "&groupfirstdigit="+ firstDigit;

                        if(!checkgroupNameArr.contains(groupName.toLowerCase())){
                            StringRequest createGroupRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    groupArrLoad();

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
                            queue.add(createGroupRequest);
                            dialog.dismiss();
                        }else{
                            Toast.makeText(HomeActivity.this, "Group name must be unique?", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });

    }


    /**
     * start board array load
     *
     *
     */
    public void boardArrLoad(){

    }


    /**
     *
     *
     */
    public class groupNameAdapter extends RecyclerView.Adapter<groupNameAdapter.groupView>{

        @NonNull
        @Override
        public groupView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
            View v = layoutInflater.inflate(R.layout.groupnamelayout, parent, false);
            return new groupView(v);
        }



        @Override
        public void onBindViewHolder(@NonNull groupView holder, @SuppressLint("RecyclerView") int position) {
            map2 = groupNameArr.get(position);
            String userid  = map2.get("userid");
            String groupnm = map2.get("groupname");
            String groupfd = map2.get("groupfirstdigit");
            holder.groupName.setText(groupnm);
            holder.firstDigit.setText(groupfd);

            holder.firstDigit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupInfoActivity.group_name_get = groupnm;
                    GroupInfoActivity.frist_digit_get = groupfd;
                    startActivity(new Intent(HomeActivity.this, GroupInfoActivity.class));
                }
            });



            /**
             * start show the file of group
             *
             */
            holder.boardRecyl.setVisibility(View.GONE);
            holder.showBoardofGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.boardRecyl.setVisibility(View.VISIBLE);

                    JsonArrayRequest boardArrRequest = new JsonArrayRequest(Request.Method.GET, "https://raquib.000webhostapp.com/apps/arr_of_board.php", null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            boardArr = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String gp = jsonObject.getString("position");
                                    int id = jsonObject.getInt("id");
                                    String board_nm = jsonObject.getString("createboardname");
                                    String cn = String.valueOf(position);
                                    if (gp.equals(cn)){
                                        boardArr.add(new BorderNameModel(board_nm, id));
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }

                            if (boardArr.size() > 0){
                                BoardAdapter boardAdapter = new BoardAdapter(HomeActivity.this, boardArr);
                                holder.boardRecyl.setLayoutManager(new GridLayoutManager(HomeActivity.this, 1));
                                holder.boardRecyl.setAdapter(boardAdapter);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
                    queue.add(boardArrRequest);
                }
            });


            /**
             *
             *
             */
            Dialog dialog = new Dialog(HomeActivity.this);
            holder.moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.setContentView(R.layout.popup_window);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    LinearLayout addPeopleBtn = dialog.findViewById(R.id.add_people);
                    LinearLayout add_board = dialog.findViewById(R.id.add_board);



                    /**
                     * start creaete a new board
                     *
                     */
                    Dialog create_board_dialog = new Dialog(HomeActivity.this);
                    add_board.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            create_board_dialog.setContentView(R.layout.create_board_out);
                            create_board_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            TextView createBoardBtn = create_board_dialog.findViewById(R.id.create_board_button);
                            EditText createBoardName = create_board_dialog.findViewById(R.id.create_board_name);

                            createBoardBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String borardname = createBoardName.getText().toString();
                                    String nl = "null";

                                    String createUrl = "https://raquib.000webhostapp.com/apps/create_board_name.php?adminid="+ admingroupid  +"&adminname="+ nl +"&groupnameofadmin="+ groupnm +"&position="+ position +"&createboardname="+ borardname;
                                    StringRequest createString = new StringRequest(Request.Method.GET, createUrl, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            holder.boardRecyl.setVisibility(View.VISIBLE);

                                            JsonArrayRequest boardArrRequest = new JsonArrayRequest(Request.Method.GET, "https://raquib.000webhostapp.com/apps/arr_of_board.php", null, new Response.Listener<JSONArray>() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    boardArr = new ArrayList<>();
                                                    for (int i = 0; i < response.length(); i++) {
                                                        try {
                                                            JSONObject jsonObject = response.getJSONObject(i);
                                                            String gp = jsonObject.getString("position");
                                                            int id = jsonObject.getInt("id");
                                                            String board_nm = jsonObject.getString("createboardname");
                                                            String cn = String.valueOf(position);
                                                            if (gp.equals(cn)){
                                                                boardArr.add(new BorderNameModel(board_nm, id));
                                                            }
                                                        } catch (JSONException e) {
                                                            throw new RuntimeException(e);
                                                        }

                                                    }

                                                    if (boardArr.size() > 0){
                                                        BoardAdapter boardAdapter = new BoardAdapter(HomeActivity.this, boardArr);
                                                        holder.boardRecyl.setLayoutManager(new GridLayoutManager(HomeActivity.this, 1));
                                                        holder.boardRecyl.setAdapter(boardAdapter);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    //Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
                                            queue.add(boardArrRequest);
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            //Toast.makeText(CreateAccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    RequestQueue createQueue = Volley.newRequestQueue(HomeActivity.this);
                                    createQueue.add(createString);
                                    create_board_dialog.dismiss();
                                }
                            });

                            create_board_dialog.show();
                        }
                    });



                    /**
                     * start add people in group
                     */
                    Dialog dialog_add_people = new Dialog(HomeActivity.this);

                    addPeopleBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_add_people.setContentView(R.layout.add_people_layout);
                            dialog_add_people.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            accountRecyl = dialog_add_people.findViewById(R.id.suggested_people);
                            SearchView add_people_Search = dialog_add_people.findViewById(R.id.search_people);



                            accountNameArr = new ArrayList<>();
                            AccountAdapter nameAdapter = new AccountAdapter(HomeActivity.this, accountNameArr);
                            JsonArrayRequest accountarrRequest = new JsonArrayRequest(Request.Method.POST, "https://raquib.000webhostapp.com/apps/array_of_managementaccount.php", null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    for (int i = 0; i < response.length(); i++) {
                                        try {
                                            JSONObject jsonObject = response.getJSONObject(i);
                                            String id = jsonObject.getString("userid");
                                            String name = jsonObject.getString("username");
                                            if (!admingroupid.equals(id)){
                                                accountNameArr.add(new AccountModel(id, name, admingroupid, groupnm));
                                            }
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }

                                    }

                                    if (accountNameArr.size() > 0){
                                        accountRecyl.setLayoutManager(new GridLayoutManager(HomeActivity.this, 1));
                                        accountRecyl.setAdapter(nameAdapter);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            RequestQueue queue3 = Volley.newRequestQueue(HomeActivity.this);
                            queue3.add(accountarrRequest);

                            add_people_Search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {
                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {
                                    return false;
                                }
                            });


                            dialog_add_people.show();
                        }
                    });

                    dialog.show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return groupNameArr.size();
        }

        public class groupView extends RecyclerView.ViewHolder{
            TextView firstDigit, groupName;
            ImageView showBoardofGroup, moreBtn;
            RecyclerView boardRecyl;
            FrameLayout groupFram;
            public groupView(@NonNull View itemView) {
                super(itemView);
                firstDigit = itemView.findViewById(R.id.group_frist_digit);
                groupName = itemView.findViewById(R.id.group_name);
                moreBtn   = itemView.findViewById(R.id.group_more_icon);
                showBoardofGroup = itemView.findViewById(R.id.show_group_file);
                boardRecyl = itemView.findViewById(R.id.board_recycle);
                groupFram = itemView.findViewById(R.id.framLayout);
            }
        }
    }


    /**
     *
     *
     */
    public void groupArrLoad() {
        groupNameArr = new ArrayList<>();
        checkgroupNameArr = new ArrayList<>();
        JsonArrayRequest accountRequest = new JsonArrayRequest(Request.Method.GET, "https://raquib.000webhostapp.com/apps/arr_of_group_name_data.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String userid = jsonObject.getString("userid");
                        String groupnm = jsonObject.getString("groupname");
                        String groupfd = jsonObject.getString("groupfirstdigit");
                        hashMap = new HashMap<>();
                        if (admingroupid.equals(userid)) {
                            hashMap.put("userid", userid);
                            hashMap.put("groupname", groupnm);
                            hashMap.put("groupfirstdigit", groupfd);
                            groupNameArr.add(hashMap);
                        }
                        checkgroupNameArr.add(groupnm.toLowerCase());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }

                if (groupNameArr.size() > 0){
                    adapter = new groupNameAdapter();
                    groupNameRecyl.setLayoutManager(new GridLayoutManager(HomeActivity.this, 1));
                    groupNameRecyl.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue2 = Volley.newRequestQueue(HomeActivity.this);
        queue2.add(accountRequest);
    }



    /**
     * start account array load

    public void accountArrLoad() {
        accountNameArr = new ArrayList<>();
        JsonArrayRequest accountarrRequest = new JsonArrayRequest(Request.Method.GET, "https://raquib.000webhostapp.com/apps/arr_of_group_name_data.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("userid");
                        String name = jsonObject.getString("groupname");
                        hashMap3 = new HashMap<>();
                        hashMap3.put("id", id);
                        hashMap3.put("name", name);
                        accountNameArr.add(new AccountModel(id, name));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }

//                nameAdapter = new accountNameAdapter();
//                accountRecyl.setLayoutManager(new GridLayoutManager(HomeActivity.this, 1));
//                accountRecyl.setAdapter(nameAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue3 = Volley.newRequestQueue(HomeActivity.this);
        queue3.add(accountarrRequest);
    }
     **/


}