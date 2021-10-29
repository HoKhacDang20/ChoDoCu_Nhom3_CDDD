package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oldstuffmarket.data_models.Commission;
import com.example.oldstuffmarket.data_models.TaiKhoanNH;
import com.example.oldstuffmarket.data_models.UserData;
import com.example.oldstuffmarket.ui.dashboard.SettingsFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<UserData> userDataArrayList;
    private TextView txtRegistry;
    private Button btnLogin;
    private EditText edtLoginName, edtLoginPass;
    static public Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.login_layout);

        txtRegistry = findViewById(R.id.txtRegistry);
        btnLogin = findViewById(R.id.btnLogin);
        edtLoginName = findViewById(R.id.edtLoginName);
        edtLoginPass = findViewById(R.id.edtLoginPass);

        userDataArrayList = new ArrayList<>();

        txtRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Đăng ký tài khoản!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        edtLoginPass.setText("");
        userDataArrayList.clear();

        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
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

        btnLogin.setOnClickListener(LoginClick);
    }

    public int loginCheck(ArrayList<UserData> userDataArrayList, String sUserName, String sPass){
        int iLoginResult = -1;
        for(UserData user : userDataArrayList){
            if(user.getsUserName().equals(sUserName) && user.getsPassword().equals(sPass) && user.getiTinhTrang() == 0){
                iLoginResult = user.getiPermission();
            }
            else if(user.getsUserName().equals(sUserName) && user.getsPassword().equals(sPass) && user.getiTinhTrang() == -2){
                iLoginResult = user.getiTinhTrang();
            }
        }
        return iLoginResult;
    }

    View.OnClickListener LoginClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int iLogin = loginCheck(userDataArrayList, edtLoginName.getText().toString(), edtLoginPass.getText().toString());
            if(edtLoginName.getText().toString().isEmpty()){
                edtLoginName.setError("Bạn chưa nhập user name!");
            }
            else if(edtLoginPass.getText().toString().isEmpty()){
                edtLoginPass.setError("Bạn chưa nhập mật khẩu!");
            }
            else if(iLogin == 0){
                intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                intent.putExtra("UserName", edtLoginName.getText().toString());
                startActivity(intent);
            }
            else if(iLogin == 1){
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                SettingsFragment settingsFragment = (SettingsFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment);
//                Bundle bundle = new Bundle();
//                bundle.putString("UserName", edtLoginName.getText().toString());

//                settingsFragment.setArguments(bundle);

                intent = new Intent(LoginActivity.this, UserMainActivity.class);
                intent.putExtra("UserName", edtLoginName.getText().toString());
                startActivity(intent);
            }
            else if(iLogin == -2) {
                Toast.makeText(v.getContext(), "Tài khoản của bạn đã bị khóa, hãy liên hệ admin!",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(v.getContext(), "Đăng nhập thất bại!",Toast.LENGTH_SHORT).show();
            }
        }
    };
}