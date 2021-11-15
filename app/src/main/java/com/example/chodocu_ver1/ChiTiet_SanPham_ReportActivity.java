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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.data_models.DanhGia;
import com.example.chodocu_ver1.data_models.RemoveSanPham;
import com.example.chodocu_ver1.data_models.SanPham;
import com.example.chodocu_ver1.data_models.SanPhamReport;
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

public class ChiTiet_SanPham_ReportActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private TextView txtTenSP, txtDiaChiDang, txtGiaSP, txtSoLuongSP, txtMoTaSP, txtLyDoReport;
    private Button btnDelete, btnBack, btnHome, btnHuy;
    private Intent intent;
    private String userName, reportID, sanphamID, moTa, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.chitiet_sanpham__report);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtDiaChiDang = (TextView) findViewById(R.id.txtDiaChiDang);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtMoTaSP = (TextView) findViewById(R.id.txtMoTaSP);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnHome = (Button) findViewById(R.id.btnHome);
        txtLyDoReport = (TextView) findViewById(R.id.txtLyDoReport);
        btnHuy = (Button) findViewById(R.id.btnHuy);

        btnBack.setOnClickListener(backClick);
        btnHome.setOnClickListener(homeClick);
        btnHuy.setOnClickListener(huyClick);
        btnDelete.setOnClickListener(xoaClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            reportID = getIntent().getExtras().getString("ReportID");
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
            databaseReference.child("ProductReport").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(SanPhamReport.class).getReportID().equals(reportID)) {
                        sanphamID = snapshot.getValue(SanPhamReport.class).getSanPham().getiD();
                        storageReference.child(snapshot.getValue(SanPhamReport.class).getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ChiTiet_SanPham_ReportActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ChiTiet_SanPham_ReportActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        txtTenSP.setText(snapshot.getValue(SanPhamReport.class).getSanPham().getTenSP());
                        txtDiaChiDang.setText(snapshot.getValue(SanPhamReport.class).getSanPham().getDiaChiDang());
                        txtGiaSP.setText(String.valueOf(snapshot.getValue(SanPhamReport.class).getSanPham().getGiaTien()));
                        txtSoLuongSP.setText(String.valueOf(snapshot.getValue(SanPhamReport.class).getSanPham().getSoLuong()));
                        txtMoTaSP.setText(snapshot.getValue(SanPhamReport.class).getSanPham().getMoTa());
                        txtLyDoReport.setText(snapshot.getValue(SanPhamReport.class).getLyDoBaoCao());

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

    View.OnClickListener huyClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("ProductReport").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.getValue(SanPhamReport.class).getSanPham().getiD().equals(sanphamID)) {
                                        databaseReference.child("ProductReport").child(snapshot.getKey()).removeValue();
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
                            intent = new Intent(v.getContext(), SP_Report_admin_Activity.class);
                            intent.putExtra("UserName", userName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            finish();
                            startActivity(intent);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Bạn muốn hủy yêu cầu báo cáo?").setNegativeButton("No", dialogClick).setPositiveButton("Yes", dialogClick).show();
        }
    };

    View.OnClickListener xoaClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("ProductReport").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.getValue(SanPhamReport.class).getSanPham().getiD().equals(sanphamID)) {
                                        databaseReference.child("ProductReport").child(snapshot.getKey()).removeValue();
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
                            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.getValue(SanPham.class).getiD().equals(sanphamID)) {
                                        String id = databaseReference.push().getKey();
                                        SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getiD(), snapshot.getValue(SanPham.class).getUserID(), snapshot.getValue(SanPham.class).getShopID(), snapshot.getValue(SanPham.class).getTenSP()
                                                , snapshot.getValue(SanPham.class).getSpImage(), snapshot.getValue(SanPham.class).getMoTa(), snapshot.getValue(SanPham.class).getDanhMuc(), snapshot.getValue(SanPham.class).getNgayDang(), snapshot.getValue(SanPham.class).getDiaChiDang()
                                                , snapshot.getValue(SanPham.class).getGiaTien(), snapshot.getValue(SanPham.class).getSoLuong(), snapshot.getValue(SanPham.class).getTinhTrang());
                                        RemoveSanPham removeProductData = new RemoveSanPham(id, sanPham);
                                        databaseReference.child("DeletedProduct").child(id).setValue(removeProductData);
                                        databaseReference.child("SanPham").child(sanphamID).removeValue();
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
                            databaseReference.child("ProductReview").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.getValue(DanhGia.class).getSanPhamID().equals(sanphamID)) {
                                        databaseReference.child("ProductReview").child(snapshot.getKey()).removeValue();
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
                                    if (snapshot.getValue(UserData.class).getUserID().equals(userID)) {
                                        int report = snapshot.getValue(UserData.class).getReport() + 1;
                                        databaseReference.child("User").child(snapshot.getKey()).child("iReport").setValue(report);
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

                            intent = new Intent(v.getContext(), SP_Report_admin_Activity.class);
                            intent.putExtra("UserName", userName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            finish();
                            startActivity(intent);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Bạn chắc chắn muốn xóa sản phẩm?").setNegativeButton("No", dialogClick).setPositiveButton("Yes", dialogClick).show();
        }
    };

    View.OnClickListener homeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), AdminMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            intent = new Intent(v.getContext(), SP_Report_admin_Activity.class);
//            intent.putExtra("UserName", userName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
//            startActivity(intent);
        }
    };
}