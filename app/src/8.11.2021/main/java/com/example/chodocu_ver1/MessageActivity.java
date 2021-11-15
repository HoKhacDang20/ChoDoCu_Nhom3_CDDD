package com.example.chodocu_ver1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.adapter.MessegaAdapter;
import com.example.chodocu_ver1.data_models.Message;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private ArrayList<UserData> userDataArrayList;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    CircleImageView image;
    TextView txtTenUser;

    UserData userData = new UserData();


    FirebaseUser fuser;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText text_send;

    MessegaAdapter messegaAdapter;
    ArrayList<Message> messages;

    Intent intent;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setTitle("chat");
        //getSupportActionBar().getDisplayOptions(t);

        image = findViewById(R.id.profile_img);
        txtTenUser = findViewById(R.id.txtTenUser);
        //btn_send = findViewById(R.id.send);

        //fuser = FirebaseAuth.getInstance().getCurrentUser();
        //reference = FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserData userData = snapshot.getValue(UserData.class);
//                txtTenUser.setText(userData.getHoTen());
//                if(userData.getImage().equals("default")){
//                    image.setImageResource(R.mipmap.anh_trang);
//                }else {
//                    Glide.with(MessageActivity.this).load(userData.getImage()).into(image);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        userDataArrayList = new ArrayList<>();

        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                userDataArrayList.add(snapshot.getValue(UserData.class));
                //Toast.makeText(getApplicationContext(), userData.getHoTen(),Toast.LENGTH_LONG).show();
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
