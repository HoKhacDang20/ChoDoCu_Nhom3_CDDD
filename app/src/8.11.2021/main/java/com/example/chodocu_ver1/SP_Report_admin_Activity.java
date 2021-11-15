package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.chodocu_ver1.adapter.SanPhamBiBaoCaoAdapter;
import com.example.chodocu_ver1.data_models.SanPhamReport;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SP_Report_admin_Activity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private GridView gridReportSP;
    private Button btnBack;
    private Intent intent;
    private ArrayList<SanPhamReport> productReportArrayList;
    private String userName;
    private SanPhamBiBaoCaoAdapter sPbibaocaoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sanpham_report_admin);

        productReportArrayList = new ArrayList<>();
        btnBack = (Button) findViewById(R.id.btnBack);
        gridReportSP = (GridView) findViewById(R.id.gridReportSP);

        btnBack.setOnClickListener(backClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("UserName");
            productReportArrayList.clear();
            databaseReference.child("ProductReport").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    productReportArrayList.add(snapshot.getValue(SanPhamReport.class));
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
            final Handler handler = new Handler();
            final int delay = 500; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    sanPhamLoad();
                }
            }, delay);
        }
        gridReportSP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), ChiTiet_SanPham_ReportActivity.class);
                intent.putExtra("ReportID", productReportArrayList.get(position).getReportID());
                intent.putExtra("UserID", productReportArrayList.get(position).getSanPham().getUserID());
                intent.putExtra("UserName", userName);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };

    public void sanPhamLoad(){
        sPbibaocaoAdapter = new SanPhamBiBaoCaoAdapter(SP_Report_admin_Activity.this, R.layout.sanpham_bibaocao_adapter, productReportArrayList);
        gridReportSP.setAdapter(sPbibaocaoAdapter);
    }
}