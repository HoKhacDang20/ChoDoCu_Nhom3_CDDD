package com.example.chodocu_nhom3_cddd;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.chodocu_nhom3_cddd.data_models.CuaHang;
import com.example.chodocu_nhom3_cddd.data_models.SanPhamReport;
import com.example.chodocu_nhom3_cddd.data_models.UserData;
import com.example.chodocu_nhom3_cddd.data_models.UserDepositData;
import com.example.chodocu_nhom3_cddd.data_models.UserReport;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Button btnLogout, btnQuanLyDanhMuc, btnLichSuDonHangShipper, btnAccountInfo, btnWallet, btnProductReport, btnUserReport, btnUserReported, btnUserDepositMoney, btnEmployeeManagement, btnPassWordChange, btnShopRegistration, btnCommission, btnAddEmployee, btnThongKe;
    private TextView txtAdminAccountName, txtNotify, txtUserDepositMoneyNotify, txtProductReprtedNotify, txtUserReportedNotify;
    private ImageView imgAccount;
    private Intent intent = DangNhapActivity.intent;
    private ArrayList<UserData> userDataArrayList;
    private ArrayList<CuaHang> shopDataArrayList;
    private ArrayList<UserDepositData> userDepositDataArrayList;
    private ArrayList<SanPhamReport> productReportArrayList;
    private ArrayList<UserReport> userReportArrayList;
    private String sUserName;
    private int per;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.admin_main_layout);

        txtAdminAccountName = (TextView) findViewById(R.id.txtAdminAccountName);
        txtNotify = (TextView) findViewById(R.id.txtNotify);
        txtUserDepositMoneyNotify = (TextView) findViewById(R.id.txtUserDepositMoneyNotify);
        imgAccount = (ImageView) findViewById(R.id.imgAccount);
        btnLichSuDonHangShipper = (Button) findViewById(R.id.btnLichSuDonHangShipper);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnPassWordChange = (Button) findViewById(R.id.btnPassWordChange);
        btnAccountInfo = (Button) findViewById(R.id.btnAccountInfo);
        btnWallet = (Button) findViewById(R.id.btnWallet);
        btnQuanLyDanhMuc = (Button) findViewById(R.id.btnQuanLyDanhMuc);
        btnShopRegistration = (Button) findViewById(R.id.btnShopRegistration);
        btnUserDepositMoney = (Button) findViewById(R.id.btnUserDepositMoney);
        btnCommission = (Button) findViewById(R.id.btnCommission);
        txtProductReprtedNotify = (TextView) findViewById(R.id.txtProductReprtedNotify);
        btnProductReport = (Button) findViewById(R.id.btnProductReport);
        txtUserReportedNotify = (TextView) findViewById(R.id.txtUserReportedNotify);
        btnUserReport = (Button) findViewById(R.id.btnUserReport);
        btnUserReported = (Button) findViewById(R.id.btnUserReported);
        btnAddEmployee = (Button) findViewById(R.id.btnAddEmployee);
        btnEmployeeManagement = (Button) findViewById(R.id.btnEmployeeManagement);
        btnThongKe = (Button) findViewById(R.id.btnThongKe);

        userDataArrayList = new ArrayList<>();
        shopDataArrayList = new ArrayList<>();
        userDepositDataArrayList = new ArrayList<>();
        productReportArrayList = new ArrayList<>();
        userReportArrayList = new ArrayList<>();

//        btnLogout.setOnClickListener(logoutClick);
//        btnAccountInfo.setOnClickListener(accountInfoClick);
//        btnPassWordChange.setOnClickListener(passwordChangeClick);
//        btnQuanLyDanhMuc.setOnClickListener(danhMucClick);
//        btnShopRegistration.setOnClickListener(shopRegistrationClick);
//        btnUserDepositMoney.setOnClickListener(depositMoneyClick);
//        btnCommission.setOnClickListener(commissionClick);
//        btnProductReport.setOnClickListener(reportSPClick);
//        btnUserReport.setOnClickListener(reportUserClick);
//        btnUserReported.setOnClickListener(dsUserClick);
//        btnAddEmployee.setOnClickListener(addEmployeeClick);
//        btnEmployeeManagement.setOnClickListener(employeeManagerClick);
//        btnThongKe.setOnClickListener(thongKeClick);
//        btnWallet.setOnClickListener(walletClick);
//        btnLichSuDonHangShipper.setOnClickListener(lichSuDonGiaoShipperClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDepositDataArrayList.clear();
        shopDataArrayList.clear();
        productReportArrayList.clear();
        userReportArrayList.clear();

        databaseReference.child("Report").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                userReportArrayList.add(snapshot.getValue(UserReport.class));
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

        databaseReference.child("ProductReport").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                productReportArrayList.add(snapshot.getValue(SanPhamReport.class));
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

        databaseReference.child("ShopRegistration").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                shopDataArrayList.add(snapshot.getValue(CuaHang.class));
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
        databaseReference.child("UserDepositRequest").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserDepositData.class).getTinhTrang() == 0){
                    userDepositDataArrayList.add(snapshot.getValue(UserDepositData.class));
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

//            txtAdminAccountName.setText(String.valueOf(userDataArrayList.size()));

//            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
////                    if(snapshot.getValue(UserData.class).getUserID().equals(UserMainActivity.sUserID)){
////                        per = snapshot.getValue(UserData.class).getPermission();
////                        if(snapshot.getValue(UserData.class).getPermission() == 2){
////                            btnAddEmployee.setVisibility(View.GONE);
////                            btnEmployeeManagement.setVisibility(View.GONE);
////                            btnCommission.setVisibility(View.GONE);
////                            btnQuanLyDanhMuc.setVisibility(View.GONE);
////                            btnWallet.setVisibility(View.GONE);
////                            btnUserDepositMoney.setVisibility(View.GONE);
////                            btnThongKe.setVisibility(View.GONE);
////                            txtAdminAccountName.setText("Employee - " + snapshot.getValue(UserData.class).getHoTen());
////                            imageLoad(snapshot.getValue(UserData.class).getImage());
////                        }
////                        else{
////                            txtAdminAccountName.setText("Admin - " + snapshot.getValue(UserData.class).getHoTen());
////                            imageLoad(snapshot.getValue(UserData.class).getImage());
////                        }
//                        btnWallet.setText("Ví tài khoản: " + String.valueOf(snapshot.getValue(UserData.class).getMoney()) + "vnđ");
//                        if(!snapshot.getValue(UserData.class).getImage().isEmpty()){
//                            imageLoad(snapshot.getValue(UserData.class).getImage());
//                        }
//                    }
//                }
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }



        Handler handler = new Handler();
        int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(shopDataArrayList.size() != 0){
                    txtNotify.setVisibility(View.VISIBLE);
                    txtNotify.setText(String.valueOf(shopDataArrayList.size()));
                }
                if(userDepositDataArrayList.size() != 0 && per == 0){
                    txtUserDepositMoneyNotify.setVisibility(View.VISIBLE);
                    txtUserDepositMoneyNotify.setText(String.valueOf(userDepositDataArrayList.size()));
                }
                if(productReportArrayList.size() != 0){
                    txtProductReprtedNotify.setVisibility(View.VISIBLE);
                    txtProductReprtedNotify.setText(String.valueOf(productReportArrayList.size()));
                }
                if(userReportArrayList.size() != 0){
                    txtUserReportedNotify.setVisibility(View.VISIBLE);
                    txtUserReportedNotify.setText(String.valueOf(userReportArrayList.size()));
                }
            }
        }, delay);
    }

//    View.OnClickListener lichSuDonGiaoShipperClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), ShipperLichSuDonGiaoActivity.class);
//            intent.putExtra("UserName", sUserName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener walletClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), ViDienTuActivity.class);
//            intent.putExtra("UserName", sUserName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener thongKeClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), ThongKeActivity.class);
//            intent.putExtra("UserName", sUserName);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener employeeManagerClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), NhanVien_AdminActivity.class);
//            intent.putExtra("UserName", sUserName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener addEmployeeClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), ThemNhanVienActivity.class);
//            intent.putExtra("UserName", sUserName);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener dsUserClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), DanhSachUserActivity.class);
//            intent.putExtra("UserName", sUserName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener reportUserClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), Report_User_Admin_Activity.class);
//            intent.putExtra("UserName", sUserName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener reportSPClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), SP_Report_admin_Activity.class);
//            intent.putExtra("UserName", sUserName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener commissionClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), HoaHongActivity.class);
//            intent.putExtra("UserName", sUserName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener depositMoneyClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), YeuCauChuyenKhoanActivity.class);
//            intent.putExtra("UserName", sUserName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener shopRegistrationClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), YeuCau_DangKy_CuaHangActivity.class);
//            intent.putExtra("UserName", sUserName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener danhMucClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), QuanLyDanhMucActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            intent.putExtra("UserName", sUserName);
//            startActivity(intent);
//
//        }
//    };

    public void imageLoad(String sImageName){
        storageReference.child(sImageName + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(AdminMainActivity.this).load(uri).into(imgAccount);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(AdminMainActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    View.OnClickListener passwordChangeClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), DoiMatKhauActivity.class);
//            intent.putExtra("UserName", sUserName);
//            startActivity(intent);
//        }
//    };
//
//    View.OnClickListener accountInfoClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            intent = new Intent(v.getContext(), ThongTinTaiKhoanActivity.class);
//            intent.putExtra("UserName", sUserName);
//            startActivity(intent);
//        }
//    };

    View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            finish();
                            //intent = new Intent(v.getContext(), DangNhapActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
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
}