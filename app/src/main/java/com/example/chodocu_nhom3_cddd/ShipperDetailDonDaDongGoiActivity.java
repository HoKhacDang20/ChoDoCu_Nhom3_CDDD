package com.example.chodocu_nhom3_cddd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chodocu_nhom3_cddd.data_models.DatHang;
import com.example.chodocu_nhom3_cddd.data_models.SanPham;
import com.example.chodocu_nhom3_cddd.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ShipperDetailDonDaDongGoiActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private EditText edtUserName, edtDiaChi, edtLienHe, edtSellerDiaChi, edtSellerLienHe, edtSellerUserName, edtShipperUserName;
    private TextView txtTenSP, txtSoLuongSP, txtGiaSP, txtPhuongThucThanhToan, txtTongGiaTriDonHang, txtNameLabel, txtDiaChi, txtSellerLabel, txtSellerDiaChi;
    private Button btnAccept, btnRefuse, btnBack, btnTimShipper;
    private Intent intent;
    private String userName, userID, donHangID, nguoiBanID, nguoiMuaID, shipperUserName;
    private int loaiDonHang;
    private long tongGiaTri;
//    private ArrayList<String> shipperList = ShipperMainActivity.shipperList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_detail_don_da_dong_goi_layout);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        edtSellerUserName = (EditText) findViewById(R.id.edtSellerUserName);
        edtSellerLienHe = (EditText) findViewById(R.id.edtSellerLienHe);
        edtSellerDiaChi = (EditText) findViewById(R.id.edtSellerDiaChi);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtLienHe = (EditText) findViewById(R.id.edtLienHe);
        edtShipperUserName = (EditText) findViewById(R.id.edtShipperUserName);
        txtSellerLabel = (TextView) findViewById(R.id.txtSellerLabel);
        txtSellerDiaChi = (TextView) findViewById(R.id.txtSellerDiaChi);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtPhuongThucThanhToan = (TextView) findViewById(R.id.txtPhuongThucThanhToan);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        txtNameLabel = (TextView) findViewById(R.id.txtNameLabel);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnTimShipper = (Button) findViewById(R.id.btnTimShipper);

        btnBack.setOnClickListener(backClick);
        btnAccept.setOnClickListener(acceptClick);
        btnTimShipper.setOnClickListener(timShipperClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ShipperDetailDonDaDongGoiActivity.this, android.R.layout.simple_spinner_item, shipperList);
//        spnShipper.setAdapter(arrayAdapter);

        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            donHangID = getIntent().getExtras().getString("DonHangID");
            shipperUserName = getIntent().getExtras().getString("ShipperUserName");

            if(!shipperUserName.isEmpty()){
                edtShipperUserName.setText(shipperUserName);
            }

            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                        nguoiBanID = snapshot.getValue(DatHang.class).getNguoiBanID();
                        nguoiMuaID = snapshot.getValue(DatHang.class).getNguoiMuaID();
                        loaiDonHang = snapshot.getValue(DatHang.class).getLoaiDonHang();
                        tongGiaTri = snapshot.getValue(DatHang.class).getGiaTien();
                        storageReference.child(snapshot.getValue(DatHang.class).getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ShipperDetailDonDaDongGoiActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtNameLabel.setText("Thông tin người mua:");
                        txtDiaChi.setText("Địa chỉ giao hàng:");
                        txtSellerLabel.setText("Thông tin người bán:");
                        txtSellerDiaChi.setText("Địa chỉ lấy hàng: ");

                        if(snapshot.getValue(DatHang.class).getSanPham().getTinhTrang() == 0){

                            txtTenSP.setText(snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - New");
                        }
                        else {
                            txtTenSP.setText(snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - 2nd");
                        }

                        txtSoLuongSP.setText("Số lượng: " + String.valueOf(snapshot.getValue(DatHang.class).getSanPham().getSoLuong()) + " x ");
                        txtGiaSP.setText(String.valueOf(snapshot.getValue(DatHang.class).getSanPham().getGiaTien()) + "vnđ");
                        txtTongGiaTriDonHang.setText(String.valueOf(snapshot.getValue(DatHang.class).getGiaTien()) + "vnđ");

                        String diaChi = snapshot.getValue(DatHang.class).getDiaChi();
                        String lienHe = snapshot.getValue(DatHang.class).getSoDienThoai();

                        if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 2){
                            txtPhuongThucThanhToan.setText("Thanh toán COD!");

                        }
                        else{
                            txtPhuongThucThanhToan.setText("Thanh toán E-Wallet!");
                        }
                        if(snapshot.getValue(DatHang.class).getTinhTrang() < 3){
                            btnAccept.setEnabled(false);
                            btnRefuse.setEnabled(false);
                        }
                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(UserData.class).getUserID().equals(nguoiMuaID)){
                                    edtUserName.setText("Họ tên: " + snapshot.getValue(UserData.class).getHoTen());
                                    edtDiaChi.setText(diaChi);
                                    edtLienHe.setText(lienHe);
                                }
                                if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                    edtSellerUserName.setText("Họ tên: " + snapshot.getValue(UserData.class).getHoTen());
                                    edtSellerDiaChi.setText(snapshot.getValue(UserData.class).getDiaChi());
                                    edtSellerLienHe.setText(snapshot.getValue(UserData.class).getSoDienThoai());
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
        }
    }

    View.OnClickListener timShipperClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //intent = new Intent(v.getContext(), ShipperSelectingActivity.class);
            intent.putExtra("UserID", userID);
            intent.putExtra("UserName", userName);
            intent.putExtra("DonHangID", donHangID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener acceptClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtShipperUserName.getText().toString().isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn shipper!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else {
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

                                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    if(snapshot.getValue(UserData.class).getUserName().equals(edtShipperUserName.getText().toString())){
                                                        DatHang orderData = new DatHang(donHangID, nguoiMuaID, nguoiBanID,
                                                                ngayTaoDon, sdt, diaChi,
                                                                sanPham, loaiDonHang, 4, sellerCommission, giaTien, snapshot.getValue(UserData.class).getUserID());

                                                        databaseReference.child("DonHang").child(donHangID).setValue(orderData);

                                                        databaseReference.child("Shipper").child(snapshot.getValue(UserData.class).getUserID()).child(donHangID).setValue(orderData);

                                                        finish();
                                                        //intent = new Intent(v.getContext(), ShipperDonDaDongGoiActivity.class);
                                                        intent.putExtra("UserName", userName);
                                                        intent.putExtra("UserID", userID);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Xác nhận giao đơn cho " + edtShipperUserName.getText().toString() + "?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }

        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            //intent = new Intent(v.getContext(), ShipperDonDaDongGoiActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
}