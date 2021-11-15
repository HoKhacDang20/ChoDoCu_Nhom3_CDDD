package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.chodocu_ver1.adapter.ChoHangAdapter;
import com.example.chodocu_ver1.data_models.DatHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class User_Cho_ChuyenKhoanActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Intent intent;
    private Button btnBack;
    private GridView gridDonHang;
    private String userName, userID;
    private ChoHangAdapter waitingAdapter;
    private ArrayList<DatHang> orderDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.user_doichuyenkhoan);

        gridDonHang = (GridView) findViewById(R.id.gridDonHang);
        btnBack = (Button) findViewById(R.id.btnBack);

        orderDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);

        gridDonHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), ChiTiet_ChoActivity.class);
                intent.putExtra("UserID", userID);
                intent.putExtra("UserName", userName);
                intent.putExtra("DonHangID", orderDataArrayList.get(position).getDonHangID());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");

            orderDataArrayList.clear();
            databaseReference.child("MoneyIncome").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(DatHang.class).getNguoiBanID().equals(userID)){
                        orderDataArrayList.add(snapshot.getValue(DatHang.class));
                    }
                    donHangLoad();
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

        }
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), UserMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };

    public void donHangLoad(){
        waitingAdapter = new ChoHangAdapter(User_Cho_ChuyenKhoanActivity.this, R.layout.don_mua_adapter_layout, orderDataArrayList);
        gridDonHang.setAdapter(waitingAdapter);
    }
}