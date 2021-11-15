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

import com.example.chodocu_ver1.adapter.ThongBaoAdapter;
import com.example.chodocu_ver1.data_models.RemoveSanPham;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class XoaSanPhamActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private GridView gridNotification;
    private Button btnBack, btnClear;
    private String userID, userName;
    private ArrayList<RemoveSanPham> removeProductDataArrayList;
    private ThongBaoAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.xoa_sanpham);

        gridNotification = (GridView) findViewById(R.id.gridNotification);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnClear = (Button) findViewById(R.id.btnClear);

        removeProductDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);
        btnClear.setOnClickListener(clearClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            removeProductDataArrayList.clear();
            databaseReference.child("DeletedProduct").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    removeProductDataArrayList.add(snapshot.getValue(RemoveSanPham.class));
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
            handler.postDelayed(new Runnable() {
                public void run() {
                    userLoad();
                }
            }, delay);
        }
    }

    public void userLoad() {
        notificationAdapter = new ThongBaoAdapter(XoaSanPhamActivity.this, R.layout.thongbao_adapter, removeProductDataArrayList);
        gridNotification.setAdapter(notificationAdapter);
    }

    View.OnClickListener clearClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            removeProductDataArrayList.clear();
            databaseReference.child("DeletedProduct").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    removeProductDataArrayList.add(snapshot.getValue(RemoveSanPham.class));
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
            handler.postDelayed(new Runnable() {
                public void run() {
                    userLoad();
                }
            }, delay);
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), UserMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
}