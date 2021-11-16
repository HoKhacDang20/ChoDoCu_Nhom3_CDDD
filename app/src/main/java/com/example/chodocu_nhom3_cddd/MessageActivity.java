package com.example.chodocu_nhom3_cddd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.adapter.MessageAdapter;
import com.example.chodocu_ver1.data_models.Chat;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    CircleImageView img;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText  text_send;

    MessageAdapter messageAdapter;
    List<Chat> mChat;

    RecyclerView recyclerView;


    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        img = findViewById(R.id.profile_img);
        username = findViewById(R.id.txtUserName);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();

        final String userID = intent.getStringExtra("userID");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mes = text_send.getText().toString();
                if(!mes.equals("")){
                    //sendMessage(fuser.getUid(), userID, mes);

                    String skey = reference.push().getKey();
                    Chat chat = new Chat(skey, fuser.getUid(), userID, mes);
                    reference.child("Chats").child(skey).setValue(chat);
                }else {
                    Toast.makeText(MessageActivity.this, "Bạn nhập tin nhắn trước khi gửi!", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData user = snapshot.getValue(UserData.class);
                username.setText(user.getUserName());
                if(user.getImage().equals("")){
                    img.setImageResource(R.mipmap.anh_trang);
                }
                else {
                    storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(MessageActivity.this).load(uri).into(img);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(AccountInfoActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                readMessages(fuser.getUid(), userID, user.getImage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private  void sendMessage(String sender, String receiver, String message){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);

    }

    private void readMessages(String myid, String userID, String imgURL){

        mChat = new ArrayList<>();

        /*Chat chat1 = new Chat("1iL4mYS7QyMo2Bz0p4pSL4Oiggx","6iOy8gp42uYd1vldOY79DcyAXT23","Hello");
        Chat chat2 = new Chat("6iOy8gp42uYd1vldOY79DcyAXT23","1iL4mYS7QyMo2Bz0p4pSL4Oiggx","Nani?");
        Chat chat3 = new Chat("1iL4mYS7QyMo2Bz0p4pSL4Oiggx","6iOy8gp42uYd1vldOY79DcyAXT23","Wanna die :v");
        mChat.add(chat1);
        mChat.add(chat2);
        mChat.add(chat3);

        messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imgURL);
        recyclerView.setAdapter(messageAdapter);*/


        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                //mChat.clear();
                 //Toast.makeText(MessageActivity.this, snapshot.getValue(Chat.class).getMessega(), Toast.LENGTH_SHORT).show();
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userID)
                            || chat.getReceiver().equals(userID) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                        messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imgURL);
                        recyclerView.setAdapter(messageAdapter);

                    }

                }
//                messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imgURL);
//                recyclerView.setAdapter(messageAdapter);





//                //Toast.makeText(MessageActivity.this, snapshot.getValue(Chat.class).getMessega(), Toast.LENGTH_SHORT).show();
//                //mChat.clear();
//
//                Chat chat = snapshot.getValue(Chat.class);
//
////                messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imgURL);
////                recyclerView.setAdapter(messageAdapter);
//
//                if(chat.getReceiver().equals(myid) && chat.getSender().equals(userID)
//                        || chat.getReceiver().equals(userID) && chat.getSender().equals(myid)){
//
//                    mChat.add(chat);
//
//
//
//                }
//                else {
//                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imgURL);
//                    recyclerView.setAdapter(messageAdapter);
//                }
////                mChat.add(chat);
////
////                messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imgURL);
////                recyclerView.setAdapter(messageAdapter);

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

        /*reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Toast.makeText(MessageActivity.this, dataSnapshot.getValue(Chat.class).getMessega(), Toast.LENGTH_SHORT).show();
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userID)
                    || chat.getReceiver().equals(userID) && chat.getSender().equals(myid)){

                        mChat.add(chat);

                        messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imgURL);
                        recyclerView.setAdapter(messageAdapter);

                    }
                    mChat.add(chat);

                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imgURL);
                    recyclerView.setAdapter(messageAdapter);
                }
//                messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imgURL);
//                recyclerView.setAdapter(messageAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }
}