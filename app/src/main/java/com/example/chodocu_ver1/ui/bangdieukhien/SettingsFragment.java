package com.example.chodocu_ver1.ui.bangdieukhien;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.DoiMatKhauActivity;
import com.example.chodocu_ver1.DonBanActivity;
import com.example.chodocu_ver1.DonMuaActivity;
import com.example.chodocu_ver1.LichHenActivity;
import com.example.chodocu_ver1.MessageMain;
import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.DangKyCuaHangActivity;
import com.example.chodocu_ver1.ThongTinTaiKhoanActivity;
import com.example.chodocu_ver1.DanhGia_SanPhamActivity;
import com.example.chodocu_ver1.UserLichSuDonBanActivity;
import com.example.chodocu_ver1.UserMainActivity;
import com.example.chodocu_ver1.UserCuaHangActivity;
import com.example.chodocu_ver1.UserTaoLichHenActivity;
import com.example.chodocu_ver1.User_LichSu_GiaoDichActivity;
import com.example.chodocu_ver1.User_Bai_TaiLenActivity;
import com.example.chodocu_ver1.User_Cho_ChuyenKhoanActivity;
import com.example.chodocu_ver1.ViDienTu_UserActivity;
import com.example.chodocu_ver1.XoaSanPhamActivity;
import com.example.chodocu_ver1.data_models.CuaHang;
import com.example.chodocu_ver1.data_models.CuocHen;
import com.example.chodocu_ver1.data_models.DatHang;
import com.example.chodocu_ver1.data_models.RemoveSanPham;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    private FirebaseUser firebaseUser = UserMainActivity.user;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private SettingsViewModel dashboardViewModel;
    private Button btnChat, btnLogout, btnUploadPost, btnShop, btnWallet, btnNotification, btnLichHen, btnTaoLichHen, btnReceiveMoney, btnLichSuDonHang, btnLichSuDonBan, btnAccountInfo, btnDonMua, btnDonBan, btnPassWordChange, btnDanhGiaSP;
    private ImageView imgAccount;
    private Intent intent;
    private TextView txtAccountName, txtSpDaBan, txtDiemThanhVien, txtNotification, txtDonMuaNotify, txtDonBanNotify, txtDanhGiaSP, txtMoneyNotify, txtLichHen;
    private String sUserName = UserMainActivity.sUserName, userID = "";
    private ArrayList<CuaHang> cuaHangArrayList;
    private ArrayList<DatHang> donMuaArrayList;
    private ArrayList<DatHang> donBanArrayList;
    private ArrayList<DatHang> danhGiaSPList;
    private ArrayList<DatHang> waitingList;
    private ArrayList<CuocHen> appointmentArrayList;
    private ArrayList<RemoveSanPham> removeProductDataArrayList;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private int diemThanhVien = 0;
    private String shopID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_cai_dat, container, false);





        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnAccountInfo = (Button) view.findViewById(R.id.btnAccountInfo);
        btnShop = (Button) view.findViewById(R.id.btnShop);
        btnLichSuDonHang = (Button) view.findViewById(R.id.btnLichSuDonHang);
        btnLichSuDonBan = (Button) view.findViewById(R.id.btnLichSuDonBan);
        btnUploadPost = (Button) view.findViewById(R.id.btnUploadPost);
        btnWallet = (Button) view.findViewById(R.id.btnWallet);
        btnDonMua = (Button) view.findViewById(R.id.btnDonMua);
        btnDonBan = (Button) view.findViewById(R.id.btnDonBan);
        btnPassWordChange = (Button) view.findViewById(R.id.btnPassWordChange);
        btnChat = (Button) view.findViewById(R.id.btnChat);
        btnDanhGiaSP = (Button) view.findViewById(R.id.btnDanhGiaSP);
        btnTaoLichHen = (Button) view.findViewById(R.id.btnTaoLichHen);
        btnLichHen = (Button) view.findViewById(R.id.btnLichHen);
        txtDanhGiaSP = (TextView) view.findViewById(R.id.txtDanhGiaSP);
        txtAccountName = (TextView) view.findViewById(R.id.txtAccountName);
        txtSpDaBan = (TextView) view.findViewById(R.id.txtSpDaBan);
        txtDiemThanhVien = (TextView) view.findViewById(R.id.txtDiemThanhVien);
        txtDonMuaNotify = (TextView) view.findViewById(R.id.txtDonMuaNotify);
        txtDonBanNotify = (TextView) view.findViewById(R.id.txtDonBanNotify);
        txtLichHen = (TextView) view.findViewById(R.id.txtLichHen);
        imgAccount = (ImageView) view.findViewById(R.id.imgAccount);
        btnReceiveMoney = (Button) view.findViewById(R.id.btnReceiveMoney);
        txtMoneyNotify = (TextView) view.findViewById(R.id.txtMoneyNotify);
        btnNotification = (Button) view.findViewById(R.id.btnNotification);
        txtNotification = (TextView) view.findViewById(R.id.txtNotification);

        cuaHangArrayList = new ArrayList<>();
        donMuaArrayList = new ArrayList<>();
        donBanArrayList = new ArrayList<>();
        danhGiaSPList = new ArrayList<>();
        waitingList = new ArrayList<>();
        appointmentArrayList = new ArrayList<>();
        removeProductDataArrayList = new ArrayList<>();

        if(sUserName != ""){
            databaseReference.child("DangKyCuaHang").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    cuaHangArrayList.add(snapshot.getValue(CuaHang.class));
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
                    if (snapshot.getValue(UserData.class).getUserID().equals(UserMainActivity.sUserID)) {
                        txtAccountName.setText(snapshot.getValue(UserData.class).getHoTen());
                        btnWallet.setText("Ví điện tử: " + String.valueOf(snapshot.getValue(UserData.class).getMoney()) + "vnđ");
                        txtSpDaBan.setText("Số sản phẩm đã bán: " + String.valueOf(snapshot.getValue(UserData.class).getSoSPDaBan()));
                        txtDiemThanhVien.setText("Điểm thành viên: " + String.valueOf(snapshot.getValue(UserData.class).getDiemThanhVien()));
                        shopID = snapshot.getValue(UserData.class).getShopID();
                        userID = snapshot.getValue(UserData.class).getUserID();
                        diemThanhVien = snapshot.getValue(UserData.class).getDiemThanhVien();
                        if(!snapshot.getValue(UserData.class).getImage().isEmpty()){
                            storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(view.getContext()).load(uri).into(imgAccount);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                        databaseReference.child("XoaSanPham").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if (snapshot.getValue(RemoveSanPham.class).getSanPham().getUserID().equals(userID)) {
                                    removeProductDataArrayList.add(snapshot.getValue(RemoveSanPham.class));
                                }

                                if (removeProductDataArrayList.size() != 0) {
                                    txtNotification.setVisibility(View.VISIBLE);
                                    txtNotification.setText(String.valueOf(removeProductDataArrayList.size()));
                                } else {
                                    txtNotification.setVisibility(View.INVISIBLE);
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
                        databaseReference.child("Comment").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if (snapshot.getValue(DatHang.class).getNguoiMuaID().equals(userID)) {
                                    danhGiaSPList.add(snapshot.getValue(DatHang.class));
                                }

                                if (danhGiaSPList.size() != 0) {
                                    txtDanhGiaSP.setVisibility(View.VISIBLE);
                                    txtDanhGiaSP.setText(String.valueOf(danhGiaSPList.size()));
                                } else {
                                    txtDanhGiaSP.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                DatHang orderRemove = null;
                                for (DatHang orderData : waitingList){
                                    if(orderData.getDonHangID().equals(snapshot.getValue(DatHang.class).getDonHangID())){
                                        orderRemove = orderData;
                                    }
                                }

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if (snapshot.getValue(DatHang.class).getNguoiMuaID().equals(userID) && snapshot.getValue(DatHang.class).getTinhTrang() != 7) {
                                    donMuaArrayList.add(snapshot.getValue(DatHang.class));
                                } else if (snapshot.getValue(DatHang.class).getNguoiBanID().equals(userID) && snapshot.getValue(DatHang.class).getTinhTrang() < 7) {
                                    donBanArrayList.add(snapshot.getValue(DatHang.class));
                                }

                                if (donMuaArrayList.size() != 0) {
                                    txtDonMuaNotify.setVisibility(View.VISIBLE);
                                    txtDonMuaNotify.setText(String.valueOf(donMuaArrayList.size()));
                                } else {
                                    txtDonMuaNotify.setVisibility(View.INVISIBLE);
                                }

                                if (donBanArrayList.size() != 0) {
                                    txtDonBanNotify.setVisibility(View.VISIBLE);
                                    txtDonBanNotify.setText(String.valueOf(donBanArrayList.size()));
                                } else {
                                    txtDonBanNotify.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                DatHang orderRemove = null;
                                for(DatHang orderData : danhGiaSPList){
                                    if(orderData.getDonHangID().equals(snapshot.getValue(DatHang.class).getDonHangID())){
                                        orderRemove = orderData;
                                    }
                                }
                                if(orderRemove != null){
                                    danhGiaSPList.remove(orderRemove);
                                }
                                if (danhGiaSPList.size() != 0) {
                                    txtDanhGiaSP.setVisibility(View.VISIBLE);
                                    txtDanhGiaSP.setText(String.valueOf(danhGiaSPList.size()));
                                }
                                else{
                                    txtDanhGiaSP.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                if (donMuaArrayList.size() != 0) {
                                    txtDonMuaNotify.setVisibility(View.VISIBLE);
                                    txtDonMuaNotify.setText(String.valueOf(donMuaArrayList.size()));
                                }
                                else{
                                    txtDonMuaNotify.setVisibility(View.INVISIBLE);
                                }

                                if (donBanArrayList.size() != 0) {
                                    txtDonBanNotify.setVisibility(View.VISIBLE);
                                    txtDonBanNotify.setText(String.valueOf(donBanArrayList.size()));
                                }
                                else{
                                    txtDonBanNotify.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        databaseReference.child("MoneyIncome").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if (snapshot.getValue(DatHang.class).getNguoiBanID().equals(userID)) {
                                    waitingList.add(snapshot.getValue(DatHang.class));
                                }
                                if (waitingList.size() != 0) {
                                    txtMoneyNotify.setVisibility(View.VISIBLE);
                                    txtMoneyNotify.setText(String.valueOf(waitingList.size()));
                                } else {
                                    txtMoneyNotify.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                DatHang orderRemove = null;
                                for (DatHang orderData : donMuaArrayList){
                                    if(orderData.getDonHangID().equals(snapshot.getValue(DatHang.class).getDonHangID()) && snapshot.getValue(DatHang.class).getTinhTrang() == 7){
                                        orderRemove = orderData;
                                    }
                                }
                                if(orderRemove != null){
                                    donMuaArrayList.remove(orderRemove);
                                    donBanArrayList.remove(orderRemove);
                                }

                                if (donMuaArrayList.size() != 0) {
                                    txtDonMuaNotify.setVisibility(View.VISIBLE);
                                    txtDonMuaNotify.setText(String.valueOf(donMuaArrayList.size()));
                                }
                                else{
                                    txtDonMuaNotify.setVisibility(View.INVISIBLE);
                                }

                                if (donBanArrayList.size() != 0) {
                                    txtDonBanNotify.setVisibility(View.VISIBLE);
                                    txtDonBanNotify.setText(String.valueOf(donBanArrayList.size()));
                                }
                                else{
                                    txtDonBanNotify.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                                DatHang orderRemove = null;
                                for (DatHang orderData : donMuaArrayList){
                                    if(orderData.getDonHangID().equals(snapshot.getValue(DatHang.class).getDonHangID())){
                                        orderRemove = orderData;
                                    }
                                }
                                if(orderRemove != null){
                                    donMuaArrayList.remove(orderRemove);
                                    donBanArrayList.remove(orderRemove);
                                }
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
            databaseReference.child("CuocHen").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if ((snapshot.getValue(CuocHen.class).getNguoiDuocHenID().equals(userID) && snapshot.getValue(CuocHen.class).isActive()) || snapshot.getValue(CuocHen.class).getNguoiHenID().equals(userID)) {
                        appointmentArrayList.add(snapshot.getValue(CuocHen.class));
                    }

                    if (appointmentArrayList.size() != 0) {
                        txtLichHen.setVisibility(View.VISIBLE);
                        txtLichHen.setText(String.valueOf(appointmentArrayList.size()));
                    } else {
                        txtLichHen.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    CuocHen appointmentRemove = null;
                    for(CuocHen appointment : appointmentArrayList){
                        if(appointment.getIdCuocHen().equals(snapshot.getValue(CuocHen.class).getIdCuocHen()) && snapshot.getValue(CuocHen.class).isActive() == false){
                            appointmentRemove = appointment;
                        }
                    }
                    if(appointmentRemove != null){
                        appointmentArrayList.remove(appointmentRemove);
                    }
                    if (appointmentArrayList.size() != 0) {
                        txtLichHen.setVisibility(View.VISIBLE);
                        txtLichHen.setText(String.valueOf(appointmentArrayList.size()));
                    }
                    else{
                        txtLichHen.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    CuocHen appointmentRemove = null;
                    for(CuocHen appointment : appointmentArrayList){
                        if(appointment.getIdCuocHen().equals(snapshot.getValue(CuocHen.class).getIdCuocHen())){
                            appointmentRemove = appointment;
                        }
                    }
                    if(appointmentRemove != null){
                        appointmentArrayList.remove(appointmentRemove);
                    }
                    if (appointmentArrayList.size() != 0) {
                        txtLichHen.setVisibility(View.VISIBLE);
                        txtLichHen.setText(String.valueOf(appointmentArrayList.size()));
                    }
                    else{
                        txtLichHen.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Toast.makeText(view.getContext(), "Không nhận được dữ liệu!", Toast.LENGTH_SHORT).show();
        }
        btnLogout.setOnClickListener(logoutClick);
        btnAccountInfo.setOnClickListener(accountInfoClick);
        btnPassWordChange.setOnClickListener(passwordChangeClick);
        btnWallet.setOnClickListener(walletClick);
        btnShop.setOnClickListener(shopClick);
        btnDonMua.setOnClickListener(donMuaClick);
        btnDonBan.setOnClickListener(donBanClick);
        btnLichSuDonHang.setOnClickListener(lichSuDonHang);
        btnLichSuDonBan.setOnClickListener(lichSuDonBanClick);
        btnUploadPost.setOnClickListener(uploadedPostClick);
        btnDanhGiaSP.setOnClickListener(danhGiaSPClick);
        btnReceiveMoney.setOnClickListener(choNhanTienClick);
        btnTaoLichHen.setOnClickListener(taoLichHenClick);
        btnLichHen.setOnClickListener(lichHenClick);
        btnNotification.setOnClickListener(notificationClick);

        btnChat.setOnClickListener(chat);

        return view;
    }
    View.OnClickListener notificationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), XoaSanPhamActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);


        }
    };

    View.OnClickListener lichHenClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), LichHenActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener taoLichHenClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserTaoLichHenActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.putExtra("NguoiCanGapID", "");
            intent.putExtra("NguoiCanGapName", "");
            intent.putExtra("TieuDe", "");
            intent.putExtra("NgayHen", "");
            intent.putExtra("ChiTiet", "");
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener choNhanTienClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), User_Cho_ChuyenKhoanActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener danhGiaSPClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), DanhGia_SanPhamActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            startActivity(intent);
        }
    };

    View.OnClickListener lichSuDonBanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserLichSuDonBanActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener uploadedPostClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), User_Bai_TaiLenActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener lichSuDonHang = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), User_LichSu_GiaoDichActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener donBanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), DonBanActivity.class);
            intent.putExtra("UserID", userID);
            intent.putExtra("UserName", sUserName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener donMuaClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), DonMuaActivity.class);
            intent.putExtra("UserID", userID);
            intent.putExtra("UserName", sUserName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    public boolean shopRegistrationCheck(ArrayList<CuaHang> shopDataArrayList, String shopID) {
        for (CuaHang shopData : shopDataArrayList) {
            if (shopData.getShopID().equals(shopID)) {
                return false;
            }
        }
        return true;
    }

    View.OnClickListener shopClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (diemThanhVien < 20 && shopID.isEmpty()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa đủ điều kiện tạo shop!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            } else if (shopRegistrationCheck(cuaHangArrayList, shopID) == false) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Yêu cầu tạo shop đang được xử lý!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            } else if (diemThanhVien >= 20 && shopID.isEmpty()) {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                intent = new Intent(v.getContext(), DangKyCuaHangActivity.class);
                                intent.putExtra("UserName", sUserName);
                                intent.putExtra("UserID", userID);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Bạn chưa đăng ký shop, tiến hành đăng ký?").setNegativeButton("No", dialogClick).setPositiveButton("Yes", dialogClick).show();
            }

            else if (!shopID.isEmpty()) {
                databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.getValue(CuaHang.class).getShopID().equals(shopID)) {
                            if (snapshot.getValue(CuaHang.class).getTinhTrangShop() == -1) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                alert.setMessage("Shop bạn đã bị khóa do điểm thành viên <20!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                            } else if (snapshot.getValue(CuaHang.class).getTinhTrangShop() == 1) {
//                                Toast.makeText(v.getContext(), "Bạn đã tạo shop thành công!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(v.getContext(), UserCuaHangActivity.class);
                                intent.putExtra("UserName", sUserName);
                                intent.putExtra("UserID", userID);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
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
    };

    View.OnClickListener chat = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), MessageMain.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
    View.OnClickListener walletClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ViDienTu_UserActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener passwordChangeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), DoiMatKhauActivity.class);
            intent.putExtra("UserName", sUserName);
            startActivity(intent);
        }
    };

    View.OnClickListener accountInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ThongTinTaiKhoanActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            startActivity(intent);
        }
    };

    View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:

                            FirebaseAuth.getInstance().signOut();
                            getActivity().finish();

                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;

                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Bạn muốn đăng xuất?").setNegativeButton("No", dialogClick).setPositiveButton("Yes", dialogClick).show();
        }
    };
}
