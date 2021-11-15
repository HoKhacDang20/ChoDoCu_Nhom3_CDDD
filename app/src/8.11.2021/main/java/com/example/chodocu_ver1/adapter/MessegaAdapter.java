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
import com.example.chodocu_ver1.data_models.Message;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MessegaAdapter extends RecyclerView.Adapter<MessegaAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private ArrayList<Message> messages;
    private String image;

    FirebaseUser fuser;

    public MessegaAdapter(Context context, ArrayList<Message> messages, String image) {
        this.context = context;
        this.messages = messages;
        this.image = image;
    }

    @NonNull
    @Override
    public MessegaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_right,parent,false);
            return new MessegaAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_left,parent,false);
            return new MessegaAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Message message = messages.get(position);

        holder.show_message.setText(message.getMessage());
        if (image.equals("default")){
            holder.imgUser.setImageResource(R.mipmap.anh_trang);

        }else {
            Glide.with(context).load(image).into(holder.imgUser);
        }

//        final UserData userData = messages.get(position);
//        holder.txtTenUser.setText(userData.getHoTen());
//        if(userData.getImage().equals("default")){
//            holder.imgUser.setImageResource(R.mipmap.anh_trang);
//        }else {
//            Glide.with(context).load(userData.getImage()).into(holder.imgUser);
//        }
//        holder.imgUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, MessageActivity.class);
//                intent.putExtra("userID",userData.getUserID());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView imgUser;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            imgUser = itemView.findViewById(R.id.profile_img);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(messages.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
