package com.example.oldstuffmarket;

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

import com.example.oldstuffmarket.data_models.Commission;
import com.example.oldstuffmarket.data_models.UserData;
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

public class RegistrationActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private TextView txtRegistryReturn;
    private EditText edtUserName, edtFullName, edtSDT, edtDiaChi, edtPass, edtPassConfirm;
    private Spinner spnGender;
    private Button btnRegistry;
    private String UserName = "", FullName = "", SDT = "", DiaChi = "", Pass = "", PassConfirm = "", Gender = "";
    private int iPermission = 1;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<UserData> userList;
    private static final String USERNAME_PATTERN = "^[a-z0-9]{3,8}$";
    private Pattern pattern;
    private int userCommission;
    //test//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.registration_layout);

        txtRegistryReturn = findViewById(R.id.txtRegistryReturn);
        spnGender = findViewById(R.id.spnGender);
        edtUserName = findViewById(R.id.edtUserName);
        edtFullName = findViewById(R.id.edtFullName);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        edtPass = findViewById(R.id.edtPassword);
        edtPassConfirm = findViewById(R.id.edtPasswordConfirm);
        btnRegistry = findViewById(R.id.btnRegistry);

        userList = new ArrayList<UserData>();

        mDatabase.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    userList.add(dataSnapshot.getValue(UserData.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnRegistry.setOnClickListener(OnClick);

        txtRegistryReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.child("Commission").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(Commission.class).getId().equals("-MKyZZdaQ3ucidlxPkUV")){
                    userCommission = snapshot.getValue(Commission.class).getUserCommission();
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

    public boolean userNameValidate(String username) {
        pattern = Pattern.compile(USERNAME_PATTERN);
        return pattern.matcher(username).matches();
    }

    public boolean sdtCheck(ArrayList<UserData> userList, String sSDT){
        for(UserData user : userList){
            if(user.getsSdt().equals(sSDT)){
                return false;
            }
        }
        return true;
    }

    public boolean UserNameCheck(ArrayList<UserData> userList, String sUserName){
        for(UserData user : userList){
            if(user.getsUserName().equals(sUserName)){
                return false;
            }
        }
        return true;
    }

    View.OnClickListener OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserName = edtUserName.getText().toString();
            FullName = edtFullName.getText().toString();
            SDT = edtSDT.getText().toString();
            DiaChi = edtDiaChi.getText().toString();
            Pass = edtPass.getText().toString();
            PassConfirm = edtPassConfirm.getText().toString();
            Gender = spnGender.getSelectedItem().toString();
            if(UserName.isEmpty()){
                edtUserName.setError("Bạn chưa nhập user name!");
            }
            else if(UserNameCheck(userList,UserName) == false){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Username đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(userNameValidate(UserName) == false){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("User name phải có từ 3-5 ký tự a-z và số từ 0-9!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(FullName.isEmpty()){
                edtFullName.setError("Bạn chưa nhập họ tên!");
            }
            else if(SDT.isEmpty()) {
                edtSDT.setError("Bạn chưa nhập số điện thoại!");
            }
            else if(SDT.length() > 11 || SDT.length() < 10){
                edtSDT.setError("Bạn nhập sai số điện thoại!");
            }
            else if(sdtCheck(userList,edtSDT.getText().toString()) == false){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số điện thoại đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(DiaChi.isEmpty()){
                edtDiaChi.setError("Bạn chưa nhập địa chỉ!");
            }
            else if(Pass.isEmpty()){
                edtPass.setError("Bạn chưa nhập mật khẩu!");
            }
            else if(Pass.length() < 6){
                edtPass.setError("Mật khẩu quá ngắn!");
            }
            else if(PassConfirm.isEmpty()){
                edtPassConfirm.setError("Bạn chưa xác nhận mật khẩu!");
            }
            else if(!Pass.equals(PassConfirm)){
                edtPassConfirm.setError("Mật khâu xác nhận không chính xác!");
            }
            else{
                Toast.makeText(RegistrationActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                UserData user = new UserData();
                CreateUser(UserName, FullName, SDT, Gender, DiaChi, Pass, iPermission);
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                finish();
                startActivity(intent);
            }
        }
    };
    private void CreateUser(String sUserName, String sFullName, String sSdt, String sGioiTinh, String sDiaChi, String sPassword, int iPermission){

        String sKey = mDatabase.push().getKey();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        UserData user = new UserData(sUserName,"", sFullName, sSdt, sGioiTinh, sDiaChi, sPassword, "",sKey, dateFormat.format(date), iPermission, userCommission, 0, 0, 0, 0, 0);

        mDatabase.child("User").child(sKey).setValue(user);
    }
}