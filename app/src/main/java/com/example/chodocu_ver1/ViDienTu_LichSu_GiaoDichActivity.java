package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;

import com.example.chodocu_ver1.adapter.YeuCauChuyenKhoanAdapter;
import com.example.chodocu_ver1.data_models.UserDepositData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViDienTu_LichSu_GiaoDichActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<UserDepositData> userDepositDataArrayList;
    private GridView lstDepositRequest;
    private Button btnBack;
    private Intent intent;
    private String userName, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.vidientu_user_lichsu_giaodich_layout);

        lstDepositRequest = (GridView) findViewById(R.id.lstDepositRequest);
        btnBack = (Button) findViewById(R.id.btnBack);

        userDepositDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        userDepositDataArrayList.clear();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");

            databaseReference.child("UserDepositRequest").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserDepositData.class).getUserID().equals(userID)){
                        userDepositDataArrayList.add(snapshot.getValue(UserDepositData.class));
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
                    userDepositRequestLoad();
                }
            }, delay);
        }
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), ViDienTu_UserActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    public void userDepositRequestLoad(){
        YeuCauChuyenKhoanAdapter yeuCauChuyenKhoanAdapter = new YeuCauChuyenKhoanAdapter(ViDienTu_LichSu_GiaoDichActivity.this,R.layout.yeu_cau_chuyen_khoan_adapter_layout, userDepositDataArrayList);
        lstDepositRequest.setAdapter(yeuCauChuyenKhoanAdapter);
    }
}