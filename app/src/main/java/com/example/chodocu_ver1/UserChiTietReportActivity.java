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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.data_models.KhoaUser;
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

public class UserChiTietReportActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Intent intent;
    private ImageView imgUser, imgUser1;
    private TextView txtFullName, txtSDT, txtDiaChi, txtDate, txtReport, txtFullName1, txtSDT1, txtDiaChi1, txtDate1;
    private Button btnLock, btnBack, btnLock1, btnHistory1, btnHistory;
    private String userName, reportID, userID, sUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.user_chitiet_report);

        imgUser1 = (ImageView) findViewById(R.id.imgUser1);
        imgUser = (ImageView) findViewById(R.id.imgUser);
        txtFullName = (TextView) findViewById(R.id.txtFullName);
        txtSDT = (TextView) findViewById(R.id.txtSDT);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtReport = (TextView) findViewById(R.id.txtReport);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnLock = (Button) findViewById(R.id.btnLock);
        txtFullName1 = (TextView) findViewById(R.id.txtFullName1);
        txtSDT1 = (TextView) findViewById(R.id.txtSDT1);
        txtDiaChi1 = (TextView) findViewById(R.id.txtDiaChi1);
        txtDate1 = (TextView) findViewById(R.id.txtDate1);
        btnLock1 = (Button) findViewById(R.id.btnLock1);
        btnHistory1 = (Button) findViewById(R.id.btnHistory1);
        btnHistory = (Button) findViewById(R.id.btnHistory);

        btnBack.setOnClickListener(backClick);
        btnLock.setOnClickListener(lockClick);
        btnLock1.setOnClickListener(lockClick1);
        btnHistory.setOnClickListener(historyUserbibaoCaoClick);
        btnHistory1.setOnClickListener(historyUserbaoCaoClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("UserName");
            reportID = getIntent().getExtras().getString("ReportID");
            userID = getIntent().getExtras().getString("UserID");
            sUserID = getIntent().getExtras().getString("sUserID");
            databaseReference.child("Report").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(UserReport.class).getReportID().equals(reportID)) {
                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if (snapshot.getValue(UserData.class).getUserID().equals(userID)) {
                                    storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Glide.with(UserChiTietReportActivity.this).load(uri).into(imgUser);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(UserChiTietReportActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    txtFullName.setText(snapshot.getValue(UserData.class).getHoTen());
                                    txtSDT.setText(snapshot.getValue(UserData.class).getSoDienThoai());
                                    txtDiaChi.setText(snapshot.getValue(UserData.class).getDiaChi());
                                    txtDate.setText(snapshot.getValue(UserData.class).getNgayThamGia());
                                }
                                if (snapshot.getValue(UserData.class).getUserID().equals(sUserID)) {
                                    storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Glide.with(UserChiTietReportActivity.this).load(uri).into(imgUser1);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(UserChiTietReportActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    txtFullName1.setText(snapshot.getValue(UserData.class).getHoTen());
                                    txtSDT1.setText(snapshot.getValue(UserData.class).getSoDienThoai());
                                    txtDiaChi1.setText(snapshot.getValue(UserData.class).getDiaChi());
                                    txtDate1.setText(snapshot.getValue(UserData.class).getNgayThamGia());
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
                        txtReport.setText(snapshot.getValue(UserReport.class).getMoTaReport());
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

    View.OnClickListener historyUserbaoCaoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), HistoryUserbaocaoActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("sUserID", sUserID);
            intent.putExtra("ReportID", reportID);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };

    View.OnClickListener historyUserbibaoCaoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), LichSu_User_BiBaoCaoActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.putExtra("ReportID", reportID);
            intent.putExtra("sUserID", sUserID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
    View.OnClickListener lockClick1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("Report").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.getValue(UserReport.class).getUserTaoReportID().equals(sUserID)) {
                                        databaseReference.child("Report").child(snapshot.getKey()).removeValue();

                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if (snapshot.getValue(UserData.class).getUserID().equals(sUserID)) {
                                                    String lockID = databaseReference.push().getKey();
                                                    KhoaUser lockUser = new KhoaUser(lockID, sUserID);
                                                    databaseReference.child("LockUser").child(lockID).setValue(lockUser);
                                                    databaseReference.child("User").child(snapshot.getKey()).child("tinhTrang").setValue(-1);
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
                            intent = new Intent(v.getContext(), Report_User_Admin_Activity.class);
                            intent.putExtra("UserName", userName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            finish();
                            startActivity(intent);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Bạn muốn chắc chắn muốn khóa User báo cáo?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
        }
    };
    View.OnClickListener lockClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("Report").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.getValue(UserReport.class).getDoiTuongReportID().equals(userID)) {
                                        databaseReference.child("Report").child(snapshot.getKey()).removeValue();

                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if (snapshot.getValue(UserData.class).getUserID().equals(userID)) {
                                                    String lockID = databaseReference.push().getKey();
                                                    KhoaUser lockUser = new KhoaUser(lockID, userID);
                                                    databaseReference.child("LockUser").child(lockID).setValue(lockUser);
                                                    databaseReference.child("User").child(snapshot.getKey()).child("tinhTrang").setValue(-1);
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
                            intent = new Intent(v.getContext(), Report_User_Admin_Activity.class);
                            intent.putExtra("UserName", userName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            finish();
                            startActivity(intent);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Bạn muốn chắc chắn muốn khóa User bị báo cáo?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
        }
    };
    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), Report_User_Admin_Activity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}