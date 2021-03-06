package com.example.chodocu_nhom3_cddd.ui.dashboard;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.chodocu_nhom3_cddd.R;
import com.example.chodocu_nhom3_cddd.UserMainActivity;
import com.example.chodocu_nhom3_cddd.data_models.DanhMuc;
import com.example.chodocu_nhom3_cddd.data_models.SanPham;
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


public class NewPostFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ArrayList<DanhMuc> danhMucDataArrayList = UserMainActivity.danhMucDataArrayList;
    private ArrayList<String> danhMucList;
    private ImageView spIMG;
    private String sUserName = UserMainActivity.sUserName, sUserID = UserMainActivity.sUserID, sShopID = UserMainActivity.sShopID;
    private Button btnChooseFromGallery, btnOpenCamera, btnPost, btnClearPost;
    private EditText edtTenSP, edtGiaSP, edtSoLuongSP, edtMoTaSP, edtDiaChiDang;
    private Spinner spnPhanLoai, spnDanhMuc;
    private int PICK_IMAGE = 123;
    private int CAMERA_IMAGE = 123;
    private int IMGCheck = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        View view = inflater.inflate(R.layout.fragment_dang_bai, container, false);


        spIMG = (ImageView)  view.findViewById(R.id.spIMG);
        btnChooseFromGallery = (Button) view.findViewById(R.id.btnChooseFromGallery);
        btnOpenCamera = (Button) view.findViewById(R.id.btnOpenCamera);
        btnPost = (Button) view.findViewById(R.id.btnPost);
        btnClearPost = (Button) view.findViewById(R.id.btnClearPost);
        edtTenSP = (EditText) view.findViewById(R.id.edtTenSP);
        edtGiaSP = (EditText) view.findViewById(R.id.edtGiaSP);
        edtSoLuongSP = (EditText) view.findViewById(R.id.edtSoLuongSP);
        edtMoTaSP = (EditText) view.findViewById(R.id.edtMoTaSP);
        edtDiaChiDang = (EditText) view.findViewById(R.id.edtDiaChiDang);
        spnPhanLoai = (Spinner) view.findViewById(R.id.spnPhanLoai);
        spnDanhMuc = (Spinner) view.findViewById(R.id.spnDanhMuc);

        danhMucList = new ArrayList<>();

        for(DanhMuc danhMucData : danhMucDataArrayList){
            danhMucList.add(danhMucData.getTenDanhMuc());
        }
        danhMucList.add("Kh??c");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, danhMucList);
        spnDanhMuc.setAdapter(arrayAdapter);

        btnChooseFromGallery.setOnClickListener(chooseGalleryClick);
        btnOpenCamera .setOnClickListener(openCameraClick);
        btnClearPost.setOnClickListener(clearPostClick);
        btnPost.setOnClickListener(postClick);


        return view;
    }

    View.OnClickListener postClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity().getApplicationContext(), sShopID, Toast.LENGTH_SHORT).show();

            if(IMGCheck == 0){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("B???n ch??a ch???n h??nh ???nh cho s???n ph???m mu???n ????ng!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtTenSP.getText().toString().isEmpty()){
                edtTenSP.setError("B???n ch??a nh???p t??n s???n ph???m!");
            }
            else if(edtDiaChiDang.getText().toString().isEmpty()){
                edtDiaChiDang.setError("B???n ch??a nh???p ?????a ch??? ????ng s???n ph???m!");
            }
            else if(edtGiaSP.getText().toString().isEmpty()){
                edtGiaSP.setError("B???n ch??a nh???p gi?? s???n ph???m!");
            }
            else if(edtSoLuongSP.getText().toString().isEmpty()){
                edtSoLuongSP.setError("B???n ch??a nh???p s??? l?????ng s???n ph???m!");
            }
            else if(Integer.parseInt(edtSoLuongSP.getText().toString()) < 1){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("S??? l?????ng s???n ph???m ph???i > 0").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtMoTaSP.getText().toString().isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("B???n ch??a nh???p m?? t??? s???n ph???m!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
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

                                // th??? hi???n m???t b???c ???nh trong h??? ??i???u h??nh Android, ch???a c??c th??ng tin v?? c??c ph????ng th???c c?? b???n ????? c?? th??? l??m vi???c ???????c v???i b???c ???nh nh?? ?????c, ghi c??c ??i???m ???nh, l???y th??ng tin k??ch th?????c, ???.
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
                                        Toast.makeText(v.getContext(), "Th??m h??nh th???t b???i!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    }
                                });
                                String sSPID = databaseReference.push().getKey();
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                                Date date = new Date();
                                SanPham sanPham = new SanPham(sSPID, sUserID, sShopID, edtTenSP.getText().toString(),sKey,edtMoTaSP.getText().toString(),spnDanhMuc.getSelectedItem().toString(), dateFormat.format(date), edtDiaChiDang.getText().toString(), Long.valueOf(edtGiaSP.getText().toString()),
                                        Integer.valueOf(edtSoLuongSP.getText().toString()),spnPhanLoai.getSelectedItemPosition());
                                databaseReference.child("SanPham").child(sSPID).setValue(sanPham);
                                Toast.makeText(v.getContext(), "????ng s???n ph???m th??nh c??ng!", Toast.LENGTH_SHORT).show();
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
                alert.setMessage("X??c nh???n ????ng th??ng tin v?? ????ng s???n ph???m?").setPositiveButton("Yes", dialog).setNegativeButton("No", dialog).show();

            }
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