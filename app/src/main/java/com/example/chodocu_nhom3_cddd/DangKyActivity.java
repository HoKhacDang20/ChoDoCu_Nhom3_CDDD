package com.example.chodocu_nhom3_cddd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class DangKyActivity extends AppCompatActivity {

    private TextView txtRegistryReturn;//link quay lại trang đăng nhập

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_dang_ky);

        txtRegistryReturn = findViewById(R.id.txtRegistryReturn);

        txtRegistryReturn.setOnClickListener(new View.OnClickListener() {//bấm vào link trở lại trang đăng nhập
            @Override
            public void onClick(View v) {
                finish();// đóng trang hiện tại
            }
        });

    }
}