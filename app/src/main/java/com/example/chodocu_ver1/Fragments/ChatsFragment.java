package com.example.chodocu_ver1.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.adapter.UserAdapter;
import com.example.chodocu_ver1.adapter.UserAdapterChat;
import com.example.chodocu_ver1.data_models.Chat;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapterChat userAdapter;
    private List<UserData> mUser;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_chats, container, false);

//        recyclerView = view.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        fuser = FirebaseAuth.getInstance().getCurrentUser();
//
//        userList = new ArrayList<>();
//
//        reference = FirebaseDatabase.getInstance().getReference("Chats");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                userList.clear();
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Chat chat = dataSnapshot.getValue(Chat.class);
//                    if(chat.getSender().equals(fuser.getUid())){
//                        userList.add(chat.getReceiver());
//                    }
//                    if(chat.getReceiver().equals(fuser.getUid())){
//                        userList.add(chat.getSender());
//                    }
//                }
//
//                readChats();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

//    private void readChats(){
//        mUser = new ArrayList<>();
//
//        reference = FirebaseDatabase.getInstance().getReference("User");

//        reference.child("User").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                mUser.clear();
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    UserData user = dataSnapshot.getValue(UserData.class);
//
//                    for(String id: userList){
//                        if(user.getUserID().equals(id)){
//                            if(mUser.size() != 0){
//                                for (UserData user1 : mUser){
//                                    if(!user.getUserID().equals(user1.getUserID())){
//                                        mUser.add(user);
//                                    }
//                                }
//                            }
//                            else {
//                                mUser.add(user);
//                            }
//                        }
//                    }
//                }
//
//                userAdapter = new UserAdapterChat(getContext(), mUser);
//                recyclerView.setAdapter(userAdapter);
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                mUser.clear();
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    UserData user = dataSnapshot.getValue(UserData.class);
//
//                    for(String id: userList){
//                        if(user.getUserID().equals(id)){
//                            if(mUser.size() != 0){
//                                for (UserData user1 : mUser){
//                                    if(!user.getUserID().equals(user1.getUserID())){
//                                        mUser.add(user);
//                                    }
//                                }
//                            }
//                            else {
//                                mUser.add(user);
//                            }
//                        }
//                    }
//                }
//
//                userAdapter = new UserAdapterChat(getContext(), mUser);
//                recyclerView.setAdapter(userAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}