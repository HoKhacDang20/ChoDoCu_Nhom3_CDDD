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
import com.example.chodocu_nhom3_cddd.data_models.SanPham;
import com.example.chodocu_nhom3_cddd.data_models.SanPhamReport;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Report_SP_Activity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Button btnReportSP, btnBack;
    private ImageView imgSP;
    private EditText edtReport;
    private TextView txtTenSP, txtMoney;
    private Intent intent;
    private String sanPhamID, nguoiBanID;
    private String userName;
    private String userID = UserMainActivity.sUserID;
    private String navigate, donHangID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.report_sanpham);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnReportSP = (Button) findViewById(R.id.btnReportSP);
        imgSP = (ImageView) findViewById(R.id.imgSP);
        edtReport = (EditText) findViewById(R.id.edtReport);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtMoney = (TextView) findViewById(R.id.txtMoney);

        //btnBack.setOnClickListener(backClick);
        btnReportSP.setOnClickListener(reportClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("UserName");
            sanPhamID = getIntent().getExtras().getString("ProductID");
            userID = getIntent().getExtras().getString("UserID");
            navigate = getIntent().getExtras().getString("Navigate");
            donHangID = getIntent().getExtras().getString("DonHangID");
            nguoiBanID = getIntent().getExtras().getString("NguoiBanID");

            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(SanPham.class).getiD().equals(sanPhamID)) {
                        storageReference.child(snapshot.getValue(SanPham.class).getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(Report_SP_Activity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                        txtMoney.setText(Long.valueOf(snapshot.getValue(SanPham.class).getGiaTien()) + "vnđ");
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
        }
    }

    View.OnClickListener reportClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (edtReport.getText().toString().isEmpty()) {
                edtReport.setError("Không được để trống!");
            } else {
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if (snapshot.getValue(SanPham.class).getiD().equals(sanPhamID)) {
                                            SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getiD(), snapshot.getValue(SanPham.class).getUserID(),
                                                    snapshot.getValue(SanPham.class).getShopID(), snapshot.getValue(SanPham.class).getTenSP(), snapshot.getValue(SanPham.class).getSpImage(),
                                                    snapshot.getValue(SanPham.class).getMoTa(), snapshot.getValue(SanPham.class).getDanhMuc(), snapshot.getValue(SanPham.class).getNgayDang(),
                                                    snapshot.getValue(SanPham.class).getDiaChiDang(), snapshot.getValue(SanPham.class).getGiaTien(), snapshot.getValue(SanPham.class).getSoLuong(), snapshot.getValue(SanPham.class).getTinhTrang());
                                            String reportID = databaseReference.push().getKey();
                                            SanPhamReport productReport = new SanPhamReport(reportID, userID, edtReport.getText().toString(), sanPham);
                                            databaseReference.child("CommentNeeds").child(donHangID).removeValue();
                                            databaseReference.child("ProductReport").child(reportID).setValue(productReport);
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
                                intent = new Intent(v.getContext(), UserMainActivity.class);
                                intent.putExtra("UserName", userName);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chắn chắn muốn báo cáo sản phẩm!").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }

        }
    };

    /*.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), DanhGiaActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("ProductID", sanPhamID);
            intent.putExtra("Navigate", navigate);
            intent.putExtra("UserID", userID);
            intent.putExtra("SellerID", nguoiBanID);
            intent.putExtra("DonHangID", donHangID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };*/
}