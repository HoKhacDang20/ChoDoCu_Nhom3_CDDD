package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.chodocu_ver1.adapter.KhoaUserAdapter;
import com.example.chodocu_ver1.data_models.KhoaUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DanhSachUserActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private GridView gridUser;
    private Button btnBack, btnRefresh;
    private Intent intent;
    private String userName;
    private ArrayList<KhoaUser> lockUserArrayList;
    private KhoaUserAdapter lockUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danh_sach_user);

        gridUser = (GridView) findViewById(R.id.gridUser);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        lockUserArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);
        btnRefresh.setOnClickListener(clearClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("UserName");
            lockUserArrayList.clear();
            databaseReference.child("LockUser").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    lockUserArrayList.add(snapshot.getValue(KhoaUser.class));
                    userLoad();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    KhoaUser lockUserRemove = null;
                    for(KhoaUser lockUser : lockUserArrayList){
                        if(lockUser.getUserID().equals(snapshot.getValue(KhoaUser.class).getUserID())){
                            lockUserRemove = lockUser;
                        }
                    }
                    if(lockUserRemove != null){
                        lockUserArrayList.remove(lockUserRemove);
                    }
                    userLoad();
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
            handler.postDelayed(new Runnable() {
                public void run() {

                }
            }, delay);
        }
    }

    View.OnClickListener clearClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            lockUserArrayList.clear();

            databaseReference.child("LockUser").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    lockUserArrayList.add(snapshot.getValue(KhoaUser.class));
                    userLoad();
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
            handler.postDelayed(new Runnable() {
                public void run() {
                    userLoad();

                }
            }, delay);
        }
    };

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

    public void userLoad() {
        lockUserAdapter = new KhoaUserAdapter(DanhSachUserActivity.this, R.layout.khoa_user_adapter, lockUserArrayList);
        gridUser.setAdapter(lockUserAdapter);
    }
}