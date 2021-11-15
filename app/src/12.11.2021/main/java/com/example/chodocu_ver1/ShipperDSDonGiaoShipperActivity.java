package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;

import com.example.chodocu_ver1.adapter.DonMuaAdapter;
import com.example.chodocu_ver1.data_models.DatHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShipperDSDonGiaoShipperActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<DatHang> orderDataArrayList;
    private GridView gridDonDangGiao;
    private Button btnBack;
    private String userName, userID, shipperID;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_d_s_don_giao_shipper_layout);

        gridDonDangGiao = (GridView) findViewById(R.id.gridDonDangGiao);
        btnBack = (Button) findViewById(R.id.btnBack);



        btnBack.setOnClickListener(backClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        orderDataArrayList = new ArrayList<>();

        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            shipperID = getIntent().getExtras().getString("ShipperID");

            databaseReference.child("Shipper").child(shipperID).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    orderDataArrayList.add(snapshot.getValue(DatHang.class));
                    orderLoad();
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
            finish();
            intent = new Intent(v.getContext(), ShipperDanhSachActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    public void orderLoad(){
        DonMuaAdapter donMuaAdapter = new DonMuaAdapter(ShipperDSDonGiaoShipperActivity.this, R.layout.don_mua_adapter_layout, orderDataArrayList);
        gridDonDangGiao.setAdapter(donMuaAdapter);
    }
}