package com.example.chodocu_nhom3_cddd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chodocu_nhom3_cddd.data_models.HoaHong;
import com.example.chodocu_nhom3_cddd.data_models.UserData;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class DangKyActivity extends AppCompatActivity {

    private DatabaseReference databaseReference; //tạo liên kết firebase
    private TextView txtRegistryReturn;//link quay lại trang đăng nhập
    private EditText edtUserName, edtFullName, edtSDT, edtDiaChi, edtPass, edtPassConfirm, edtSoCMND;// các trường nhập thông tin tài khoản
    private Spinner spnGender;//Spinner giới tính user
    private Button btnRegistry;//nút đăng ký
    private String UserName = "", FullName = "", SDT = "", DiaChi = "", Pass = "", PassConfirm = "", Gender = "", SoCMND = "";//các biến lưu thông tin như user name, full name,...
    private int iPermission = 1;//permission của user mặc định là 1
    private ArrayList<UserData> userList;//khai báo danh sách user
    private ArrayList<UserData> blackList;
    private static final String USERNAME_PATTERN = "^[a-z0-9]{3,8}$";//kiểm tra user name nhập vào
    private Pattern pattern;
    private int userCommission;// hoa hồng của user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_dang_ky);

        txtRegistryReturn = findViewById(R.id.txtRegistryReturn);
        spnGender = findViewById(R.id.spnGender);
        edtUserName = findViewById(R.id.edtUserName);
        edtFullName = findViewById(R.id.edtFullName);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtPass = findViewById(R.id.edtPassword);
        edtPassConfirm = findViewById(R.id.edtPasswordConfirm);
        edtSoCMND = findViewById(R.id.edtSoCMND);
        btnRegistry = findViewById(R.id.btnRegistry);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        userList = new ArrayList<UserData>();//tạo mới user list
        blackList = new ArrayList<UserData>();

        databaseReference.child("User").addValueEventListener(new ValueEventListener() {// lọc dữ liệu trong mục User
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
        databaseReference.child("BlackList").addValueEventListener(new ValueEventListener() {// lọc dữ liệu trong mục User
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
        databaseReference.child("Commission").addChildEventListener(new ChildEventListener() {// lọc dữ liệu trong mục commission
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(HoaHong.class).getId().equals("-MKyZZdaQ3ucidlxPkUV")){
                    userCommission = snapshot.getValue(HoaHong.class).getUserHoaHong();// đưa thông tin commission vào danh sách
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
            if(UserName.isEmpty()){//kiểm tra user có nhập hay chưa
                edtUserName.setError("Bạn chưa nhập user name!");
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
            else if(UserNameCheck(userList,UserName) == false){//kiểm tra user có tồn tại hay không
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Username đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
            else if(sdtCheck(userList,edtSDT.getText().toString()) == false){//kiểm tra SDT đã tồn tại chưa
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số điện thoại đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
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
                                Toast.makeText(DangKyActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();// thông báo tạo tài khoản thành công
                                UserData user = new UserData();
                                CreateUser(UserName, FullName, SDT, Gender, DiaChi, Pass,SoCMND, iPermission);//chạy hàm tạo mới user và đưa user vào firebase
                                Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);// quay trở lại trang đăng nhập
                                finish();// đóng trang
                                startActivity(intent);
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
    private void CreateUser(String sUserName, String sFullName, String sSdt, String sGioiTinh, String sDiaChi, String sPassword,String soCMND, int iPermission){// hàm tạo mới user và đưa vào firebase

        String sKey = databaseReference.push().getKey();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");// định dạng ngày
        Date date = new Date();// lấy ngày hiện tại trong hệ thống

        UserData user = new UserData(sUserName,"", sFullName, sSdt, sGioiTinh, sDiaChi, sPassword, "",sKey, dateFormat.format(date),soCMND, iPermission, userCommission, 0, 0, 0, 0, 0);// tạo mới user

        databaseReference.child("User").child(sKey).setValue(user);//đưa user mới vào firebase
    }



}