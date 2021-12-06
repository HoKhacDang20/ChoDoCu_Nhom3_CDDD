package com.example.chodocu_nhom3_cddd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.chodocu_nhom3_cddd.adapter.DonMuaAdapter;
import com.example.chodocu_nhom3_cddd.data_models.DatHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShipperLichSuDonGiaoActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Button btnBack;
    private GridView gridDonGiaoShipper;
    private EditText edtFind;
    private Intent intent;
    private ArrayList<DatHang> orderDataArrayList;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_lich_su_don_giao_layout);

        edtFind = (EditText) findViewById(R.id.edtFind);
        btnBack = (Button) findViewById(R.id.btnBack);
        gridDonGiaoShipper = (GridView) findViewById(R.id.gridDonGiaoShipper);

        orderDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);

        gridDonGiaoShipper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //intent = new Intent(view.getContext(), ShipperLichSuChiTietDonGiaoActivity.class);
                intent.putExtra("UserName", userName);
                intent.putExtra("DonHangID", orderDataArrayList.get(position).getDonHangID());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        orderDataArrayList.clear();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");

            databaseReference.child("LichSuGiaoDich").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(!snapshot.getValue(DatHang.class).getShipperID().isEmpty()){
                        orderDataArrayList.add(snapshot.getValue(DatHang.class));
                    }
                    donGiaoLoad();
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

    public void donGiaoLoad(){
        DonMuaAdapter donMuaAdapter = new DonMuaAdapter(ShipperLichSuDonGiaoActivity.this, R.layout.don_mua_adapter_layout, orderDataArrayList);
        gridDonGiaoShipper.setAdapter(donMuaAdapter);
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //intent = new Intent(v.getContext(), AdminMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}