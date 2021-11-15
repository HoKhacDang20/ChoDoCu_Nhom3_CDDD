package com.example.chodocu_ver1;

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

import com.example.chodocu_ver1.data_models.CuocHen;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChiTietCuocHenActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private EditText edtTieuDe, edtNgayHen, edtNguoiCanGap, edtChiTietCuocHen;
    private Button btnBack, btnDelete;
    private String userID, userName, appointmentID, nguoiCanGapID, nguoiHenID, moTaCuocHen, tieuDeCuocHen, ngayHen;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_chitiet_cuochen_layout);

        edtTieuDe = (EditText) findViewById(R.id.edtTieuDe);
        edtNgayHen = (EditText) findViewById(R.id.edtNgayHen);
        edtNguoiCanGap = (EditText) findViewById(R.id.edtNguoiCanGap);
        edtChiTietCuocHen = (EditText) findViewById(R.id.edtChiTietCuocHen);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(backClick);
        btnDelete.setOnClickListener(deleteClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){

            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            appointmentID = getIntent().getExtras().getString("AppointmentID");
            nguoiCanGapID = getIntent().getExtras().getString("NguoiCanGapID");
            nguoiHenID = getIntent().getExtras().getString("NguoiHenID");
            moTaCuocHen = getIntent().getExtras().getString("MoTaCuocHen");
            tieuDeCuocHen = getIntent().getExtras().getString("TieuDeCuocHen");
            ngayHen = getIntent().getExtras().getString("NgayHen");

            if(!ngayHen.isEmpty()){
                edtNgayHen.setText(ngayHen);
            }
            if(!nguoiCanGapID.isEmpty()){
                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getValue(UserData.class).getUserID().equals(nguoiHenID)){
                            edtNguoiCanGap.setText(snapshot.getValue(UserData.class).getHoTen());
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
            if(!moTaCuocHen.isEmpty()){
                edtChiTietCuocHen.setText(moTaCuocHen);
            }
            if(!tieuDeCuocHen.isEmpty()){
                edtTieuDe.setText(tieuDeCuocHen);
            }

        }
    }

    View.OnClickListener deleteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.getValue(CuocHen.class).getIdCuocHen().equals(appointmentID)) {

                                        CuocHen appointment = new CuocHen(appointmentID, snapshot.getValue(CuocHen.class).getTieuDe(), snapshot.getValue(CuocHen.class).getNgayHen(), snapshot.getValue(CuocHen.class).getMoTaCuocHen(), snapshot.getValue(CuocHen.class).getNguoiHenID(), snapshot.getValue(CuocHen.class).getNguoiDuocHenID(), false);

                                        databaseReference.child("Appointment").child(appointmentID).setValue(appointment);
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


                            intent = new Intent(v.getContext(), LichHenActivity.class);
                            intent.putExtra("UserID", userID);
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
            builder.setMessage("Bạn muốn xóa cuộc hẹn?").setNegativeButton("No", dialogClick).setPositiveButton("Yes", dialogClick).show();
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), LichHenActivity.class);
            intent.putExtra("UserID", userID);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}