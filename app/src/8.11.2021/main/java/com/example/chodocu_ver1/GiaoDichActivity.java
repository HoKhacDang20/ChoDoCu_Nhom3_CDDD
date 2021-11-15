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
import android.widget.TextView;

import com.example.chodocu_ver1.adapter.GiaoDichAdapter;
import com.example.chodocu_ver1.data_models.CuaHang;
import com.example.chodocu_ver1.data_models.DatHang;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GiaoDichActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Intent intent;
    private Button btnBack;
    private GridView gridDonHang;
    private String userName, userID;
    private TextView txtDoanhThu, txtGiaoDichUser;
    private ArrayList<DatHang> orderDataArrayList;
    private GiaoDichAdapter giaoDichAdapter;
    private long commission, tong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.giaodich_layout);

        btnBack = (Button) findViewById(R.id.btnBack);
        gridDonHang = (GridView) findViewById(R.id.gridDonHang);
        txtDoanhThu = (TextView) findViewById(R.id.txtDoanhThu);
        txtGiaoDichUser = (TextView) findViewById(R.id.txtGiaoDichUser);

        orderDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);
    }

    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
            orderDataArrayList.clear();
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getUserID().equals(userID)){
                        if(snapshot.getValue(UserData.class).getShopID().isEmpty()){
                            txtGiaoDichUser.setText("Giao dịch của user: " + snapshot.getValue(UserData.class).getHoTen());
                        }
                        else{
                            String shopID = snapshot.getValue(UserData.class).getShopID();
                            databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(CuaHang.class).getShopID().equals(shopID)){
                                        txtGiaoDichUser.setText("Giao dịch của shop: " + snapshot.getValue(CuaHang.class).getShopName());
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
                        }
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
            databaseReference.child("LichSuGiaoDich").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(DatHang.class).getNguoiBanID().equals(userID)) {
                        orderDataArrayList.add(snapshot.getValue(DatHang.class));
                        if (snapshot.getValue(DatHang.class).getTinhTrang() == 8) {
                            commission = snapshot.getValue(DatHang.class).getGiaTien() * snapshot.getValue(DatHang.class).getSellerCommission() /100;
                            tong += commission;
                            txtDoanhThu.setText(String.valueOf(tong) + "VNĐ");
                        }
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
            final int delay = 500; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    userLoad();
                }
            }, delay);
        }
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ThongKeActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };

    public void userLoad(){
        giaoDichAdapter = new GiaoDichAdapter(GiaoDichActivity.this, R.layout.giaodich_adapter_layout, orderDataArrayList);
        gridDonHang.setAdapter(giaoDichAdapter);
    }
}