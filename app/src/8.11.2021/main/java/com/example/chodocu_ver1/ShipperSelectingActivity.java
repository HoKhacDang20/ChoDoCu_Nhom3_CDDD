package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.chodocu_ver1.adapter.ShipperAdapter;
import com.example.chodocu_ver1.data_models.DatHang;
import com.example.chodocu_ver1.data_models.SanPham;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShipperSelectingActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<UserData> userDataArrayList;
    private GridView gridShipper;
    private Button btnBack;
    private EditText edtFind;
    private Intent intent;
    private String userName, userID, donHangID, shipperID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_selecting_layout);

        gridShipper = (GridView) findViewById(R.id.gridShipper);
        btnBack = (Button) findViewById(R.id.btnBack);
        edtFind = (EditText) findViewById(R.id.edtFind);

        btnBack.setOnClickListener(backClick);



        edtFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    userDataArrayList.clear();
                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getValue(UserData.class).getPermission() == 3){
                                if(snapshot.getValue(UserData.class).getSoDienThoai().contains(edtFind.getText().toString()) || snapshot.getValue(UserData.class).getHoTen().toLowerCase().contains(edtFind.getText().toString().toLowerCase()) || snapshot.getValue(UserData.class).getUserName().toLowerCase().contains(edtFind.getText().toString().toLowerCase())){
                                    userDataArrayList.add(snapshot.getValue(UserData.class));
                                }
                            }
                            shipperLoad();
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
                else{
                    userDataArrayList.clear();
                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getValue(UserData.class).getPermission() == 3){
                                userDataArrayList.add(snapshot.getValue(UserData.class));
                            }
                            shipperLoad();
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
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        userDataArrayList = new ArrayList<>();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
            donHangID = getIntent().getExtras().getString("DonHangID");
            shipperID = getIntent().getExtras().getString("ShipperID");

            gridShipper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:

                                    databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                                                String donHangID = snapshot.getValue(DatHang.class).getDonHangID();
                                                String nguoiMuaID = snapshot.getValue(DatHang.class).getNguoiMuaID();
                                                String nguoiBanID = snapshot.getValue(DatHang.class).getNguoiBanID();
                                                String ngayTaoDon = snapshot.getValue(DatHang.class).getNgayTaoDonHang();
                                                String sdt = snapshot.getValue(DatHang.class).getSoDienThoai();
                                                String diaChi = snapshot.getValue(DatHang.class).getDiaChi();
                                                SanPham sanPham = snapshot.getValue(DatHang.class).getSanPham();
                                                int loaiDonHang = snapshot.getValue(DatHang.class).getLoaiDonHang();
                                                int sellerCommission = snapshot.getValue(DatHang.class).getSellerCommission();
                                                long giaTien = snapshot.getValue(DatHang.class).getGiaTien();


                                                if(shipperID != "" && !shipperID.equals(userDataArrayList.get(position).getUserID())){
                                                    databaseReference.child("Shipper").child(shipperID).child(donHangID).removeValue();
                                                }


                                                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                        if(snapshot.getValue(UserData.class).getUserID().equals(userDataArrayList.get(position).getUserID())){

                                                            DatHang orderData = new DatHang(donHangID, nguoiMuaID, nguoiBanID,
                                                                    ngayTaoDon, sdt, diaChi,
                                                                    sanPham, loaiDonHang, 4, sellerCommission, giaTien, snapshot.getValue(UserData.class).getUserID());

                                                            databaseReference.child("DonHang").child(donHangID).setValue(orderData);

                                                            databaseReference.child("Shipper").child(snapshot.getValue(UserData.class).getUserID()).child(donHangID).setValue(orderData);

                                                            finish();
                                                            intent = new Intent(view.getContext(), XuLyDonHang_NguoiBanActivity.class);
                                                            intent.putExtra("UserName", userName);
                                                            intent.putExtra("UserID", userID);
                                                            intent.putExtra("DonHangID", donHangID);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                            startActivity(intent);
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

                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    return;
                            }
                        }
                    };
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                    alert.setMessage("Bạn chọn giao đơn hàng cho shipper " + userDataArrayList.get(position).getHoTen() + "?").setPositiveButton("Yes", dialog).setNegativeButton("No", dialog).show();

                }
            });

            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getPermission() == 3){
                        userDataArrayList.add(snapshot.getValue(UserData.class));
                    }
                    shipperLoad();
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
            intent = new Intent(v.getContext(), XuLyDonHang_NguoiBanActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.putExtra("ShipperID", "");
            intent.putExtra("DonHangID", donHangID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    public void shipperLoad(){
        ShipperAdapter shipperAdapter = new ShipperAdapter(ShipperSelectingActivity.this, R.layout.shipper_adapter_layout, userDataArrayList);
        gridShipper.setAdapter(shipperAdapter);
    }
}