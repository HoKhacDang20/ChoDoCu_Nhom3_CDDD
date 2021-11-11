package com.example.chodocu_nhom3_cddd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DangNhapActivity extends AppCompatActivity {

    private TextView txtRegistry; //link đăng ký tài khoản mới

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.dangnhap_layout);

        txtRegistry = findViewById(R.id.txtRegistry);

        txtRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Đăng ký tài khoản!",Toast.LENGTH_SHORT).show(); //hiện thông báo chuyển trang
                Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class); //chuyển sang trang khác
                startActivity(intent);
            }
        });

    }




}