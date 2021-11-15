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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.data_models.DanhGia;
import com.example.chodocu_ver1.data_models.DatHang;
import com.example.chodocu_ver1.data_models.SanPham;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DanhGiaActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private TextView txtTenSP, txtGiaSP;
    private Spinner spnStarLevel;
    private Button btnPostComment, btnHome, btnReport, btnBack;
    private EditText edtComment;
    private String productID, userID, nguoiBanID, userName, navigateTo = "", donHangID = "";
    private int diemThanhVien;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.danhgia_layout);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        spnStarLevel = (Spinner) findViewById(R.id.spnStarLevel);
        btnPostComment = (Button) findViewById(R.id.btnPostComment);
        btnHome = (Button) findViewById(R.id.btnHome);
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnReport = (Button) findViewById(R.id.btnReport);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnHome.setOnClickListener(homeClick);
        btnPostComment.setOnClickListener(postCommentClick);
        btnReport.setOnClickListener(reportClick);
        btnBack.setOnClickListener(backClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            productID = getIntent().getExtras().getString("ProductID");
            userID = getIntent().getExtras().getString("UserID");
            nguoiBanID = getIntent().getExtras().getString("IdNguoiBan");
            userName = getIntent().getExtras().getString("UserName");
            navigateTo = getIntent().getExtras().getString("Navigate");
            donHangID = getIntent().getExtras().getString("DonHangID");

            if(!navigateTo.isEmpty()){
                btnBack.setVisibility(View.VISIBLE);
            }

            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getiD().equals(productID)){
                        storageReference.child(snapshot.getValue(SanPham.class).getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(DanhGiaActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                        if(snapshot.getValue(SanPham.class).getTinhTrang() == 0){
                            txtTenSP.setText(snapshot.getValue(SanPham.class).getTenSP() + " - New");
                        }
                        else {
                            txtTenSP.setText(snapshot.getValue(SanPham.class).getTenSP() + " - 2nd");
                        }

                        txtGiaSP.setText(String.valueOf(snapshot.getValue(SanPham.class).getGiaTien()) + "vnđ");
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
            intent = new Intent(DanhGiaActivity.this, DanhGia_SanPhamActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener reportClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            intent = new Intent(DanhGiaActivity.this, Report_SP_Activity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("IdSanPham", productID);
            intent.putExtra("UserID", userID);
            intent.putExtra("Navigate", navigateTo);
            intent.putExtra("IdDonHang", donHangID);
            intent.putExtra("IdNguoiBan", nguoiBanID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener postCommentClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            if (productID != null) {
//                Toast.makeText(v.getContext(), productID, Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(v.getContext(), "productID bị null :v", Toast.LENGTH_SHORT).show();
//            }
            if(edtComment.getText().toString().isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa nhập đánh giá sản phẩm!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtComment.getText().toString().length() < 10){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Đánh giá sản phẩm quá ngắn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else{

                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                alert.setMessage("Cám ơn bài đánh giá của bạn, điểm sẽ được cộng vào tài khoản của bạn trong giây lát!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int soSao = spnStarLevel.getSelectedItemPosition() + 1;
                                        diemThanhVien = 0;
                                        if(soSao == 3){
                                            diemThanhVien = 3;
                                        }
                                        else if(soSao == 4){
                                            diemThanhVien = 4;
                                        }
                                        else if(soSao == 5){
                                            diemThanhVien = 5;
                                        }
                                        else if(soSao == 2){
                                            diemThanhVien = -2;
                                        }
                                        else if(soSao == 1){
                                            diemThanhVien = -4;
                                        }
                                        String cmtID = databaseReference.push().getKey();
                                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        Date date = new Date();
                                        DanhGia comment = new DanhGia(cmtID, productID, userID, edtComment.getText().toString(), dateFormat.format(date), soSao);
                                        //DanhGia comment = new DanhGia(cmtID,);
                                        databaseReference.child("ProductReview").child(cmtID).setValue(comment);
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getUserID().equals(userID)){
                                                    int newPoint = snapshot.getValue(UserData.class).getDiemThanhVien() + 1;
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getUserName(),snapshot.getValue(UserData.class).getShopID(),snapshot.getValue(UserData.class).getHoTen(),snapshot.getValue(UserData.class).getSoDienThoai(),
                                                            snapshot.getValue(UserData.class).getGioiTinh(),snapshot.getValue(UserData.class).getDiaChi(), snapshot.getValue(UserData.class).getPassword(),snapshot.getValue(UserData.class).getImage(),snapshot.getValue(UserData.class).getUserID(),
                                                            snapshot.getValue(UserData.class).getNgayThamGia(),snapshot.getValue(UserData.class).getSoCMND(),snapshot.getValue(UserData.class).getEmail(),snapshot.getValue(UserData.class).getPermission(),
                                                            snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(), snapshot.getValue(UserData.class).getSoSPDaBan(),
                                                            newPoint,snapshot.getValue(UserData.class).getReport(),snapshot.getValue(UserData.class).getMoney());
                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);

                                                }
                                                else if(snapshot.getValue(UserData.class).getUserID().equals(nguoiBanID)){
                                                    int newPoint = snapshot.getValue(UserData.class).getDiemThanhVien() + diemThanhVien;
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getUserName(),snapshot.getValue(UserData.class).getShopID(),snapshot.getValue(UserData.class).getHoTen(),snapshot.getValue(UserData.class).getSoDienThoai(),
                                                            snapshot.getValue(UserData.class).getGioiTinh(),snapshot.getValue(UserData.class).getDiaChi(), snapshot.getValue(UserData.class).getPassword(),snapshot.getValue(UserData.class).getImage(),snapshot.getValue(UserData.class).getUserID(),
                                                            snapshot.getValue(UserData.class).getNgayThamGia()
                                                            ,snapshot.getValue(UserData.class).getSoCMND(),snapshot.getValue(UserData.class).getEmail(),snapshot.getValue(UserData.class).getPermission(),
                                                            snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(), snapshot.getValue(UserData.class).getSoSPDaBan(),
                                                            newPoint,snapshot.getValue(UserData.class).getReport(),snapshot.getValue(UserData.class).getMoney());
                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
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

                                        if(!navigateTo.isEmpty()){
                                            databaseReference.child("DanhGia").addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    if(snapshot.getValue(DatHang.class).getDonHangID().equals(donHangID)){
                                                        databaseReference.child("DanhGia").child(snapshot.getKey()).removeValue();
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

                                        finish();
                                        intent = new Intent(v.getContext(), UserMainActivity.class);
                                        intent.putExtra("UserName", userName);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                    }
                                }).show();

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Gửi đánh giá sản phẩm?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();

            }
        }
    };

    View.OnClickListener homeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("CommentNeeds").child(donHangID).removeValue();
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
            alert.setMessage("Bạn muốn thoát đánh giá sản phẩm, bạn sẽ không được cộng điểm?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
        }
    };
}