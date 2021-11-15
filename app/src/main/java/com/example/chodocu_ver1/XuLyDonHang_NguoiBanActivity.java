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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.data_models.DatHang;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class XuLyDonHang_NguoiBanActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private TextView txtChonNguoiGiaoHang, txtShipperPhuTrach, txtShipperUserName, txtHoTenShipper, txtSDTShipper, txtDiaChiShipper, txtTenSP, txtSoLuongSP, txtMaVanDon, txtGiaSP, txtHoTenNguoiMua, txtDiaChi, txtLienHe, txtTongGiaTriDonHang, txtShipperLabel, txtShipperName, txtShipperPhone;
    private CheckBox cbProccessing, cbPacking, cbDelivery;
    private Button btnCancelOrder, btnBack, btnTimShipper;
    private EditText edtShipperID;
    private String shipperID = "";
    private String userName, userID, donHangID, nguoiMuaID, nguoiBanID;
    private int loaiDonHang;
    private long tongGiaTri;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.xuli_donhang_nguoiban);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        edtShipperID = (EditText) findViewById(R.id.edtShipperID);
        txtChonNguoiGiaoHang = (TextView) findViewById(R.id.txtChonNguoiGiaoHang);
        txtShipperPhuTrach = (TextView) findViewById(R.id.txtShipperPhuTrach);
        txtShipperUserName = (TextView) findViewById(R.id.txtShipperUserName);
        txtHoTenShipper = (TextView) findViewById(R.id.txtHoTenShipper);
        txtSDTShipper = (TextView) findViewById(R.id.txtSDTShipper);
        txtDiaChiShipper = (TextView) findViewById(R.id.txtDiaChiShipper);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtHoTenNguoiMua = (TextView) findViewById(R.id.txtHoTenNguoiMua);
        txtMaVanDon = (TextView) findViewById(R.id.txtMaVanDon);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        txtLienHe = (TextView) findViewById(R.id.txtLienHe);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        txtShipperLabel = (TextView) findViewById(R.id.txtShipperLabel);
        txtShipperPhone = (TextView) findViewById(R.id.txtShipperPhone);
        txtShipperName = (TextView) findViewById(R.id.txtShipperName);
        cbProccessing = (CheckBox) findViewById(R.id.cbProccessing);
        cbPacking = (CheckBox) findViewById(R.id.cbPacking);
        cbDelivery = (CheckBox) findViewById(R.id.cbDelivery);
        btnCancelOrder = (Button) findViewById(R.id.btnCancelOrder);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnTimShipper = (Button) findViewById(R.id.btnTimShipper);



        btnBack.setOnClickListener(backClick);
        btnCancelOrder.setOnClickListener(cancelOrderClick);
        btnTimShipper.setOnClickListener(timShipperClick);

        cbDelivery.setOnCheckedChangeListener(deliveryCheck);
        cbPacking.setOnCheckedChangeListener(packingCheck);
        cbProccessing.setOnCheckedChangeListener(proccessingCheck);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            donHangID = getIntent().getExtras().getString("DonHangID");

            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                        nguoiMuaID = snapshot.getValue(DatHang.class).getNguoiMuaID();
                        nguoiBanID = snapshot.getValue(DatHang.class).getNguoiBanID();
                        loaiDonHang = snapshot.getValue(DatHang.class).getLoaiDonHang();
                        tongGiaTri = snapshot.getValue(DatHang.class).getGiaTien();

                        if(!snapshot.getValue(DatHang.class).getShipperID().isEmpty()){
                            edtShipperID.setText(snapshot.getValue(DatHang.class).getShipperID());
                            shipperID = snapshot.getValue(DatHang.class).getShipperID();

                            txtShipperPhuTrach.setVisibility(View.VISIBLE);
                            txtShipperUserName.setVisibility(View.VISIBLE);
                            txtHoTenShipper.setVisibility(View.VISIBLE);
                            txtSDTShipper.setVisibility(View.VISIBLE);
                            txtDiaChiShipper.setVisibility(View.VISIBLE);

                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(UserData.class).getUserID().equals(shipperID)){
                                        txtShipperUserName.setText("User name: " + snapshot.getValue(UserData.class).getUserName());
                                        txtHoTenShipper.setText("Họ tên: " + snapshot.getValue(UserData.class).getHoTen());
                                        txtSDTShipper.setText("Số điện thoại: " + snapshot.getValue(UserData.class).getSoDienThoai());
                                        txtDiaChiShipper.setText("Địa chỉ: " + snapshot.getValue(UserData.class).getDiaChi());
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

                        if(snapshot.getValue(DatHang.class).getTinhTrang() >= 3){
                            btnTimShipper.setEnabled(true);
                            txtChonNguoiGiaoHang.setVisibility(View.VISIBLE);
                            edtShipperID.setVisibility(View.VISIBLE);
                            btnTimShipper.setVisibility(View.VISIBLE);
                        }
                        storageReference.child(snapshot.getValue(DatHang.class).getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(XuLyDonHang_NguoiBanActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        txtMaVanDon.setText("Mã vận đơn: " + snapshot.getValue(DatHang.class).getDonHangID());
                        if(!snapshot.getValue(DatHang.class).getShipperID().isEmpty()){
                            txtShipperLabel.setVisibility(View.VISIBLE);
                            String shipperID = snapshot.getValue(DatHang.class).getShipperID();
                            txtShipperName.setVisibility(View.VISIBLE);
                            txtShipperPhone.setVisibility(View.VISIBLE);
                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(UserData.class).getUserID().equals(shipperID)){
                                        txtShipperName.setText("Họ tên người giao: " + snapshot.getValue(UserData.class).getHoTen());
                                        txtShipperPhone.setText("Số điện thoại: " + snapshot.getValue(UserData.class).getSoDienThoai());
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

                        if(snapshot.getValue(DatHang.class).getSanPham().getTinhTrang() == 0){
                            txtTenSP.setText(snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - New");
                        }
                        else if(snapshot.getValue(DatHang.class).getSanPham().getTinhTrang() == 1){
                            txtTenSP.setText(snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - 2nd");
                        }

                        txtSoLuongSP.setText("Số lượng: " + String.valueOf(snapshot.getValue(DatHang.class).getSanPham().getSoLuong()) + " x ");
                        txtGiaSP.setText(String.valueOf(snapshot.getValue(DatHang.class).getSanPham().getGiaTien()) + "vnđ");
                        txtTongGiaTriDonHang.setText("Tổng tiền: " + String.valueOf(snapshot.getValue(DatHang.class).getGiaTien()) + "vnđ");
                        txtDiaChi.setText("Địa chỉ: " + snapshot.getValue(DatHang.class).getDiaChi());
                        txtLienHe.setText("Liên hệ: " + snapshot.getValue(DatHang.class).getSoDienThoai());

                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(UserData.class).getUserID().equals(nguoiMuaID)){
                                    txtHoTenNguoiMua.setText(snapshot.getValue(UserData.class).getHoTen());

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

                        if(snapshot.getValue(DatHang.class).getTinhTrang() < 1){
                            cbProccessing.setChecked(false);
                            cbProccessing.setEnabled(true);
                            cbPacking.setChecked(false);
                            cbDelivery.setChecked(false);
                        }
                        else if(snapshot.getValue(DatHang.class).getTinhTrang() < 2){
                            cbPacking.setChecked(false);
                            cbPacking.setEnabled(true);
                            cbDelivery.setChecked(false);
                        }
                        else if(snapshot.getValue(DatHang.class).getTinhTrang() < 3){
                            cbDelivery.setChecked(false);
                            cbDelivery.setEnabled(true);
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
        }
    }

    View.OnClickListener timShipperClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperSelectingActivity.class);
            intent.putExtra("UserID", userID);
            intent.putExtra("UserName", userName);
            intent.putExtra("DonHangID", donHangID);
            intent.putExtra("ShipperID", shipperID);
            startActivity(intent);
        }
    };

    CompoundButton.OnCheckedChangeListener deliveryCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked == true){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                cbDelivery.setEnabled(false);
                                btnTimShipper.setEnabled(true);
                                txtChonNguoiGiaoHang.setVisibility(View.VISIBLE);
                                edtShipperID.setVisibility(View.VISIBLE);
                                btnTimShipper.setVisibility(View.VISIBLE);
                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                                            DatHang orderUpdate = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                    snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                    snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(), snapshot.getValue(DatHang.class).getLoaiDonHang(), 3, snapshot.getValue(DatHang.class).getSellerCommission(), snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                            databaseReference.child("DonHang").child(donHangID).setValue(orderUpdate);
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
                                cbDelivery.setChecked(false);
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(XuLyDonHang_NguoiBanActivity.this);
                alert.setMessage("Gửi hàng cho đơn vị vận chuyển?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        }
    };

    CompoundButton.OnCheckedChangeListener packingCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked == true){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                cbPacking.setEnabled(false);
                                cbDelivery.setEnabled(true);
                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                                            DatHang orderUpdate = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                    snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                    snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(), snapshot.getValue(DatHang.class).getLoaiDonHang(), 2, snapshot.getValue(DatHang.class).getSellerCommission(), snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                            databaseReference.child("DonHang").child(donHangID).setValue(orderUpdate);
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
                                cbPacking.setChecked(false);
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(XuLyDonHang_NguoiBanActivity.this);
                alert.setMessage("Hoàn tất đóng gói sản phẩm?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        }
    };

    CompoundButton.OnCheckedChangeListener proccessingCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked == true){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                cbProccessing.setEnabled(false);
                                cbPacking.setEnabled(true);
                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                                            DatHang orderUpdate = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                    snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                    snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(), snapshot.getValue(DatHang.class).getLoaiDonHang(), 1, snapshot.getValue(DatHang.class).getSellerCommission(), snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                            databaseReference.child("DonHang").child(donHangID).setValue(orderUpdate);
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
                                cbProccessing.setChecked(false);
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(XuLyDonHang_NguoiBanActivity.this);
                alert.setMessage("Xác nhận đơn hàng thành công và tiến hành đóng gói?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        }
    };

    View.OnClickListener cancelOrderClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(loaiDonHang == 2){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                                            DatHang orderUpdate = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                    snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                    snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(), snapshot.getValue(DatHang.class).getLoaiDonHang(), -1, snapshot.getValue(DatHang.class).getSellerCommission(), snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                            databaseReference.child("LichSuGiaoDich").child(donHangID).setValue(orderUpdate);
                                            databaseReference.child("DonHang").child(donHangID).removeValue();

                                            if(!shipperID.isEmpty()){
                                                databaseReference.child("Shipper").child(shipperID).child(donHangID).removeValue();
                                            }


                                            finish();
                                            intent = new Intent(v.getContext(), DonBanActivity.class);
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

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };

                AlertDialog.Builder alert = new AlertDialog.Builder(XuLyDonHang_NguoiBanActivity.this);
                alert.setMessage("Bạn muốn hủy đơn hàng người mua?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
            else if(loaiDonHang == 3){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                                            DatHang orderUpdate = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                    snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                    snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(), snapshot.getValue(DatHang.class).getLoaiDonHang(), -1, snapshot.getValue(DatHang.class).getSellerCommission(), snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                            databaseReference.child("LichSuGiaoDich").child(donHangID).setValue(orderUpdate);
                                            databaseReference.child("DonHang").child(donHangID).removeValue();

                                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                                        long newBalance = snapshot.getValue(UserData.class).getMoney() - tongGiaTri;
                                                        databaseReference.child("User").child(snapshot.getKey()).child("lMoney").setValue(newBalance);
                                                    }
                                                    else if(snapshot.getValue(UserData.class).getUserID().equals(nguoiMuaID)){
                                                        long newBalance = snapshot.getValue(UserData.class).getMoney() + tongGiaTri;
                                                        databaseReference.child("User").child(snapshot.getKey()).child("lMoney").setValue(newBalance);
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

                                            if(!shipperID.isEmpty()){
                                                databaseReference.child("Shipper").child(shipperID).child(donHangID).removeValue();
                                            }

                                            Handler handler = new Handler();
                                            int delay = 1500;

                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                    intent = new Intent(v.getContext(), DonBanActivity.class);
                                                    intent.putExtra("UserName", userName);
                                                    intent.putExtra("UserID", userID);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                    startActivity(intent);
                                                }
                                            }, delay);
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

                AlertDialog.Builder alert = new AlertDialog.Builder(XuLyDonHang_NguoiBanActivity.this);
                alert.setMessage("Đây là đơn hàng đã thanh toán trước và tiền của bạn sẽ được hoàn lại cho người mua, bạn xác nhận hủy?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), DonBanActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
}