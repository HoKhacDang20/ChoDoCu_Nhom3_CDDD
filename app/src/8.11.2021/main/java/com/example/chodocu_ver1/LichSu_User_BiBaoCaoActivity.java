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

import com.example.chodocu_ver1.adapter.GiaoDichAdapter;
import com.example.chodocu_ver1.data_models.DatHang;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class LichSu_User_BiBaoCaoActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Intent intent;
    private Button btnBack;
    private String userName, reportID, userID, sUserID;
    private ArrayList<DatHang> orderDataArrayList;
    private GiaoDichAdapter giaoDichAdapter;
    private GridView gridHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.lichsu_user_bibaocao);

        btnBack = (Button) findViewById(R.id.btnBack);
        gridHistory = (GridView) findViewById(R.id.gridHistory);

        orderDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("UserName");
            sUserID = getIntent().getExtras().getString("sUserID");
            reportID = getIntent().getExtras().getString("ReportID");
            userID = getIntent().getExtras().getString("UserID");
            orderDataArrayList.clear();
            databaseReference.child("LichSuGiaoDich").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(DatHang.class).getNguoiMuaID().equals(userID)) {
                        orderDataArrayList.add(snapshot.getValue(DatHang.class));
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
            final Handler handler = new Handler();
            final int delay = 1000; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    userLoad();
                }
            }, delay);

        }
    }

    public void userLoad(){
        giaoDichAdapter = new GiaoDichAdapter(LichSu_User_BiBaoCaoActivity.this, R.layout.giaodich_adapter_layout, orderDataArrayList);
        gridHistory.setAdapter(giaoDichAdapter);
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), UserChiTietReportActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("ReportID", reportID);
            intent.putExtra("sUserID", sUserID);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}