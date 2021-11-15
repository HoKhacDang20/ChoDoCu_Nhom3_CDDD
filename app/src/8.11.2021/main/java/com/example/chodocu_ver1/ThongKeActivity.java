package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chodocu_ver1.adapter.ThongKeAdapter;
import com.example.chodocu_ver1.data_models.DatHang;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ThongKeActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Intent intent;
    private String userName;
    private Button btnBack;
    private GridView gridThongke;
    private TextView txtDoanhThu;
    private ArrayList<UserData> userDataArrayList;
    private ArrayList<DatHang> orderDataArrayList;
    private ThongKeAdapter thongKeAdapter;
    private long tong, commission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_thong_ke);

        btnBack = (Button) findViewById(R.id.btnBack);
        gridThongke = (GridView) findViewById(R.id.gridThongke);
        txtDoanhThu = (TextView) findViewById(R.id.txtDoanhThu);

        userDataArrayList = new ArrayList<>();
        orderDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);
        gridThongke.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), GiaoDichActivity.class);
                intent.putExtra("UserID", userDataArrayList.get(position).getUserID());
                intent.putExtra("UserName", userName);
                startActivity(intent);
            }
        });

        if(getIntent().getExtras() != null){

            userName = getIntent().getExtras().getString("UserName");
            //Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(UserData.class).getPermission() == 1) {
                        UserData userData = snapshot.getValue(UserData.class);
                        //Toast.makeText(getApplicationContext(), userData.getHoTen(), Toast.LENGTH_SHORT).show();
                        userDataArrayList.add(userData);
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
                    orderDataArrayList.add(snapshot.getValue(DatHang.class));
                    if (snapshot.getValue(DatHang.class).getTinhTrang() == 8 && snapshot.getValue(DatHang.class).getLoaiDonHang() != 1) {
                        commission = snapshot.getValue(DatHang.class).getGiaTien() * snapshot.getValue(DatHang.class).getSellerCommission() / 100;
                        tong = tong + commission;
                        txtDoanhThu.setText(String.valueOf(tong) + "VNĐ");
                    }
                    if (orderDataArrayList != null && userDataArrayList != null) {
                        userLoad();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
    public void userLoad(){
        thongKeAdapter = new ThongKeAdapter(ThongKeActivity.this, R.layout.thongke_adapter_layout, userDataArrayList, orderDataArrayList);
        gridThongke.setAdapter(thongKeAdapter);
    }
}