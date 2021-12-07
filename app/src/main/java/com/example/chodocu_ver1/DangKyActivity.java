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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chodocu_ver1.data_models.HoaHong;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class DangKyActivity extends AppCompatActivity {
    //private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();//tạo liên kết firebase
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    private TextView txtRegistryReturn;//link quay lại trang đăng nhập
    private EditText edtEmail, edtUserName, edtFullName, edtSDT, edtDiaChi, edtPass, edtPassConfirm, edtSoCMND;// các trường nhập thông tin tài khoản
    private Spinner spnGender;//Spinner giới tính user
    private Button btnRegistry;//nút đăng ký
    private String UserName = "", FullName = "", SDT = "", DiaChi = "", Pass = "", Email = "", PassConfirm = "", Gender = "", SoCMND = "";//các biến lưu thông tin như user name, full name,...
    private int iPermission = 1;//permission của user mặc định là 1
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ArrayList<UserData> userList;//khai báo danh sách user
    private ArrayList<UserData> blackList;
    private static final String USERNAME_PATTERN = "^[a-z0-9]{3,8}$";//kiểm tra user name nhập vào
    private Pattern pattern;
    private int userCommission;// hoa hồng của user
    private ImageView imgCMNDMatTruoc, imgCMNDMatSau;

    private int PICK_IMAGE = 123;
    private int CAMERA_IMAGE = 123;

    private int IMGCheck = 0;

    private Button btnChooseFromGallery, btnOpenCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.dangky_layout);


        //CMND:
        imgCMNDMatTruoc = (ImageView) findViewById(R.id.imgCMNDMatTruoc);

        imgCMNDMatSau = (ImageView) findViewById(R.id.imgCMNDMatTruoc);

        btnChooseFromGallery = (Button) findViewById(R.id.btnChooseFromGallery);
        btnOpenCamera = (Button) findViewById(R.id.btnOpenCamera);


        btnChooseFromGallery.setOnClickListener(chooseGalleryClick);
        btnOpenCamera.setOnClickListener(openCameraClick);


        txtRegistryReturn = findViewById(R.id.txtRegistryReturn);
        spnGender = findViewById(R.id.spnGender);
        edtUserName = findViewById(R.id.edtUserName);
        edtFullName = findViewById(R.id.edtFullName);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtPass = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassConfirm = findViewById(R.id.edtPasswordConfirm);
        edtSoCMND = findViewById(R.id.edtSoCMND);
        btnRegistry = findViewById(R.id.btnRegistry);

        userList = new ArrayList<UserData>();//tạo mới user list
        blackList = new ArrayList<UserData>();

        mDatabase.child("User").addValueEventListener(new ValueEventListener() {// lọc dữ liệu trong mục User
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    userList.add(dataSnapshot.getValue(UserData.class));// thêm dữ liệu vào user list
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.child("BlackList").addValueEventListener(new ValueEventListener() {// lọc dữ liệu trong mục User
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    blackList.add(dataSnapshot.getValue(UserData.class));// thêm dữ liệu vào user list
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnRegistry.setOnClickListener(OnClick);//sự kiện bấm nút đăng ký

        txtRegistryReturn.setOnClickListener(new View.OnClickListener() {//bấm vào link trở lại trang đăng nhập
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DangNhapActivity.class);//quay trở lại trang đăng nhập
                finish();// đóng trang hiện tại
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatabase.child("Commission").addChildEventListener(new ChildEventListener() {// lọc dữ liệu trong mục commission
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(HoaHong.class).getId().equals("-MKyZZdaQ3ucidlxPkUV")){
                    userCommission = snapshot.getValue(HoaHong.class).getUserCommission();// đưa thông tin commission vào danh sách
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

    public boolean userNameValidate(String username) {//kiểm tra user name nhập vào có hợp lệ (ít nhất từ 3-8 ký tự bào gồm các chữ cái a-z hoặc các số từ 0-9)
        pattern = Pattern.compile(USERNAME_PATTERN);
        return pattern.matcher(username).matches();
    }

    public boolean sdtCheck(ArrayList<UserData> userList, String sSDT){// kiểm tra sdt có tồn tại trên csdl hay chưa
        for(UserData user : userList){
            if(user.getSoDienThoai().equals(sSDT)){
                return false;
            }
        }
        return true;
    }

//    public boolean emailCheck(ArrayList<UserData> userList, String sEmail){// kiểm tra sdt có tồn tại trên csdl hay chưa
//        for(UserData user : userList){
//            if(user.getEmail().equals(sEmail)){
//                return false;
//            }
//        }
//        return true;
//    }

    public boolean SoCMNDCheck(ArrayList<UserData> userList, String soCMND){// kiểm tra sdt có tồn tại trên csdl hay chưa
        for(UserData user : userList){
            if(user.getSoCMND().equals(soCMND)){
                return false;
            }
        }
        return true;
    }

    public boolean UserNameCheck(ArrayList<UserData> userList, String sUserName){// kiểm tra user name có tồn tại trên csdl hay chưa
        for(UserData user : userList){
            if(user.getUserName().equals(sUserName)){
                return false;
            }
        }
        return true;
    }

    public boolean blackListSdtCheck(ArrayList<UserData> blackList, String sSDT){// kiểm tra sdt có tồn tại trên csdl hay chưa
        for(UserData user : blackList){
            if(user.getSoDienThoai().equals(sSDT)){
                return false;
            }
        }
        return true;
    }

    public boolean blackListNameCheck(ArrayList<UserData> blackList, String sUserName){// kiểm tra user name có tồn tại trên csdl hay chưa
        for(UserData user : blackList){
            if(user.getUserName().equals(sUserName)){
                return false;
            }
        }
        return true;
    }

    View.OnClickListener OnClick = new View.OnClickListener() {// tạo sự kiện click cho nút đăng ký
        @Override
        public void onClick(View v) {
            UserName = edtUserName.getText().toString();
            FullName = edtFullName.getText().toString();
            SoCMND = edtSoCMND.getText().toString();
            SDT = edtSDT.getText().toString();
            DiaChi = edtDiaChi.getText().toString();
            Pass = edtPass.getText().toString();
            PassConfirm = edtPassConfirm.getText().toString();
            Gender = spnGender.getSelectedItem().toString();
            Email = edtEmail.getText().toString();


//            mAuth.createUserWithEmailAndPassword("nhanvien@gmail.com","0123456").addOnCompleteListener(DangKyActivity.this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                    if (task.isSuccessful()) {
//                        // Sign in success, update UI with the signed-in user's information
//                        FirebaseUser user = mAuth.getCurrentUser();
//                        Toast.makeText(DangKyActivity.this, "Đăng ký tài khoản thành công",
//                                Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Toast.makeText(DangKyActivity.this, "Lỗi email chưa chính xác",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });

            if(UserName.isEmpty()){//kiểm tra user có nhập hay chưa
                edtUserName.setError("Bạn chưa nhập user name!");
            }
            else if(IMGCheck == 0){
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn hình ảnh CMND mặt trước!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(SoCMND.isEmpty()){
                edtSoCMND.setError("Bạn chưa nhập CMND!");
            }
            else if(SoCMNDCheck(userList,edtSoCMND.getText().toString()) == false){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số CMND đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
//            else if(emailCheck(userList,edtEmail.getText().toString()) == false){
//                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
//                alert.setMessage("Số CMND đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
//            }
            else if(UserNameCheck(userList,Email) == false){//kiểm tra user có tồn tại hay không
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Email đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(userNameValidate(UserName) == false){// kiểm tra user name có hợp lệ chưa
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("User name phải có từ 3-5 ký tự a-z và số từ 0-9!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if (blackListNameCheck(blackList, UserName) == false) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Username đã tồn tại trong danh sách đen!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(FullName.isEmpty()){//kiểm tra họ tên có nhập hay chưa
                edtFullName.setError("Bạn chưa nhập họ tên!");
            }
            else if(SDT.isEmpty()) {//kiểm tra sdt có nhập hay chưa
                edtSDT.setError("Bạn chưa nhập số điện thoại!");
            }
            else if(SDT.length() > 11 || SDT.length() < 10){//kiểm tra SDT có hợp lệ không
                edtSDT.setError("Bạn nhập sai số điện thoại!");
            }
//            else if(Email.isEmpty()){//kiểm tra họ tên có nhập hay chưa
//                edtEmail.setError("Bạn chưa nhập Email!");
//            }
            /*else if(sdtCheck(userList,edtSDT.getText().toString()) == false){//kiểm tra SDT đã tồn tại chưa
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số điện thoại đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }*/
            else if (blackListSdtCheck(blackList, edtSDT.getText().toString()) == false) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số điện thoại đã tồn tại trong danh sách đen!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(DiaChi.isEmpty()){//kiểm tra địa chỉ có nhập hay chưa
                edtDiaChi.setError("Bạn chưa nhập địa chỉ!");
            }
            else if(Pass.isEmpty()){//kiểm tra password có nhập hay chưa
                edtPass.setError("Bạn chưa nhập mật khẩu!");
            }
            else if(Pass.length() < 6){//kiểm tra độ dài mật khẩu
                edtPass.setError("Mật khẩu quá ngắn!");
            }
            else if(PassConfirm.isEmpty()){//kiểm tra password confrim có nhập hay chưa
                edtPassConfirm.setError("Bạn chưa xác nhận mật khẩu!");
            }
            else if(!Pass.equals(PassConfirm)){//kiểm tra password == password confirm ?
                edtPassConfirm.setError("Mật khâu xác nhận không chính xác!");
            }
            else{
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                CreateUser(UserName, FullName, SDT, Email, Gender, DiaChi, Pass, SoCMND, iPermission);

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Xác nhận đúng thông tin đăng ký tài khoản?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();

            }

        }
    };
    private void CreateUser(String sUserName, String sFullName, String sSdt, String email, String sGioiTinh, String sDiaChi, String sPassword,String soCMND, int iPermission){// hàm tạo mới user và đưa vào firebase

        mAuth.createUserWithEmailAndPassword(email,sPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    FirebaseUser user = mAuth.getCurrentUser();
                    //Toast.makeText(DangKyActivity.this, "Đăng ký tài khoản thành công",Toast.LENGTH_SHORT).show();
                    String userID = user.getUid();

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");// định dạng ngày
                    Date date = new Date();// lấy ngày hiện tại trong hệ thống

                    String sKey = databaseReference.push().getKey();
                    final StorageReference mountainsRef = storageReference.child(sKey + ".png");

                    // thể hiện một bức ảnh trong hệ điều hành Android, chứa các thông tin và các phương thức cơ bản để có thể làm việc được với bức ảnh như đọc, ghi các điểm ảnh, lấy thông tin kích thước, ….
                    imgCMNDMatTruoc.setDrawingCacheEnabled(true);
                    imgCMNDMatTruoc.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) imgCMNDMatTruoc.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();
                    final UploadTask uploadTask = mountainsRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //Toast.makeText(v.getContext(), "Thêm hình thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        }
                    });

                    UserData userData = new UserData(sUserName,"", sFullName, sSdt, sGioiTinh, sDiaChi, sPassword, "",userID, dateFormat.format(date),soCMND, email, sKey, iPermission, userCommission, 0, 0, 0, 0, 0);// tạo mới user
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("User").child(userID).setValue(userData);
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
//                    Toast.makeText(DangKyActivity.this, "Lỗi email chưa chính xác",
//                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
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
                imgCMNDMatTruoc.setImageURI(imageUri);

                IMGCheck = 1;
            }
            PICK_IMAGE = 123;
        }
        if(CAMERA_IMAGE != 123){
            if(requestCode == CAMERA_IMAGE && resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgCMNDMatTruoc.setImageBitmap(bitmap);

                IMGCheck = 1;
            }
            CAMERA_IMAGE = 123;
        }



        super.onActivityResult(requestCode, resultCode, data);
    }
}