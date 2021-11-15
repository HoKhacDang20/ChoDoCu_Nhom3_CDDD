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

import com.example.chodocu_ver1.adapter.YeuCauTaoShopAdapter;
import com.example.chodocu_ver1.data_models.CuaHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class YeuCau_DangKy_CuaHangActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Button btnBack;
    private GridView gridShopRequest;
    private ArrayList<CuaHang> shopDataArrayList;
    private Intent intent;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.yeucau_dangky_cuahang_layout);

        gridShopRequest = (GridView) findViewById(R.id.gridShopRequest);
        btnBack = (Button) findViewById(R.id.btnBack);

        shopDataArrayList = new ArrayList<>();
        btnBack.setOnClickListener(backClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
        }

        shopDataArrayList.clear();

        databaseReference.child("ShopRegistration").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                shopDataArrayList.add(snapshot.getValue(CuaHang.class));
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

        final Handler handler = new Handler();
        final int delay = 500; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){

                shopRequestLoad();

//                handler.postDelayed(this, delay);
            }
        }, delay);

        gridShopRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), ChiTiet_YeuCau_CuaHangActivity.class);
                intent.putExtra("ShopID", shopDataArrayList.get(position).getShopID());
                intent.putExtra("UserID", shopDataArrayList.get(position).getUserID());
                intent.putExtra("UserName", userName);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), AdminMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };

    public void shopRequestLoad(){
        YeuCauTaoShopAdapter shopRequestAdapter = new YeuCauTaoShopAdapter(YeuCau_DangKy_CuaHangActivity.this, R.layout.yeucau_cuahang_adapter_layout, shopDataArrayList);
        gridShopRequest.setAdapter(shopRequestAdapter);
    }
}