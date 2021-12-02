package com.example.chodocu_ver1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.chodocu_ver1.data_models.CuaHang;
import com.example.chodocu_ver1.data_models.DanhMuc;
import com.example.chodocu_ver1.data_models.HoaHong;
import com.example.chodocu_ver1.data_models.SanPham;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class UserMainActivity extends AppCompatActivity{

    public static FirebaseUser user;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    static public String sUserName;
    static public String sUserID;
    static public String sShopID;
    static public ArrayList<UserData> userDataArrayList;
    static public ArrayList<DanhMuc> danhMucDataArrayList;
    static public ArrayList<SanPham> sanPhamArrayList;
    private ArrayList<CuaHang> shopDataArrayList;
    private Intent intent = DangNhapActivity.intent;
    private BottomNavigationView navView;
    private int userCommission = 0, shopCommission = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_main_layout);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        shopDataArrayList = new ArrayList<>();

        danhMucDataArrayList = new ArrayList<>();
        databaseReference.child("DanhMuc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                danhMucDataArrayList.add(snapshot.getValue(DanhMuc.class));
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

        sanPhamArrayList = new ArrayList<>();
        databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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

    @Override
    protected void onResume() {
        super.onResume();

        databaseReference.child("HoaHong").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(HoaHong.class).getId().equals("-MKyZZdaQ3ucidlxPkUV")){
                    userCommission = snapshot.getValue(HoaHong.class).getUserCommission();
                    shopCommission = snapshot.getValue(HoaHong.class).getShipperCommission();
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

        reloadShopArrayList();

        if(user != null){
//            Toast.makeText(this, getIntent().getExtras().getString("UserName"), Toast.LENGTH_SHORT).show();



            userDataArrayList = new ArrayList<>();

            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getUserID().equals(user.getUid())){
                        sShopID = snapshot.getValue(UserData.class).getShopID();
                        sUserID = snapshot.getValue(UserData.class).getUserID();
                        sUserName = snapshot.getValue(UserData.class).getUserName();

                        if(!sShopID.isEmpty()){
                            Handler handler = new Handler();
                            int delay = 1000;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(snapshot.getValue(UserData.class).getDiemThanhVien() <= 0){
                                        databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(CuaHang.class).getUserID().equals(sUserID)){
                                                    CuaHang shopData = new CuaHang(snapshot.getValue(CuaHang.class).getShopID(), snapshot.getValue(CuaHang.class).getUserID(),
                                                            snapshot.getValue(CuaHang.class).getShopName(), snapshot.getValue(CuaHang.class).getMoTaShop(), snapshot.getValue(CuaHang.class).getShopImage(),
                                                            snapshot.getValue(CuaHang.class).getNgayTaoShop(), -1);
                                                    databaseReference.child("Shop").child(snapshot.getValue(CuaHang.class).getShopID()).setValue(shopData);
                                                    updateSanPham();

                                                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            if(snapshot.getValue(UserData.class).getUserID().equals(sUserID)){
                                                                UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getUserName(),snapshot.getValue(UserData.class).getShopID(),snapshot.getValue(UserData.class).getHoTen(),
                                                                        snapshot.getValue(UserData.class).getSoDienThoai(), snapshot.getValue(UserData.class).getGioiTinh(), snapshot.getValue(UserData.class).getDiaChi(),
                                                                        snapshot.getValue(UserData.class).getPassword(), snapshot.getValue(UserData.class).getImage(), snapshot.getValue(UserData.class).getUserID(),
                                                                        snapshot.getValue(UserData.class).getNgayThamGia(),snapshot.getValue(UserData.class).getSoCMND(),snapshot.getValue(UserData.class).getEmail(),snapshot.getValue(UserData.class).getCmndMatTruoc(), snapshot.getValue(UserData.class).getPermission(), snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(),
                                                                        snapshot.getValue(UserData.class).getSoSPDaBan(), snapshot.getValue(UserData.class).getDiemThanhVien(), snapshot.getValue(UserData.class).getReport(), snapshot.getValue(UserData.class).getMoney());
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
                                    else if(snapshot.getValue(UserData.class).getDiemThanhVien() >= 20 && shopActiveCheck(shopDataArrayList, sShopID) == -1){
                                        databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(CuaHang.class).getUserID().equals(sUserID)){
                                                    CuaHang shopData = new CuaHang(snapshot.getValue(CuaHang.class).getShopID(), snapshot.getValue(CuaHang.class).getUserID(),
                                                            snapshot.getValue(CuaHang.class).getShopName(), snapshot.getValue(CuaHang.class).getMoTaShop(), snapshot.getValue(CuaHang.class).getShopImage(),
                                                            snapshot.getValue(CuaHang.class).getNgayTaoShop(), 1);
                                                    databaseReference.child("Shop").child(snapshot.getValue(CuaHang.class).getShopID()).setValue(shopData);
                                                    updateSanPham();

                                                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            if(snapshot.getValue(UserData.class).getUserID().equals(sUserID)){
                                                                UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getUserName(),snapshot.getValue(UserData.class).getShopID(),snapshot.getValue(UserData.class).getHoTen(),
                                                                        snapshot.getValue(UserData.class).getSoDienThoai(), snapshot.getValue(UserData.class).getGioiTinh(), snapshot.getValue(UserData.class).getDiaChi(),
                                                                        snapshot.getValue(UserData.class).getPassword(), snapshot.getValue(UserData.class).getImage(), snapshot.getValue(UserData.class).getUserID(),
                                                                        snapshot.getValue(UserData.class).getNgayThamGia(),snapshot.getValue(UserData.class).getSoCMND(),snapshot.getValue(UserData.class).getEmail(),snapshot.getValue(UserData.class).getCmndMatTruoc(), snapshot.getValue(UserData.class).getPermission(), snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(),
                                                                        snapshot.getValue(UserData.class).getSoSPDaBan(), snapshot.getValue(UserData.class).getDiemThanhVien(), snapshot.getValue(UserData.class).getReport(), snapshot.getValue(UserData.class).getMoney());
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
                            }, delay);
                        }
                    }
                    UserData userData = snapshot.getValue(UserData.class);
                    userDataArrayList.add(userData);
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
        else{
            Toast.makeText(this, "Không nhận được data", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateSanPham(){
        databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(CuaHang.class).getShopID().equals(sShopID)){
                    if(snapshot.getValue(CuaHang.class).getTinhTrangShop() == 1){
                        databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(SanPham.class).getUserID().equals(sUserID)){
                                    SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getiD(), snapshot.getValue(SanPham.class).getUserID(),
                                            sShopID, snapshot.getValue(SanPham.class).getTenSP(), snapshot.getValue(SanPham.class).getSpImage(), snapshot.getValue(SanPham.class).getMoTa(),
                                            snapshot.getValue(SanPham.class).getDanhMuc(), snapshot.getValue(SanPham.class).getNgayDang(), snapshot.getValue(SanPham.class).getDiaChiDang(), snapshot.getValue(SanPham.class).getGiaTien(), snapshot.getValue(SanPham.class).getSoLuong(), snapshot.getValue(SanPham.class).getTinhTrang());
                                    databaseReference.child("SanPham").child(snapshot.getValue(SanPham.class).getiD()).setValue(sanPham);
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
                    else if(snapshot.getValue(CuaHang.class).getTinhTrangShop() == 3){
                        databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(SanPham.class).getUserID().equals(sUserID)){
                                    SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getiD(), snapshot.getValue(SanPham.class).getUserID(),
                                            sShopID, snapshot.getValue(SanPham.class).getTenSP(), snapshot.getValue(SanPham.class).getSpImage(), snapshot.getValue(SanPham.class).getMoTa(),
                                            snapshot.getValue(SanPham.class).getDanhMuc(), snapshot.getValue(SanPham.class).getNgayDang(), snapshot.getValue(SanPham.class).getDiaChiDang(), snapshot.getValue(SanPham.class).getGiaTien(), snapshot.getValue(SanPham.class).getSoLuong(), snapshot.getValue(SanPham.class).getTinhTrang());
                                    databaseReference.child("SanPham").child(snapshot.getValue(SanPham.class).getiD()).setValue(sanPham);
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

    public void reloadShopArrayList(){
        shopDataArrayList.clear();
        databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
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
    }

    public int shopActiveCheck(ArrayList<CuaHang> shopDataArrayList, String shopID){
        for(CuaHang shopData : shopDataArrayList){
            if(shopData.getShopID().equals(shopID)){
                return shopData.getTinhTrangShop();
            }
        }
        return -1;
    }
}