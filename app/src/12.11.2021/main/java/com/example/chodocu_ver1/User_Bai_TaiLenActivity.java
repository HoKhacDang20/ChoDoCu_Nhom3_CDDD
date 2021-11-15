package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.chodocu_ver1.adapter.SanPhamAdapter;
import com.example.chodocu_ver1.data_models.SanPham;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class User_Bai_TaiLenActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<SanPham> sanPhamArrayList;
    private Button btnBack;
    private GridView gridUploadedPost;
    private String userID, userName;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_bai_tailen_layout);

        btnBack = (Button) findViewById(R.id.btnBack);
        gridUploadedPost = (GridView) findViewById(R.id.gridUploadedPost);

        sanPhamArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);

        gridUploadedPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), User_ChinhSua_SanPhamActivity.class);
                intent.putExtra("UserName", userName);
                intent.putExtra("UserID", userID);
                intent.putExtra("ProductID", sanPhamArrayList.get(position).getiD());
                intent.putExtra("NavigateTo", "UserPost");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        sanPhamArrayList.clear();

        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");

            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getUserID().equals(userID)){
                        sanPhamArrayList.add(snapshot.getValue(SanPham.class));
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    userSanPhamLoad();
                }
            }, delay);

        }
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), UserMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    public void userSanPhamLoad(){
        SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(User_Bai_TaiLenActivity.this, R.layout.san_pham_adapter_layout, sanPhamArrayList);
        gridUploadedPost.setAdapter(sanPhamAdapter);
    }
}