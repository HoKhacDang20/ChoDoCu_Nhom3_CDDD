package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.data_models.DatHang;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ShipperMainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Button btnLogout, btnAccountInfo, btnPassWordChange, btnDanhSachDonHang, btnCacDonDangGiao, btnDanhSachShipper, btnLichSuDonHangShipper, btnCacDonHoanTat;
    private TextView txtCacDonDangGiaoNotify, txtCacDonHoanTatNotify, txtShipperAccountName, txtDanhSachDonHang;
    private ArrayList<DatHang> donDangGiao;
    private ArrayList<DatHang> donDaDongGoi;
    private ArrayList<DatHang> donHoanTat;
    private ImageView imgAccount;
    private String sUserName, userID;
    private Intent intent;
    private int permission = 0;
    public static ArrayList<String> shipperList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_main_layout);

        txtCacDonDangGiaoNotify = (TextView) findViewById(R.id.txtCacDonDangGiaoNotify);
        txtShipperAccountName = (TextView) findViewById(R.id.txtShipperAccountName);
        txtDanhSachDonHang = (TextView) findViewById(R.id.txtDanhSachDonHang);
        txtCacDonHoanTatNotify = (TextView) findViewById(R.id.txtCacDonHoanTatNotify);
        imgAccount = (ImageView) findViewById(R.id.imgAccount);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnPassWordChange = (Button) findViewById(R.id.btnPassWordChange);
        btnAccountInfo = (Button) findViewById(R.id.btnAccountInfo);
        btnDanhSachDonHang = (Button) findViewById(R.id.btnDanhSachDonHang);
        btnCacDonDangGiao =(Button) findViewById(R.id.btnCacDonDangGiao);
        btnDanhSachShipper = (Button) findViewById(R.id.btnDanhSachShipper);
        btnLichSuDonHangShipper = (Button) findViewById(R.id.btnLichSuDonHangShipper);
        btnCacDonHoanTat = (Button) findViewById(R.id.btnCacDonHoanTat);

        donDangGiao = new ArrayList<>();
        shipperList = new ArrayList<>();
        donHoanTat = new ArrayList<>();

        btnLogout.setOnClickListener(logoutClick);
        btnAccountInfo.setOnClickListener(accountInfoClick);
        btnPassWordChange.setOnClickListener(passwordChangeClick);
        btnDanhSachDonHang.setOnClickListener(donDaDongGoiClick);
        btnCacDonDangGiao.setOnClickListener(cacDonDangGiaoClick);
        btnDanhSachShipper.setOnClickListener(danhSachShipperClick);
        btnLichSuDonHangShipper.setOnClickListener(lichSuDonGiaoShipperClick);
        btnCacDonHoanTat.setOnClickListener(donHoanTatClick);

    }

    @Override
    protected void onResume() {
        super.onResume();
        shipperList.clear();
        donDangGiao.clear();
        donHoanTat.clear();
        donDaDongGoi = new ArrayList<>();
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserData.class).getPermission() == 3){
                    shipperList.add(snapshot.getValue(UserData.class).getUserName());
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

        databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(DatHang.class).getLoaiDonHang() != 1 && snapshot.getValue(DatHang.class).getTinhTrang() == 3){
                    donDaDongGoi.add(snapshot.getValue(DatHang.class));
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

        if(getIntent().getExtras() != null){

            sUserName = getIntent().getExtras().getString("UserName");

            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getUserName().equals(sUserName) && snapshot.getValue(UserData.class).getPermission() == 3){
                        permission = 3;
                        txtShipperAccountName.setText("Shipper - " + snapshot.getValue(UserData.class).getHoTen());
                        userID = snapshot.getValue(UserData.class).getUserID();
                        if(!snapshot.getValue(UserData.class).getImage().isEmpty()){
                            final Handler handler = new Handler();
                            final int delay = 1200; //milliseconds
                            handler.postDelayed(new Runnable(){
                                public void run(){
                                    imageLoad(snapshot.getValue(UserData.class).getImage());
//                                    handler.postDelayed(this, delay);
                                }
                            }, delay);
                        }
                        btnCacDonDangGiao.setVisibility(View.VISIBLE);
                        databaseReference.child("Shipper").child(userID).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(DatHang.class).getTinhTrang() != 7 && snapshot.getValue(DatHang.class).getTinhTrang() != -1){
                                    donDangGiao.add(snapshot.getValue(DatHang.class));
                                }
                                else if(snapshot.getValue(DatHang.class).getTinhTrang() == 7 || snapshot.getValue(DatHang.class).getTinhTrang() == -1){
                                    donHoanTat.add(snapshot.getValue(DatHang.class));
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
                    else if(snapshot.getValue(UserData.class).getUserName().equals(sUserName) && snapshot.getValue(UserData.class).getPermission() == 4){
                        permission = 4;
                        txtShipperAccountName.setText("Trưởng shipper: " + snapshot.getValue(UserData.class).getHoTen());
                        userID = snapshot.getValue(UserData.class).getUserID();
                        if(!snapshot.getValue(UserData.class).getImage().isEmpty()){
                            imageLoad(snapshot.getValue(UserData.class).getImage());
                        }
                        btnDanhSachDonHang.setVisibility(View.VISIBLE);
                        btnDanhSachShipper.setVisibility(View.VISIBLE);
                        btnLichSuDonHangShipper.setVisibility(View.VISIBLE);
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

        Handler handler = new Handler();
        int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(donDangGiao.size() != 0){
                    txtCacDonDangGiaoNotify.setVisibility(View.VISIBLE);
                    txtCacDonDangGiaoNotify.setText(String.valueOf(donDangGiao.size()));
                }
                else{
                    txtCacDonDangGiaoNotify.setVisibility(View.GONE);
                }

                if(donHoanTat.size() != 0){
                    txtCacDonHoanTatNotify.setVisibility(View.VISIBLE);
                    txtCacDonHoanTatNotify.setText(String.valueOf(donHoanTat.size()));
                }
                else{
                    txtCacDonHoanTatNotify.setVisibility(View.GONE);
                }

                if(donDaDongGoi.size() != 0 && permission == 4){
                    txtDanhSachDonHang.setVisibility(View.VISIBLE);
                    txtDanhSachDonHang.setText(String.valueOf(donDaDongGoi.size()));
                }
                else{
                    txtDanhSachDonHang.setVisibility(View.GONE);
                }
            }
        }, delay);
    }

    View.OnClickListener donHoanTatClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperDonHoanTatActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener lichSuDonGiaoShipperClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperLichSuDonGiaoActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener danhSachShipperClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperDanhSachActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener cacDonDangGiaoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener donDaDongGoiClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperDonDaDongGoiActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener passwordChangeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), DoiMatKhauActivity.class);
            intent.putExtra("UserName", sUserName);
            startActivity(intent);
        }
    };

    View.OnClickListener accountInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ThongTinTaiKhoanActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
//                            FirebaseAuth.getInstance().signOut();
//                            ShipperMainActivity.this.finish();
//                            break;
                            FirebaseAuth.getInstance().signOut();
                            intent = new Intent(v.getContext(), DangNhapActivity.class);
                            startActivity(intent);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;

                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Bạn muốn đăng xuất?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
        }
    };

    public void imageLoad(String sImageName){
        storageReference.child(sImageName + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ShipperMainActivity.this).load(uri).into(imgAccount);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(AdminMainActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}