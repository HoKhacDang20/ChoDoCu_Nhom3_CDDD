package com.example.chodocu_ver1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.data_models.DatHang;
import com.example.chodocu_ver1.data_models.SanPham;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MuaActivity extends AppCompatActivity {
    private FirebaseUser user = UserMainActivity.user;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private TextView txtTenSP, txtSoLuongSP, txtGiaSP, txtSellerUserName, txtNgayThamGia, txtDiaChi, txtLienHe, txtTongGiaTriDonHang;
    private ImageView imgSP, imgSellerUser;
    private RadioButton rdbThanhToanTrucTiep, rdbCOD, rdbEWallet;
    private Button btnCheckOut, btnBack, btnHome;
    private String userID, nguoiBanID, sanPhamID, userName, navigateTo;
    private int soLuongSP;
    private long tongGiaTri;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.muahang_layout);

        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtSellerUserName = (TextView) findViewById(R.id.txtSellerUserName);
        txtNgayThamGia = (TextView) findViewById(R.id.txtNgayThamGia);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        txtLienHe = (TextView) findViewById(R.id.txtLienHe);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        imgSP = (ImageView) findViewById(R.id.imgSP);
        imgSellerUser = (ImageView) findViewById(R.id.imgSellerUser);
        rdbThanhToanTrucTiep = (RadioButton) findViewById(R.id.rdbThanhToanTrucTiep);
        rdbCOD = (RadioButton) findViewById(R.id.rdbCOD);
        rdbEWallet = (RadioButton) findViewById(R.id.rdbEWallet);
        btnCheckOut = (Button) findViewById(R.id.btnCheckOut);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnHome = (Button) findViewById(R.id.btnHome);

        btnBack.setOnClickListener(backClick);
        btnCheckOut.setOnClickListener(checkOutClick);
        btnHome.setOnClickListener(homeClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getExtras() != null){
            navigateTo = getIntent().getExtras().getString("NavigateTo");
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            nguoiBanID = getIntent().getExtras().getString("SellerID");
            sanPhamID = getIntent().getExtras().getString("SanPhamID");
            soLuongSP = Integer.valueOf(getIntent().getExtras().getString("SoLuongSP"));
            txtSoLuongSP.setText("Số lượng: " + getIntent().getExtras().getString("SoLuongSP") + " x ");

            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getiD().equals(sanPhamID)){
                        tongGiaTri = snapshot.getValue(SanPham.class).getGiaTien() * soLuongSP;
                        storageReference.child(snapshot.getValue(SanPham.class).getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(MuaActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtGiaSP.setText(Long.valueOf(snapshot.getValue(SanPham.class).getGiaTien()) + "vnđ");
                        txtTongGiaTriDonHang.setText("Tổng tiền: " + String.valueOf(tongGiaTri) + "vnđ");
                        txtTenSP.setText(snapshot.getValue(SanPham.class).getTenSP());

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
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                        storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(MuaActivity.this).load(uri).into(imgSellerUser);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        txtDiaChi.setText("Địa chỉ: " + snapshot.getValue(UserData.class).getDiaChi());
                        txtLienHe.setText("Liên hệ: " + snapshot.getValue(UserData.class).getSoDienThoai());
                        txtSellerUserName.setText(snapshot.getValue(UserData.class).getHoTen());
                        txtNgayThamGia.setText("Ngày tham gia: " + snapshot.getValue(UserData.class).getNgayThamGia());
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

    View.OnClickListener homeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserMainActivity.class);
            UserMainActivity.user = user;
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    View.OnClickListener checkOutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!rdbCOD.isChecked() && !rdbEWallet.isChecked() && !rdbThanhToanTrucTiep.isChecked()){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn phương thức thanh toán!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(rdbThanhToanTrucTiep.isChecked()){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(SanPham.class).getiD().equals(sanPhamID)){
                                            int newSL = snapshot.getValue(SanPham.class).getSoLuong() - soLuongSP;
                                            SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getiD(), snapshot.getValue(SanPham.class).getUserID(),
                                                    snapshot.getValue(SanPham.class).getShopID(), snapshot.getValue(SanPham.class).getTenSP(),snapshot.getValue(SanPham.class).getSpImage(),
                                                    snapshot.getValue(SanPham.class).getMoTa(),snapshot.getValue(SanPham.class).getDanhMuc(), snapshot.getValue(SanPham.class).getNgayDang(),
                                                    snapshot.getValue(SanPham.class).getDiaChiDang(), snapshot.getValue(SanPham.class).getGiaTien(),newSL,snapshot.getValue(SanPham.class).getTinhTrang());
                                            databaseReference.child("SanPham").child(snapshot.getKey()).setValue(sanPham);
                                            SanPham sanPhamOrder = new SanPham(snapshot.getValue(SanPham.class).getiD(), snapshot.getValue(SanPham.class).getUserID(),
                                                    snapshot.getValue(SanPham.class).getShopID(), snapshot.getValue(SanPham.class).getTenSP(),snapshot.getValue(SanPham.class).getSpImage(),
                                                    snapshot.getValue(SanPham.class).getMoTa(),snapshot.getValue(SanPham.class).getDanhMuc(), snapshot.getValue(SanPham.class).getNgayDang(),
                                                    snapshot.getValue(SanPham.class).getDiaChiDang(), snapshot.getValue(SanPham.class).getGiaTien(),soLuongSP,snapshot.getValue(SanPham.class).getTinhTrang());
                                            String donHangID = databaseReference.push().getKey();
                                            long tongGiaTriDonHang = snapshot.getValue(SanPham.class).getGiaTien() * soLuongSP;
                                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                            Date date = new Date();
                                            DatHang orderData = new DatHang(donHangID, UserMainActivity.user.getUid(), nguoiBanID, dateFormat.format(date), "", "", sanPhamOrder, 1, 0, 0, tongGiaTriDonHang,"");
                                            databaseReference.child("DonHang").child(donHangID).setValue(orderData);
                                            finish();
                                            intent = new Intent(v.getContext(), UserMainActivity.class);
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
                alert.setMessage("Bạn muốn thanh toán trực tiếp cho đơn hàng!").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
            else if(rdbCOD.isChecked()){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                intent = new Intent(v.getContext(), ThanhToanActivity.class);
                                intent.putExtra("KieuThanhToan", 2);
                                intent.putExtra("UserName", userName);
                                intent.putExtra("UserID", userID);
                                intent.putExtra("NguoiBanID", nguoiBanID);
                                intent.putExtra("SanPhamID", sanPhamID);
                                intent.putExtra("SoLuongSP", soLuongSP);
                                intent.putExtra("TongGiaTri", tongGiaTri);
                                intent.putExtra("NavigateTo", navigateTo);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chọn thanh toán COD!").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
            else if(rdbEWallet.isChecked()){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                intent = new Intent(v.getContext(), ThanhToanActivity.class);
                                intent.putExtra("KieuThanhToan", 3);
                                intent.putExtra("UserName", userName);
                                intent.putExtra("UserID", userID);
                                intent.putExtra("NguoiBanID", nguoiBanID);
                                intent.putExtra("SanPhamID", sanPhamID);
                                intent.putExtra("SoLuongSP", soLuongSP);
                                intent.putExtra("TongGiaTri", tongGiaTri);
                                intent.putExtra("NavigateTo", navigateTo);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chọn thanh toán trước qua ví điện tử!").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            /*intent = new Intent(v.getContext(), ChiTiet_SanPhamActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("ProductID", sanPhamID);
            intent.putExtra("NavigateTo", navigateTo);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);*/
        }
    };
}
