package com.example.managementapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.boardView> {

    private Context context;
    private ArrayList<BorderNameModel> boardArr;

    public BoardAdapter(Context context, ArrayList<BorderNameModel> boardArr) {
        this.context = context;
        this.boardArr = boardArr;
    }

    @NonNull
    @Override
    public boardView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.board_layout, parent, false);
        return new boardView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull boardView holder, @SuppressLint("RecyclerView") int position) {
        holder.boardName.setText(boardArr.get(position).getBorderName());


        holder.boardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.boardBtn);
                popupMenu.getMenuInflater().inflate(R.menu.menu,  popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.remove){
                            int boardid = boardArr.get(position).getId();
                            String url = "https://raquib.000webhostapp.com/apps/board_file_remove.php?id="+boardid;
                            StringRequest createString = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(context, "Board remove successfully", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            });
                            RequestQueue createQueue = Volley.newRequestQueue(context);
                            createQueue.add(createString);
                        }else if (id == R.id.spam){

                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardArr.size();
    }

    public class boardView extends RecyclerView.ViewHolder{
        TextView boardName;
        ImageView boardBtn;
        public boardView(@NonNull View itemView) {
            super(itemView);

            boardName = itemView.findViewById(R.id.board_name);
            boardBtn = itemView.findViewById(R.id.board_btn);
        }
    }
}
