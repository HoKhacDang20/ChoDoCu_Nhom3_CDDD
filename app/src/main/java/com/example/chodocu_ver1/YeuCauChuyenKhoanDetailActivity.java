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
import com.example.chodocu_ver1.data_models.UserData;
import com.example.chodocu_ver1.data_models.UserDepositData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class YeuCauChuyenKhoanDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgUser;
    private TextView txtUserName, txtFullName, txtRequest, txtSDT;
    private EditText edtSoTienCanChuyen;
    private Button btnAccept, btnDeny, btnBack;
    private String userName, idGiaoDich, userID, userFullName;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.yeu_cau_chuyen_khoan_detail_layout);

        imgUser = (ImageView) findViewById(R.id.imgUser);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtFullName = (TextView) findViewById(R.id.txtFullName);
        txtRequest = (TextView) findViewById(R.id.txtRequest);
        txtSDT = (TextView) findViewById(R.id.txtSDT);
        edtSoTienCanChuyen = (EditText) findViewById(R.id.edtSoTienCanChuyen);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnDeny = (Button) findViewById(R.id.btnDeny);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(backClick);
        btnAccept.setOnClickListener(acceptClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            idGiaoDich = getIntent().getExtras().getString("IDGiaoDich");
            userID = getIntent().getExtras().getString("UserID");
            databaseReference.child("UserDepositRequest").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserDepositData.class).getIdGiaoDich().equals(idGiaoDich)){
                        txtRequest.setText("Số tiền yêu cần chuyển: " + String.valueOf(snapshot.getValue(UserDepositData.class).getMoney()) + "vnđ");
                        edtSoTienCanChuyen.setText(String.valueOf(snapshot.getValue(UserDepositData.class).getMoney()));
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
                    if(snapshot.getValue(UserData.class).getUserID().equals(userID)){
                        storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(YeuCauChuyenKhoanDetailActivity.this).load(uri).into(imgUser);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtFullName.setText(snapshot.getValue(UserData.class).getHoTen());
                        userFullName = snapshot.getValue(UserData.class).getHoTen();
                        txtUserName.setText(snapshot.getValue(UserData.class).getUserName());
                        txtSDT.setText(snapshot.getValue(UserData.class).getSoDienThoai());
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

    View.OnClickListener acceptClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtSoTienCanChuyen.getText().toString().isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa nhập số tiền để chuyển!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                                databaseReference.child("UserDepositRequest").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        long money = Long.valueOf(edtSoTienCanChuyen.getText().toString());
                                        if(snapshot.getValue(UserDepositData.class).getIdGiaoDich().equals(idGiaoDich)){
                                            UserDepositData newDepositData = new UserDepositData(snapshot.getValue(UserDepositData.class).getIdGiaoDich(), snapshot.getValue(UserDepositData.class).getUserID(),
                                                    snapshot.getValue(UserDepositData.class).getMoney(), 1);
                                            databaseReference.child("UserDepositRequest").child(snapshot.getValue(UserDepositData.class).getIdGiaoDich()).setValue(newDepositData);
                                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                                                    if(snapshot.getValue(UserData.class).getsUserName().equals("admin")){
//                                                        long iMyMoney = snapshot.getValue(UserData.class).getlMoney() - money;
//                                                        UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
//                                                                snapshot.getValue(UserData.class).getsSdt(),snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(),
//                                                                snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(),snapshot.getValue(UserData.class).getsTaiKhoanNH(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
//                                                                snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
//                                                                snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(),iMyMoney);
//                                                        databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
//                                                    }
                                                    if(snapshot.getValue(UserData.class).getUserID().equals(userID)){
                                                        long iMyMoney = snapshot.getValue(UserData.class).getMoney() + money;
                                                        UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getUserName(),snapshot.getValue(UserData.class).getShopID(),snapshot.getValue(UserData.class).getHoTen(),
                                                                snapshot.getValue(UserData.class).getSoDienThoai(),snapshot.getValue(UserData.class).getGioiTinh(),snapshot.getValue(UserData.class).getDiaChi(),
                                                                snapshot.getValue(UserData.class).getPassword(),snapshot.getValue(UserData.class).getImage(),snapshot.getValue(UserData.class).getUserID(), snapshot.getValue(UserData.class).getNgayThamGia()
                                                                , snapshot.getValue(UserData.class).getSoCMND(),snapshot.getValue(UserData.class).getEmail(),snapshot.getValue(UserData.class).getCmndMatTruoc(),snapshot.getValue(UserData.class).getPermission(),
                                                                snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(), snapshot.getValue(UserData.class).getSoSPDaBan(),
                                                                snapshot.getValue(UserData.class).getDiemThanhVien(), snapshot.getValue(UserData.class).getReport(),iMyMoney);
                                                        databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
//                                                        databaseReference.child("UserDepositRequest").child(idGiaoDich).removeValue();
                                                        intent = new Intent(v.getContext(), YeuCauChuyenKhoanActivity.class);
                                                        intent.putExtra("UserName", userName);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                        finish();
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
                alert.setMessage("Bạn muốn chuyển " + edtSoTienCanChuyen.getText().toString() + "vnđ cho user: " + userFullName + "?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }

        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), YeuCauChuyenKhoanActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}