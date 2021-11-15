package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chodocu_ver1.data_models.DanhMuc;
import com.example.chodocu_ver1.data_models.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class User_DangBaiActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ArrayList<DanhMuc> danhMucDataArrayList = UserMainActivity.danhMucDataArrayList;
    private ArrayList<String> danhMucList;
    private ImageView spIMG;
    private String userName, userID, shopID;
    private Button btnChooseFromGallery, btnOpenCamera, btnPost, btnClearPost, btnBack;
    private EditText edtTenSP, edtGiaSP, edtSoLuongSP, edtMoTaSP, edtDiaChiDang;
    private Spinner spnPhanLoai, spnDanhMuc;
    private int PICK_IMAGE = 123;
    private int CAMERA_IMAGE = 123;
    private int IMGCheck = 0;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_dangbai_moi_layout);

        spIMG = (ImageView)  findViewById(R.id.spIMG);
        btnChooseFromGallery = (Button) findViewById(R.id.btnChooseFromGallery);
        btnOpenCamera = (Button) findViewById(R.id.btnOpenCamera);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnClearPost = (Button) findViewById(R.id.btnClearPost);
        btnBack = (Button) findViewById(R.id.btnBack);
        edtTenSP = (EditText) findViewById(R.id.edtTenSP);
        edtGiaSP = (EditText) findViewById(R.id.edtGiaSP);
        edtSoLuongSP = (EditText) findViewById(R.id.edtSoLuongSP);
        edtMoTaSP = (EditText) findViewById(R.id.edtMoTaSP);
        edtDiaChiDang = (EditText) findViewById(R.id.edtDiaChiDang);
        spnPhanLoai = (Spinner) findViewById(R.id.spnPhanLoai);
        spnDanhMuc = (Spinner) findViewById(R.id.spnDanhMuc);

        danhMucList = new ArrayList<>();

        for(DanhMuc danhMucData : danhMucDataArrayList){
            danhMucList.add(danhMucData.getTenDanhMuc());
        }
        danhMucList.add("Khác");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(User_DangBaiActivity.this, android.R.layout.simple_spinner_item, danhMucList);
        spnDanhMuc.setAdapter(arrayAdapter);

        btnChooseFromGallery.setOnClickListener(chooseGalleryClick);
        btnOpenCamera .setOnClickListener(openCameraClick);
        btnClearPost.setOnClickListener(clearPostClick);
        btnPost.setOnClickListener(postClick);
        btnBack.setOnClickListener(backClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
            shopID = getIntent().getExtras().getString("ShopID");


        }
    }

    View.OnClickListener postClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(IMGCheck == 0){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn hình ảnh cho sản phẩm!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtTenSP.getText().toString().isEmpty()){
                edtTenSP.setError("Bạn chưa nhập tên sản phẩm!");
            }
            else if(edtDiaChiDang.getText().toString().isEmpty()){
                edtDiaChiDang.setError("Bạn chưa nhập địa chỉ đăng sản phẩm!");
            }
            else if(edtGiaSP.getText().toString().isEmpty()){
                edtGiaSP.setError("Bạn chưa nhập giá sản phẩm!");
            }
            else if(edtSoLuongSP.getText().toString().isEmpty()){
                edtSoLuongSP.setError("Bạn chưa nhập số lượng sản phẩm!");
            }
            else if(Integer.parseInt(edtSoLuongSP.getText().toString()) < 1){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số lượng sản phẩm phải > 0!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtMoTaSP.getText().toString().isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa mô tả sản phẩm!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                                String sKey = databaseReference.push().getKey();

                                final StorageReference mountainsRef = storageReference.child(sKey + ".png");

                                spIMG.setDrawingCacheEnabled(true);
                                spIMG.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) spIMG.getDrawable()).getBitmap();
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
                                String sSPID = databaseReference.push().getKey();
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = new Date();
                                SanPham sanPham = new SanPham(sSPID, userID, shopID, edtTenSP.getText().toString(),sKey,edtMoTaSP.getText().toString(),spnDanhMuc.getSelectedItem().toString(), dateFormat.format(date), edtDiaChiDang.getText().toString(), Long.valueOf(edtGiaSP.getText().toString()),
                                        Integer.valueOf(edtSoLuongSP.getText().toString()),spnPhanLoai.getSelectedItemPosition());
                                databaseReference.child("SanPham").child(sSPID).setValue(sanPham);
                                Toast.makeText(v.getContext(), "Đăng sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                                spIMG.setImageResource(R.mipmap.anh_trang);
                                edtGiaSP.setText("");
                                edtMoTaSP.setText("");
                                edtSoLuongSP.setText("");
                                edtTenSP.setText("");
                                edtDiaChiDang.setText("");
                                spnDanhMuc.setSelection(0);
                                spnPhanLoai.setSelection(0);
                                IMGCheck = 0;
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Xác nhận đúng thông tin và đăng sản phẩm?").setPositiveButton("Yes", dialog).setNegativeButton("No", dialog).show();

            }
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserCuaHangActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };

    View.OnClickListener clearPostClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            spIMG.setImageResource(R.mipmap.anh_trang);
            edtGiaSP.setText("");
            edtMoTaSP.setText("");
            edtSoLuongSP.setText("");
            edtTenSP.setText("");
            spnDanhMuc.setSelection(0);
            spnPhanLoai.setSelection(0);
            IMGCheck = 0;
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

    View.OnClickListener chooseGalleryClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PICK_IMAGE = 1;
            Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(PICK_IMAGE != 123){
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                spIMG.setImageURI(imageUri);
                IMGCheck = 1;
            }
            PICK_IMAGE = 123;
        }
        if(CAMERA_IMAGE != 123){
            if(requestCode == CAMERA_IMAGE && resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                spIMG.setImageBitmap(bitmap);
                IMGCheck = 1;
            }
            CAMERA_IMAGE = 123;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}