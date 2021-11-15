package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.chodocu_ver1.adapter.CuocHenAdapter;
import com.example.chodocu_ver1.data_models.CuocHen;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LichHenActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<CuocHen> appointmentArrayList;
    private String userName, userID;
    private Button btnBack;
    private GridView gridCuocHen;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.lich_hen_layout);

        gridCuocHen = (GridView) findViewById(R.id.gridCuocHen);
        btnBack = (Button) findViewById(R.id.btnBack);

        appointmentArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);

        gridCuocHen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(userID.equals(appointmentArrayList.get(position).getNguoiHenID())){

                    intent = new Intent(view.getContext(), User_ChinhSua_CuocHenActivity.class);
                    intent.putExtra("UserID", userID);
                    intent.putExtra("UserName", userName);
                    intent.putExtra("MoTaCuocHen", appointmentArrayList.get(position).getMoTaCuocHen());
                    intent.putExtra("AppointmentID", appointmentArrayList.get(position).getIdCuocHen());
                    intent.putExtra("TieuDeCuocHen", appointmentArrayList.get(position).getTieuDe());
                    intent.putExtra("NguoiCanGapID", appointmentArrayList.get(position).getNguoiDuocHenID());
                    intent.putExtra("NguoiHenID", appointmentArrayList.get(position).getNguoiHenID());
                    intent.putExtra("NgayHen", appointmentArrayList.get(position).getNgayHen());
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);


                }
                else if(userID.equals(appointmentArrayList.get(position).getNguoiDuocHenID())){

                    intent = new Intent(view.getContext(), ChiTietCuocHenActivity.class);
                    intent.putExtra("UserID", userID);
                    intent.putExtra("UserName", userName);
                    intent.putExtra("MoTaCuocHen", appointmentArrayList.get(position).getMoTaCuocHen());
                    intent.putExtra("AppointmentID", appointmentArrayList.get(position).getIdCuocHen());
                    intent.putExtra("TieuDeCuocHen", appointmentArrayList.get(position).getTieuDe());
                    intent.putExtra("NguoiCanGapID", appointmentArrayList.get(position).getNguoiDuocHenID());
                    intent.putExtra("NguoiHenID", appointmentArrayList.get(position).getNguoiHenID());
                    intent.putExtra("NgayHen", appointmentArrayList.get(position).getNgayHen());
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);


                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        appointmentArrayList.clear();
        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");

            databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if((snapshot.getValue(CuocHen.class).getNguoiDuocHenID().equals(userID) && snapshot.getValue(CuocHen.class).isActive()) || snapshot.getValue(CuocHen.class).getNguoiHenID().equals(userID)){
                        appointmentArrayList.add(snapshot.getValue(CuocHen.class));
                    }
                    appointmentLoad();
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

    public void appointmentLoad(){
        CuocHenAdapter appointmentAdapter = new CuocHenAdapter(LichHenActivity.this, R.layout.lich_hen_adapter_layout, appointmentArrayList, userID);
        gridCuocHen.setAdapter(appointmentAdapter);
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
//            intent = new Intent(v.getContext(), UserMainActivity.class);
//            intent.putExtra("UserName", userName);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(intent);
        }
    };

}