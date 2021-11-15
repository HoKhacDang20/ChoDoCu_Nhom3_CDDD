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

import com.example.chodocu_ver1.adapter.YeuCauChuyenKhoanAdapter;
import com.example.chodocu_ver1.data_models.UserDepositData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class YeuCauChuyenKhoanActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Button btnBack;
    private String userName;
    private Intent intent;
    private GridView gridYeuCauChuyenKhoan;
    private ArrayList<UserDepositData> userDepositDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.yeu_cau_chuyen_khoan_layout);

        btnBack = (Button) findViewById(R.id.btnBack);
        gridYeuCauChuyenKhoan = (GridView) findViewById(R.id.gridYeuCauChuyenKhoan);

        userDepositDataArrayList = new ArrayList<>();

        gridYeuCauChuyenKhoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), YeuCauChuyenKhoanDetailActivity.class);
                intent.putExtra("UserName", userName);
                intent.putExtra("IDGiaoDich", userDepositDataArrayList.get(position).getIdGiaoDich());
                intent.putExtra("UserID", userDepositDataArrayList.get(position).getUserID());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), AdminMainActivity.class);
                intent.putExtra("UserName", userName);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
        }

        userDepositDataArrayList.clear();
        databaseReference.child("UserDepositRequest").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserDepositData.class).getTinhTrang() == 0){
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
                userRequestLoad();
            }
        }, delay);

    }

    public void userRequestLoad(){
        YeuCauChuyenKhoanAdapter yeuCauChuyenKhoanAdapter = new YeuCauChuyenKhoanAdapter(YeuCauChuyenKhoanActivity.this, R.layout.yeu_cau_chuyen_khoan_adapter_layout, userDepositDataArrayList);
        gridYeuCauChuyenKhoan.setAdapter(yeuCauChuyenKhoanAdapter);
    }
}