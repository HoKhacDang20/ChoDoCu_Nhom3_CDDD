package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.data_models.CuaHang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class User_ChinhSua_CuaHangActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgShop;
    private Button btnChooseFromGallery, btnOpenCamera, btnUpdate, btnBack;
    private EditText edtTenShop, edtMoTaShop;
    private String userName, userID, shopID;
    private Intent intent;
    private int PICK_IMAGE = 123;
    private int CAMERA_IMAGE = 123;
    private ArrayList<CuaHang> shopDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_chinhsua_cuahang_layout);

        imgShop = (ImageView) findViewById(R.id.imgShop);
        btnChooseFromGallery = (Button) findViewById(R.id.btnChooseFromGallery);
        btnOpenCamera = (Button) findViewById(R.id.btnOpenCamera);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnBack = (Button) findViewById(R.id.btnBack);
        edtTenShop = (EditText) findViewById(R.id.edtTenShop);
        edtMoTaShop = (EditText) findViewById(R.id.edtMoTaShop);

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
            shopID = getIntent().getExtras().getString("ShopID");

            databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(!snapshot.getValue(CuaHang.class).getShopID().equals(shopID))
                    {
                        shopDataArrayList.add(snapshot.getValue(CuaHang.class));
                    }
                    if(snapshot.getValue(CuaHang.class).getShopID().equals(shopID)){

                        storageReference.child(snapshot.getValue(CuaHang.class).getShopImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(User_ChinhSua_CuaHangActivity.this).load(uri).into(imgShop);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
             // Toast.makeText(this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        edtTenShop.setText(snapshot.getValue(CuaHang.class).getShopName());
                        edtMoTaShop.setText(snapshot.getValue(CuaHang.class).getMoTaShop());
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


        shopDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);
        btnChooseFromGallery.setOnClickListener(chooseImageClick);
        btnOpenCamera.setOnClickListener(openCameraClick);
        btnUpdate.setOnClickListener(updateClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    View.OnClickListener updateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtTenShop.getText().toString().isEmpty()){
                edtTenShop.setError("Bạn chưa nhập tên shop!");
            }
            else if(shopNameCheck(shopDataArrayList, edtTenShop.getText().toString()) == false){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Tên shop đã tồn tại, xin chọn tên khác!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtMoTaShop.getText().toString().isEmpty()){
                edtMoTaShop.setError("Bạn chưa nhập mô tả shop!");
            }
            else{
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                        if(snapshot.getValue(CuaHang.class).getShopID().equals(shopID)){
                                            storageReference.child(snapshot.getValue(CuaHang.class).getShopImage() + ".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
//                                                    Toast.makeText(AccountInfoActivity.this, "Xoa hinh thanh cong", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
//                                                    Toast.makeText(AccountInfoActivity.this, "Xoa hinh that bai", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            String shopImage = databaseReference.push().getKey();

                                            final StorageReference mountainsRef = storageReference.child(shopImage + ".png");

                                            imgShop.setDrawingCacheEnabled(true);
                                            imgShop.buildDrawingCache();
                                            Bitmap bitmap = ((BitmapDrawable) imgShop.getDrawable()).getBitmap();
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                            byte[] data = baos.toByteArray();
                                            final UploadTask uploadTask = mountainsRef.putBytes(data);
                                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Toast.makeText(v.getContext(), "Thêm hình thất bại!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                                }
                                            });

                                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                            Date date = new Date();

                                            CuaHang shopData = new CuaHang(shopID, snapshot.getValue(CuaHang.class).getUserID(), edtTenShop.getText().toString(), edtMoTaShop.getText().toString(), shopImage, dateFormat.format(date),snapshot.getValue(CuaHang.class).getTinhTrangShop());

                                            databaseReference.child("Shop").child(shopID).setValue(shopData);

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

                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                alert.setMessage("Cập nhật thông tin shop thành công!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        intent = new Intent(v.getContext(), UserCuaHangActivity.class);
                                        intent.putExtra("UserName", userName);
                                        intent.putExtra("UserID", userID);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        finish();
                                        startActivity(intent);
                                    }
                                }).show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Cập nhật thông tin shop?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }
        }
    };

    public boolean shopNameCheck(ArrayList<CuaHang> shopDataArrayList, String shopName){
        for(CuaHang shopData : shopDataArrayList){
            if(shopData.getShopName().equals(shopName)){
                return false;
            }
        }
        return true;
    }

    View.OnClickListener chooseImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PICK_IMAGE = 1;
            Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }
    };

    View.OnClickListener openCameraClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CAMERA_IMAGE = 2;
            Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, CAMERA_IMAGE);
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
//            intent = new Intent(v.getContext(), UserMainActivity.class);
//            intent.putExtra("UserName", userName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            finish();
//            startActivity(intent);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(PICK_IMAGE != 123){
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                imgShop.setImageURI(imageUri);
            }
            PICK_IMAGE = 123;
        }
        if(CAMERA_IMAGE != 123){
            if(requestCode == CAMERA_IMAGE && resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgShop.setImageBitmap(bitmap);
            }
            CAMERA_IMAGE = 123;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}