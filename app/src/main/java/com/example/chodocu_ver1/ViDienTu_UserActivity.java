package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.chodocu_ver1.data_models.UserData;
import com.example.chodocu_ver1.data_models.UserDepositData;
import com.example.chodocu_ver1.ui.vdientu.WalletUserFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ViDienTu_UserActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Intent intent;
    private String sUserName, userID;
    private Button btnNapTien, btnBack, btnTransactionHistory;
    private TextView txtSoDu, txtUserDepositMoneyNotify;
    public static ArrayList<UserData> userDataArrayList;
    public static ArrayList<UserDepositData> userDepositPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.vidientu_user_layout);

        txtSoDu = (TextView) findViewById(R.id.txtSoDu);
        txtUserDepositMoneyNotify = (TextView) findViewById(R.id.txtUserDepositMoneyNotify);
        btnNapTien = (Button) findViewById(R.id.btnNapTien);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnTransactionHistory = (Button) findViewById(R.id.btnTransactionHistory);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getValue(UserData.class).getUserName().equals(sUserName) && snapshot.getValue(UserData.class).getPermission() == 1){
                            Intent intent = new Intent(v.getContext(), UserMainActivity.class);
                            intent.putExtra("UserName", sUserName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            finish();
                            startActivity(intent);
                        }
                        else if(snapshot.getValue(UserData.class).getUserName().equals(sUserName) && snapshot.getValue(UserData.class).getPermission() == 0){
                            Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
                            intent.putExtra("UserName", sUserName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            finish();
                            startActivity(intent);
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
                });*/
            }
        });

        btnNapTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                WalletUserFragment walletFragment = new WalletUserFragment();

                Bundle bundle = new Bundle();

                bundle.putString("UserName", sUserName);
                bundle.putString("UserID", userID);

                walletFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.napTienLayout, walletFragment);
                fragmentTransaction.commit();

            }
        });

        btnTransactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), ViDienTu_LichSu_GiaoDichActivity.class);
                intent.putExtra("UserName", sUserName);
                intent.putExtra("UserID", userID);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDataArrayList = new ArrayList<>();
        userDepositPending = new ArrayList<>();

        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                userDataArrayList.add(snapshot.getValue(UserData.class));
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


        if(getIntent().getExtras() != null){
            sUserName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
            databaseReference.child("UserDepositRequest").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserDepositData.class).getUserID().equals(userID) && snapshot.getValue(UserDepositData.class).getTinhTrang() == 0){
                        userDepositPending.add(snapshot.getValue(UserDepositData.class));
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
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getUserName().equals(sUserName)){
                        txtSoDu.setText(String.valueOf(snapshot.getValue(UserData.class).getMoney()) + "vnđ");
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(userDepositPending.size() != 0){
                        txtUserDepositMoneyNotify.setVisibility(View.VISIBLE);
                        txtUserDepositMoneyNotify.setText(String.valueOf(userDepositPending.size()));
                    }

                }
            }, delay);
        }
    }
}