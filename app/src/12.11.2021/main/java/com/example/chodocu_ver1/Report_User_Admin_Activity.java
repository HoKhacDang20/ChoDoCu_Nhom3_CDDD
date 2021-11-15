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

import com.example.chodocu_ver1.adapter.UserBiBaoCaoAdapter;
import com.example.chodocu_ver1.data_models.UserReport;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Report_User_Admin_Activity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private GridView gridReportUser;
    private Button btnBack;
    private Intent intent;
    private String userName;
    private ArrayList<UserReport> userReportArrayList;
    private UserBiBaoCaoAdapter userbibaocao_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.report_user_admin);

        gridReportUser = (GridView) findViewById(R.id.gridReportUser);
        btnBack = (Button) findViewById(R.id.btnBack);

        userReportArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);

    }
    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("UserName");
            userReportArrayList.clear();
            databaseReference.child("Report").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    userReportArrayList.add(snapshot.getValue(UserReport.class));
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
                    userLoad();
                }
            }, delay);
        }
        gridReportUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), UserChiTietReportActivity.class);
                intent.putExtra("ReportID", userReportArrayList.get(position).getReportID());
                intent.putExtra("UserID", userReportArrayList.get(position).getDoiTuongReportID());
                intent.putExtra("sUserID", userReportArrayList.get(position).getUserTaoReportID());
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

    public void userLoad(){
        userbibaocao_adapter = new UserBiBaoCaoAdapter(Report_User_Admin_Activity.this, R.layout.user_bibaocao_adapter, userReportArrayList);
        gridReportUser.setAdapter(userbibaocao_adapter);
    }
}