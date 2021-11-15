package com.example.chodocu_ver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.chodocu_ver1.adapter.UserAdapter;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserTimNguoiCanGapActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Button btnBack;
    private GridView gridUser;
    private EditText edtFind;
    private ArrayList<UserData> userDataArrayList;
    private Intent intent;
    private String userName, userID, nguoiCanGapID, nguoiCanGapName, tieuDe, ngayHen, chiTiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_tim_nguoi_can_gap_layout);

        btnBack = (Button) findViewById(R.id.btnBack);
        gridUser = (GridView) findViewById(R.id.gridUser);
        edtFind = (EditText) findViewById(R.id.edtFind);

        userDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);
        edtFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    userDataArrayList.clear();
                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getValue(UserData.class).getPermission() == 1 && !snapshot.getValue(UserData.class).getUserID().equals(userID)){
                                if(snapshot.getValue(UserData.class).getSoDienThoai().contains(edtFind.getText().toString()) || snapshot.getValue(UserData.class).getHoTen().toLowerCase().contains(edtFind.getText().toString().toLowerCase()) || snapshot.getValue(UserData.class).getUserName().toLowerCase().contains(edtFind.getText().toString().toLowerCase())){
                                    userDataArrayList.add(snapshot.getValue(UserData.class));
                                }
                            }
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
                }
                else{
                    userDataArrayList.clear();
                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getValue(UserData.class).getPermission() == 1 && !snapshot.getValue(UserData.class).getUserID().equals(userID)){
                                userDataArrayList.add(snapshot.getValue(UserData.class));
                            }
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
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        gridUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                intent = new Intent(view.getContext(), UserTaoLichHenActivity.class);
                                intent.putExtra("UserName", userName);
                                intent.putExtra("UserID", userID);
                                intent.putExtra("NguoiCanGapID", userDataArrayList.get(position).getUserID());
                                intent.putExtra("NguoiCanGapName", userDataArrayList.get(position).getHoTen());
                                intent.putExtra("TieuDe", tieuDe);
                                intent.putExtra("NgayHen", ngayHen);
                                intent.putExtra("ChiTiet", chiTiet);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                finish();
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Chọn user " + userDataArrayList.get(position).getHoTen() + "?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        userDataArrayList.clear();

        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            nguoiCanGapID = getIntent().getExtras().getString("NguoiCanGapID");
            nguoiCanGapName = getIntent().getExtras().getString("NguoiCanGapName");
            tieuDe = getIntent().getExtras().getString("TieuDe");
            ngayHen = getIntent().getExtras().getString("NgayHen");
            chiTiet = getIntent().getExtras().getString("ChiTiet");

            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getPermission() == 1 && !snapshot.getValue(UserData.class).getUserID().equals(userID)){
                        userDataArrayList.add(snapshot.getValue(UserData.class));
                    }
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
        }
    }

    public void userLoad(){
        UserAdapter userAdapter = new UserAdapter(UserTimNguoiCanGapActivity.this, R.layout.user_adapter_layout, userDataArrayList);
        gridUser.setAdapter(userAdapter);
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), UserTaoLichHenActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.putExtra("NguoiCanGapID", nguoiCanGapID);
            intent.putExtra("NguoiCanGapName", nguoiCanGapName);
            intent.putExtra("TieuDe", tieuDe);
            intent.putExtra("NgayHen", ngayHen);
            intent.putExtra("ChiTiet", chiTiet);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
}