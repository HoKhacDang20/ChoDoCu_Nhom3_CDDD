package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.data_models.UserData;
import com.example.chodocu_ver1.data_models.UserReport;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ReportUserActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Intent intent;
    private ImageView imgUser;
    private TextView txtFullName, txtSDT, txtDiaChi;
    private EditText edtReport;
    private Button btnReportUser, btnHome, btnBack;
    private String sUserID = UserMainActivity.sUserID;
    private String userName, userID, productID, navigateTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.report_user);

        imgUser = (ImageView) findViewById(R.id.imgUser);
        txtFullName = (TextView) findViewById(R.id.txtFullName);
        txtSDT = (TextView) findViewById(R.id.txtSDT);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        edtReport = (EditText) findViewById(R.id.edtReport);
        btnReportUser = (Button) findViewById(R.id.btnReportUser);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnHome = (Button) findViewById(R.id.btnHome);

        btnBack.setOnClickListener(backClick);
        btnHome.setOnClickListener(homeClick);
        btnReportUser.setOnClickListener(reportUserClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
            productID = getIntent().getExtras().getString("ProductID");
            navigateTo = getIntent().getExtras().getString("NavigateTo");
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(UserData.class).getUserID().equals(userID)) {
                        storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ReportUserActivity.this).load(uri).into(imgUser);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                        txtFullName.setText(snapshot.getValue(UserData.class).getHoTen());
                        txtDiaChi.setText(snapshot.getValue(UserData.class).getDiaChi());
                        txtSDT.setText(snapshot.getValue(UserData.class).getSoDienThoai());

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
    }

    View.OnClickListener reportUserClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (edtReport.getText().toString().isEmpty()) {
                edtReport.setError("Không được để trống!");
            }
            else {
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if (snapshot.getValue(UserData.class).getUserID().equals(userID)) {
                                            String reportID = databaseReference.push().getKey();
                                            UserReport userReport = new UserReport(reportID, userID, sUserID, edtReport.getText().toString(), 0,false);
                                            databaseReference.child("Report").child(reportID).setValue(userReport);
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
                                finish();
                                intent = new Intent(v.getContext(), UserMainActivity.class);
                                intent.putExtra("UserName", userName);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chắn chắn muốn báo cáo User!").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(navigateTo.equals("Seller")){
                finish();
                intent = new Intent(v.getContext(), NguoiBan_MainActivity.class);
                intent.putExtra("UserName", userName);
                intent.putExtra("UserID", userID);
                intent.putExtra("ProductID", productID);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
            else if(navigateTo.equals("Shop")){
                finish();
                intent = new Intent(v.getContext(), CuaHang_MainActivity.class);
                intent.putExtra("UserName", userName);
                intent.putExtra("UserID", userID);
                intent.putExtra("ProductID", productID);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }
    };

    View.OnClickListener homeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), UserMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
}