package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.adapter.SanPhamAdapter;
import com.example.chodocu_ver1.data_models.CuaHang;
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

import java.util.ArrayList;

public class UserCuaHangActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private TextView txtUserName, txtNgayThamGia, txtDiaChi, txtLienHe, txtMoTaShop;
    private ListView lstSanPham;
    private Button btnBack, btnAll, btnDoCu, btnDoMoi, btnPost, btnShopEdit;
    private ImageView imgUser;
    private String userName, userID, shopID;
    private Intent intent;
    private ArrayList<SanPham> sanPhamArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_cuahang_layout);

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtNgayThamGia = (TextView) findViewById(R.id.txtNgayThamGia);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        txtLienHe = (TextView) findViewById(R.id.txtLienHe);
        txtMoTaShop = (TextView) findViewById(R.id.txtMoTaShop);
        lstSanPham = (ListView) findViewById(R.id.lstSanPham);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnAll = (Button) findViewById(R.id.btnAll);
        btnDoCu = (Button) findViewById(R.id.btnDoCu);
        btnDoMoi = (Button) findViewById(R.id.btnDoMoi);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnShopEdit = (Button) findViewById(R.id.btnShopEdit);
        imgUser = (ImageView) findViewById(R.id.imgUser);

        sanPhamArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);

        btnAll.setOnClickListener(allClick);
        btnDoCu.setOnClickListener(doCuClick);
        btnDoMoi.setOnClickListener(doMoiClick);
        btnShopEdit.setOnClickListener(shopEditClick);

        lstSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), User_ChinhSua_SanPhamActivity.class);
                intent.putExtra("ProductID", sanPhamArrayList.get(position).getiD());
                intent.putExtra("UserName", userName);
                intent.putExtra("UserID", userID);
                intent.putExtra("NavigateTo", "UserShop");
                startActivity(intent);
            }
        });

        btnPost.setOnClickListener(postClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");

            sanPhamArrayList.clear();
            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                    Toast.makeText(UserCuaHangActivity.this, userID, Toast.LENGTH_SHORT).show();

                    //sanPhamArrayList.add(snapshot.getValue(SanPham.class));
                    if(snapshot.getValue(SanPham.class).getUserID() != null){
                        if(snapshot.getValue(SanPham.class).getUserID().equals(userID)){
                            sanPhamArrayList.add(snapshot.getValue(SanPham.class));
                        }
                    }
                    else {
                        Toast.makeText(UserCuaHangActivity.this, "Null", Toast.LENGTH_SHORT).show();
                    }
//                    if(snapshot.getValue(SanPham.class).getUserID().equals(userID)){
//                        sanPhamArrayList.add(snapshot.getValue(SanPham.class));
//                    }
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

            databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(CuaHang.class).getUserID().equals(userID)){
                        shopID = snapshot.getValue(CuaHang.class).getShopID();
                        storageReference.child(snapshot.getValue(CuaHang.class).getShopImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(UserCuaHangActivity.this).load(uri).into(imgUser);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtNgayThamGia.setText("Ngày tham gia: " + snapshot.getValue(CuaHang.class).getNgayTaoShop());
                        txtUserName.setText(snapshot.getValue(CuaHang.class).getShopName());
                        txtMoTaShop.setText(snapshot.getValue(CuaHang.class).getMoTaShop());

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

                        txtDiaChi.setText("Địa chỉ: " + snapshot.getValue(UserData.class).getDiaChi());
                        txtLienHe.setText("Liên hệ: " + snapshot.getValue(UserData.class).getSoDienThoai());

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
            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sanPhamLoad();
                }
            }, delay);
        }
    }

    View.OnClickListener shopEditClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), User_ChinhSua_CuaHangActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.putExtra("ShopID", shopID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener postClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), User_DangBaiActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.putExtra("ShopID", shopID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener doMoiClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sanPhamArrayList.clear();
            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getUserID().equals(userID) && snapshot.getValue(SanPham.class).getTinhTrang() == 0){
                        sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(UserCuaHangActivity.this, R.layout.san_pham_adapter_layout, sanPhamArrayList);
                    lstSanPham.setAdapter(sanPhamAdapter);
                }
            }, delay);
        }
    };

    View.OnClickListener doCuClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sanPhamArrayList.clear();
            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getUserID().equals(userID) && snapshot.getValue(SanPham.class).getTinhTrang() == 1){
                        sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(UserCuaHangActivity.this, R.layout.san_pham_adapter_layout, sanPhamArrayList);
                    lstSanPham.setAdapter(sanPhamAdapter);
                }
            }, delay);
        }
    };

    View.OnClickListener allClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sanPhamArrayList.clear();
            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getUserID().equals(userID)){
                        sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(UserCuaHangActivity.this, R.layout.san_pham_adapter_layout, sanPhamArrayList);
                    lstSanPham.setAdapter(sanPhamAdapter);
                }
            }, delay);
        }
    };

    public void sanPhamLoad(){
        SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(UserCuaHangActivity.this, R.layout.san_pham_adapter_layout, sanPhamArrayList);
        lstSanPham.setAdapter(sanPhamAdapter);
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}