package com.example.chodocu_ver1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserTaoLichHenActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String userID, userName, nguoiCanGapID, nguoiCanGapName, tieuDe, ngayHen, chiTiet;
    private Intent intent;
    private EditText edtTieuDe, edtNgayHen, edtNguoiCanGap, edtChiTietCuocHen;
    private Button btnChonNgay, btnTimUser, btnCreate, btnBack, btnClear;
    private int mYear, mMonth, mDay;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_tao_cuoc_hen_layout);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnChonNgay = (Button) findViewById(R.id.btnChonNgay);
        btnTimUser = (Button) findViewById(R.id.btnTimUser);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnBack = (Button) findViewById(R.id.btnBack);
        edtTieuDe = (EditText) findViewById(R.id.edtTieuDe);
        edtNgayHen = (EditText) findViewById(R.id.edtNgayHen);
        edtNguoiCanGap = (EditText) findViewById(R.id.edtNguoiCanGap);
        edtChiTietCuocHen = (EditText) findViewById(R.id.edtChiTietCuocHen);


        btnBack.setOnClickListener(backClick);

        btnClear.setOnClickListener(clearClick);
        btnTimUser.setOnClickListener(timUserClick);
        btnCreate.setOnClickListener(taoLichHenClick);

        btnChonNgay.setOnClickListener(chonNgayClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
            nguoiCanGapID = getIntent().getExtras().getString("NguoiCanGapID");
            nguoiCanGapName = getIntent().getExtras().getString("NguoiCanGapName");
            tieuDe = getIntent().getExtras().getString("TieuDe");
            ngayHen = getIntent().getExtras().getString("NgayHen");
            chiTiet = getIntent().getExtras().getString("ChiTiet");

            if(!nguoiCanGapName.isEmpty()){
                edtNguoiCanGap.setText(nguoiCanGapName);
            }
            if(!tieuDe.isEmpty()){
                edtTieuDe.setText(tieuDe);
            }
            if(!ngayHen.isEmpty()){
                edtNgayHen.setText(ngayHen);
            }
            if(!chiTiet.isEmpty()){
                edtChiTietCuocHen.setText(chiTiet);
            }
        }
    }

    View.OnClickListener taoLichHenClick = new View.OnClickListener() {
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
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa nhập tiêu đề hoặc tiêu đề quá ngắn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtNgayHen.getText().toString().isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn ngày hẹn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(currentDate.compareTo(appointmentDate) == 1){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Ngày hẹn không thể nhỏ hơn ngày hiện tại!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtNguoiCanGap.getText().toString().isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn user để tạo cuộc hẹn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtChiTietCuocHen.getText().toString().isEmpty() || edtChiTietCuocHen.getText().toString().length() < 20){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa ghi chi tiết cuộc hẹn hoặc chi tiết quá ngắn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(!nguoiCanGapID.isEmpty() && !edtNguoiCanGap.getText().toString().isEmpty()){
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                String appointmentID = databaseReference.push().getKey();
                                CuocHen appointment = new CuocHen(appointmentID, edtTieuDe.getText().toString(), edtNgayHen.getText().toString(), edtChiTietCuocHen.getText().toString(), userID, nguoiCanGapID, true);

                                databaseReference.child("Appointment").child(appointmentID).setValue(appointment);

                                intent = new Intent(v.getContext(), UserMainActivity.class);
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
                builder.setMessage("Bạn muốn tạo cuộc hẹn với " + edtNguoiCanGap.getText().toString() + "?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }

        }
    };

    View.OnClickListener timUserClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserTimNguoiCanGapActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.putExtra("TieuDe", edtTieuDe.getText().toString());
            intent.putExtra("NgayHen", edtNgayHen.getText().toString());
            intent.putExtra("ChiTiet", edtChiTietCuocHen.getText().toString());
            intent.putExtra("NguoiCanGapID", nguoiCanGapID);
            intent.putExtra("NguoiCanGapName", edtNguoiCanGap.getText().toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener chonNgayClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get Current Date
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

    View.OnClickListener clearClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            edtChiTietCuocHen.setText("");
            edtNgayHen.setText("");
            edtNguoiCanGap.setText("");
            edtTieuDe.setText("");
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
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