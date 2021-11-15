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

import com.example.chodocu_ver1.adapter.DonMuaAdapter;
import com.example.chodocu_ver1.data_models.DatHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DonBanActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private GridView gridDonBan;
    private Button btnBack;
    private ArrayList<DatHang> orderDataArrayList;
    private String userID, userName;
    private DonMuaAdapter donMuaAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.don_ban_layout);

        orderDataArrayList = new ArrayList<>();

        btnBack = (Button) findViewById(R.id.btnBack);
        gridDonBan = (GridView) findViewById(R.id.gridDonBan);

        btnBack.setOnClickListener(backClick);

        gridDonBan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(orderDataArrayList.get(position).getLoaiDonHang() == 1){
                    intent = new Intent(view.getContext(), DatHangThanhToanTrucTiepActivity.class);
                    intent.putExtra("UserID", userID);
                    intent.putExtra("UserName", userName);
                    intent.putExtra("DonHangID", orderDataArrayList.get(position).getDonHangID());
                    startActivity(intent);
                }
                else if(orderDataArrayList.get(position).getLoaiDonHang() == 2 || orderDataArrayList.get(position).getLoaiDonHang() == 3){
                    intent = new Intent(view.getContext(), XuLyDonHang_NguoiBanActivity.class);
                    intent.putExtra("UserID", userID);
                    intent.putExtra("UserName", userName);
                    intent.putExtra("DonHangID", orderDataArrayList.get(position).getDonHangID());
                    startActivity(intent);
                }
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
            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(DatHang.class).getNguoiBanID().equals(userID) && snapshot.getValue(DatHang.class).getTinhTrang() < 7){
                        orderDataArrayList.add(snapshot.getValue(DatHang.class));
                    }
                    donMuaLoad();
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
//            Intent intent = new Intent(v.getContext(), UserMainActivity.class);
//            intent.putExtra("UserName", userName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
        }
    };

    public void donMuaLoad(){
        donMuaAdapter = new DonMuaAdapter(DonBanActivity.this, R.layout.don_mua_adapter_layout, orderDataArrayList);
        gridDonBan.setAdapter(donMuaAdapter);
    }
}