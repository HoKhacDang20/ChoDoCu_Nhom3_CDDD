package com.example.chodocu_ver1;

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

public class User_XacNhan_DatHangActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private EditText edtUserName, edtDiaChi, edtLienHe;
    private TextView txtTenSP, txtMaVanDon, txtSoLuongSP, txtGiaSP, txtPhuongThucThanhToan, txtTongGiaTriDonHang, txtNameLabel, txtDiaChi;
    private Button btnHuyDon, btnBack;
    private Intent intent;
    private String userName, userID, donHangID, nguoiBanID, nguoiMuaID, productID;
    private int loaiDonHang, sellerCommission;
    private long tongGiaTri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_xacnhan_muahang_layout);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtLienHe = (EditText) findViewById(R.id.edtLienHe);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtMaVanDon = (TextView) findViewById(R.id.txtMaVanDon);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtPhuongThucThanhToan = (TextView) findViewById(R.id.txtPhuongThucThanhToan);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        txtNameLabel = (TextView) findViewById(R.id.txtNameLabel);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        btnHuyDon = (Button) findViewById(R.id.btnHuyDon);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(backClick);
        btnHuyDon.setOnClickListener(huyDonClick);
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
                        nguoiBanID = snapshot.getValue(DatHang.class).getNguoiBanID();
                        nguoiMuaID = snapshot.getValue(DatHang.class).getNguoiMuaID();
                        loaiDonHang = snapshot.getValue(DatHang.class).getLoaiDonHang();
                        tongGiaTri = snapshot.getValue(DatHang.class).getGiaTien();
                        productID = snapshot.getValue(DatHang.class).getSanPham().getiD();
                        storageReference.child(snapshot.getValue(DatHang.class).getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(User_XacNhan_DatHangActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtMaVanDon.setText("Mã vận đơn: " + snapshot.getValue(DatHang.class).getDonHangID());
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

                        if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 1){
                            txtPhuongThucThanhToan.setText("Thanh toán trực tiếp!");

                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                        sellerCommission = snapshot.getValue(UserData.class).getHoaHong();
                                        edtUserName.setText(snapshot.getValue(UserData.class).getHoTen());
                                        edtDiaChi.setText(snapshot.getValue(UserData.class).getDiaChi());
                                        edtLienHe.setText(snapshot.getValue(UserData.class).getSoDienThoai());
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
        }
    }

//    View.OnClickListener acceptClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    switch (which){
//                        case DialogInterface.BUTTON_POSITIVE:
//                            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
//                                @Override
//                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                                    if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID)){
//                                        OrderData orderData = new OrderData(snapshot.getValue(OrderData.class).getDonHangID(), snapshot.getValue(OrderData.class).getNguoiMuaID(), snapshot.getValue(OrderData.class).getNguoiBanID(),
//                                                snapshot.getValue(OrderData.class).getNgayTaoDonHang(), snapshot.getValue(OrderData.class).getSoDienThoai(), snapshot.getValue(OrderData.class).getDiaChi(),
//                                                snapshot.getValue(OrderData.class).getSanPham(), snapshot.getValue(OrderData.class).getLoaiDonHang(), 8, sellerCommission, snapshot.getValue(OrderData.class).getGiaTien(), snapshot.getValue(OrderData.class).getShipperID());
//                                        databaseReference.child("LichSuGiaoDich").child(snapshot.getValue(OrderData.class).getDonHangID()).setValue(orderData);
//                                        databaseReference.child("DonHang").child(snapshot.getValue(OrderData.class).getDonHangID()).removeValue();
//                                        intent = new Intent(v.getContext(), CommentActivity.class);
//                                        intent.putExtra("ProductID", productID);
//                                        intent.putExtra("UserID", userID);
//                                        intent.putExtra("SellerID", nguoiBanID);
//                                        intent.putExtra("UserName", userName);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                        startActivity(intent);
//                                    }
//                                }
//
//                                @Override
//                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                                }
//
//                                @Override
//                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                                }
//
//                                @Override
//                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//                            break;
//                        case DialogInterface.BUTTON_NEGATIVE:
//                            return;
//                    }
//                }
//            };
//            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
//            alert.setMessage("Bạn đồng ý nhận hàng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
//        }
//    };

    View.OnClickListener huyDonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                                        DatHang orderData = new DatHang(snapshot.getValue(DatHang.class).getDonHangID(), snapshot.getValue(DatHang.class).getNguoiMuaID(), snapshot.getValue(DatHang.class).getNguoiBanID(),
                                                snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(), snapshot.getValue(DatHang.class).getDiaChi(),
                                                snapshot.getValue(DatHang.class).getSanPham(), snapshot.getValue(DatHang.class).getLoaiDonHang(), -1, sellerCommission, snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                        databaseReference.child("LichSuGiaoDich").child(snapshot.getValue(DatHang.class).getDonHangID()).setValue(orderData);
                                        databaseReference.child("DonHang").child(snapshot.getValue(DatHang.class).getDonHangID()).removeValue();
                                        intent = new Intent(v.getContext(), DonMuaActivity.class);
//                                        intent.putExtra("ProductID", productID);
                                        intent.putExtra("UserID", userID);
//                                        intent.putExtra("SellerID", nguoiBanID);
                                        intent.putExtra("UserName", userName);
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
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
            alert.setMessage("Bạn muốn hủy đơn hàng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
//            intent = new Intent(v.getContext(), DonMuaActivity.class);
//            intent.putExtra("UserName", userName);
//            intent.putExtra("UserID", userID);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
        }
    };
}