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

public class ChiTiet_ChoActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private EditText edtUserName, edtLienHe;
    private TextView txtTenSP, txtSoLuongSP, txtGiaSP, txtPhuongThucThanhToan, txtTongGiaTriDonHang, txtNameLabel;
    private Button btnAccept, btnBack;
    private Intent intent;
    private String userName, userID, donHangID, nguoiBanID, nguoiMuaID, productID;
    private int loaiDonHang, sellerCommission;
    private long tongGiaTri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.chitiet_chochuyenkhoan);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtLienHe = (EditText) findViewById(R.id.edtLienHe);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtPhuongThucThanhToan = (TextView) findViewById(R.id.txtPhuongThucThanhToan);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        txtNameLabel = (TextView) findViewById(R.id.txtNameLabel);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(backClick);
        btnAccept.setOnClickListener(xacNhanClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            donHangID = getIntent().getExtras().getString("DonHangID");

            databaseReference.child("MoneyIncome").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                        nguoiBanID = snapshot.getValue(DatHang.class).getNguoiBanID();
                        nguoiMuaID = snapshot.getValue(DatHang.class).getNguoiMuaID();
                        loaiDonHang = snapshot.getValue(DatHang.class).getLoaiDonHang();
                        tongGiaTri = snapshot.getValue(DatHang.class).getGiaTien();
                        productID = snapshot.getValue(DatHang.class).getSanPham().getiD();
                        sellerCommission = snapshot.getValue(DatHang.class).getSellerCommission();
                        storageReference.child(snapshot.getValue(DatHang.class).getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ChiTiet_ChoActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });


                        if(snapshot.getValue(DatHang.class).getSanPham().getTinhTrang() == 0){

                            txtTenSP.setText(snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - New");
                        }
                        else {
                            txtTenSP.setText(snapshot.getValue(DatHang.class).getSanPham().getTenSP() + " - 2nd");
                        }

                        txtSoLuongSP.setText("Số lượng: " + String.valueOf(snapshot.getValue(DatHang.class).getSanPham().getSoLuong()) + " x ");
                        txtGiaSP.setText(String.valueOf(snapshot.getValue(DatHang.class).getSanPham().getGiaTien()) + "vnđ");
                        txtTongGiaTriDonHang.setText(String.valueOf(snapshot.getValue(DatHang.class).getGiaTien()) + "vnđ");

                        String lienHe = snapshot.getValue(DatHang.class).getSoDienThoai();

                        if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 2){
                            if(snapshot.getValue(DatHang.class).getLoaiDonHang() == 2){
                                txtPhuongThucThanhToan.setText("Thanh toán COD!");

                            }
                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(UserData.class).getUserID().equals(nguoiMuaID)){
                                        edtUserName.setText(snapshot.getValue(UserData.class).getHoTen());
                                        edtLienHe.setText(lienHe);
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

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), User_Cho_ChuyenKhoanActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener xacNhanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            databaseReference.child("MoneyIncome").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID) && snapshot.getValue(DatHang.class).getTinhTrang() == -1) {
                        DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        databaseReference.child("MoneyIncome").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if (snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)) {
                                                    DatHang orderUpdate = new DatHang(donHangID, snapshot.getValue(DatHang.class).getNguoiMuaID(),
                                                            snapshot.getValue(DatHang.class).getNguoiBanID(), snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(),
                                                            snapshot.getValue(DatHang.class).getDiaChi(), snapshot.getValue(DatHang.class).getSanPham(),
                                                            snapshot.getValue(DatHang.class).getLoaiDonHang(), snapshot.getValue(DatHang.class).getTinhTrang(), snapshot.getValue(DatHang.class).getSellerCommission(),
                                                            snapshot.getValue(DatHang.class).getGiaTien(),snapshot.getValue(DatHang.class).getShipperID());

                                                    //databaseReference.child("Shipper").child(orderUpdate.getShipperID()).child(orderUpdate.getDonHangID()).removeValue();
                                                    databaseReference.child("LichSuGiaoDich").child(donHangID).setValue(orderUpdate);
                                                    databaseReference.child("CommentNeeds").child(donHangID).setValue(orderUpdate);
                                                    databaseReference.child("MoneyIncome").child(donHangID).removeValue();
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
                                        finish();
                                        intent = new Intent(v.getContext(), User_Cho_ChuyenKhoanActivity.class);
                                        intent.putExtra("UserName", userName);
                                        intent.putExtra("UserID", userID);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        return;
                                }
                            }
                        };
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setMessage("Bạn chắc chắn đã nhận lại hàng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
                    }
                    else if (snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID) && snapshot.getValue(DatHang.class).getTinhTrang() == 7) {
                        DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        databaseReference.child("MoneyIncome").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if (snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)) {
                                                    DatHang orderData = new DatHang(donHangID, snapshot.getValue(DatHang.class).getNguoiMuaID(), snapshot.getValue(DatHang.class).getNguoiBanID(),
                                                            snapshot.getValue(DatHang.class).getNgayTaoDonHang(), snapshot.getValue(DatHang.class).getSoDienThoai(), snapshot.getValue(DatHang.class).getDiaChi(),
                                                            snapshot.getValue(DatHang.class).getSanPham(), snapshot.getValue(DatHang.class).getLoaiDonHang(), 8, snapshot.getValue(DatHang.class).getSellerCommission(), snapshot.getValue(DatHang.class).getGiaTien(), snapshot.getValue(DatHang.class).getShipperID());
                                                    //databaseReference.child("Shipper").child(orderData.getShipperID()).child(orderData.getDonHangID()).removeValue();
                                                    databaseReference.child("LichSuGiaoDich").child(donHangID).setValue(orderData);
                                                    databaseReference.child("CommentNeeds").child(donHangID).setValue(orderData);
                                                    databaseReference.child("DonHang").child(donHangID).removeValue();
                                                    databaseReference.child("MoneyIncome").child(donHangID).removeValue();
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

                                        finish();
                                        intent = new Intent(v.getContext(), User_Cho_ChuyenKhoanActivity.class);
                                        intent.putExtra("UserName", userName);
                                        intent.putExtra("UserID", userID);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        return;
                                }
                            }
                        };
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        alert.setMessage("Bạn chắc chắn đã nhận được tiền?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
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
    };
}