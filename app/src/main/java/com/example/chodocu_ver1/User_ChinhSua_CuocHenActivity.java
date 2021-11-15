package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.chodocu_ver1.data_models.CuocHen;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User_ChinhSua_CuocHenActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private EditText edtTieuDe, edtNgayHen, edtNguoiCanGap, edtChiTietCuocHen;
    private Button btnSua, btnXoa, btnBack, btnChonNgay;
    private String userID, userName, appointmentID, nguoiCanGapID, nguoiHenID, moTaCuocHen, tieuDeCuocHen, ngayHen;
    private int mYear, mMonth, mDay;
    private Intent intent;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_chinhsua_cuochen_layout);

        edtTieuDe = (EditText) findViewById(R.id.edtTieuDe);
        edtNgayHen = (EditText) findViewById(R.id.edtNgayHen);
        edtNguoiCanGap = (EditText) findViewById(R.id.edtNguoiCanGap);
        edtChiTietCuocHen = (EditText) findViewById(R.id.edtChiTietCuocHen);
        btnSua = (Button) findViewById(R.id.btnSua);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnChonNgay = (Button) findViewById(R.id.btnChonNgay);

        btnBack.setOnClickListener(backClick);
        btnXoa.setOnClickListener(xoaClick);
        btnChonNgay.setOnClickListener(chonNgayClick);
        btnSua.setOnClickListener(suaClick);
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
                        if(snapshot.getValue(UserData.class).getUserID().equals(nguoiCanGapID)){
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

    View.OnClickListener suaClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Date currentDate = new Date();
            Date appointmentDate = new Date();
            if(!edtNgayHen.getText().toString().isEmpty()){
                try{
                    appointmentDate = simpleDateFormat.parse(edtNgayHen.getText().toString());
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            if(edtTieuDe.getText().toString().isEmpty() || edtTieuDe.getText().toString().length() < 10){
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa nhập tiêu đề hoặc tiêu đề quá ngắn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtNgayHen.getText().toString().isEmpty()){
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn ngày hẹn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(currentDate.compareTo(appointmentDate) == 1){
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(v.getContext());
                alert.setMessage("Ngày hẹn không thể nhỏ hơn ngày hiện tại!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtNguoiCanGap.getText().toString().isEmpty()){
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn user để tạo cuộc hẹn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtChiTietCuocHen.getText().toString().isEmpty() || edtChiTietCuocHen.getText().toString().length() < 20){
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa ghi chi tiết cuộc hẹn hoặc chi tiết quá ngắn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(!nguoiCanGapID.isEmpty() && !edtNguoiCanGap.getText().toString().isEmpty()){
                if(!appointmentID.isEmpty()){
                    DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if (snapshot.getValue(CuocHen.class).getIdCuocHen().equals(appointmentID)) {

                                                CuocHen appointment = new CuocHen(appointmentID, edtTieuDe.getText().toString(), edtNgayHen.getText().toString(), edtChiTietCuocHen.getText().toString(), snapshot.getValue(CuocHen.class).getNguoiHenID(), snapshot.getValue(CuocHen.class).getNguoiDuocHenID(), true);

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
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(v.getContext());
                    builder.setMessage("Bạn muốn sửa cuộc hẹn với " + edtNguoiCanGap.getText().toString() + "?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
                }

            }
        }
    };

    View.OnClickListener chonNgayClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            edtNgayHen.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };

    View.OnClickListener xoaClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!appointmentID.isEmpty()){
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                databaseReference.child("Appointment").child(appointmentID).removeValue();
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
                builder.setMessage("Bạn muốn xóa cuộc hẹn?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }

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