package com.example.chodocu_ver1.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.MessageActivity;
import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.data_models.Chat;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    DatabaseReference reference;
    FirebaseUser fuser;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    private Context mContext;
    private List<Chat> mChat;
    private String imgURL;

    public  MessageAdapter(Context mContext, List<Chat> mChat, String imgURL)
    {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imgURL = imgURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {


        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessega());



        if(imgURL.equals("")){
            holder.img.setImageResource(R.mipmap.anh_trang);
        }else {
            Glide.with(mContext).load(imgURL).into(holder.img);
            storageReference.child(imgURL + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext).load(uri).into(holder.img);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                         //Toast.makeText(AccountInfoActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                }
            });
        }


//        UserData user = mUser.get(position);
//        holder.userName.setText(user.getUserName());
//        if(user.getImage().equals("")){
//            holder.img.setImageResource(R.mipmap.anh_trang);
//        }else {
//            //Glide.with(mContext).load(user.getImage()).into(holder.img);
//            storageReference.child(user.getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Glide.with(mContext).load(uri).into(holder.img);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
////                                    Toast.makeText(AccountInfoActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MessageActivity.class);
//                intent.putExtra("userID", user.getUserID());
//                mContext.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView show_message;
        public ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            img = itemView.findViewById(R.id.profile_img);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
