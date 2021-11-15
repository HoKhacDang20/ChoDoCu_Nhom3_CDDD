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
import com.example.chodocu_ver1.MessageMain;
import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class UserAdapterChat extends RecyclerView.Adapter<UserAdapterChat.ViewHolder>{

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    DatabaseReference reference;
    FirebaseUser fuser;

    private Context mContext;
    private List<UserData> mUser;

    public  UserAdapterChat(Context mContext, List<UserData> mUser)
    {
        this.mUser = mUser;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);

        return new UserAdapterChat.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserData user = mUser.get(position);
        holder.userName.setText(user.getUserName());
        if(user.getImage().equals("")){
            holder.img.setImageResource(R.mipmap.anh_trang);
        }else {
            //Glide.with(mContext).load(user.getImage()).into(holder.img);
            storageReference.child(user.getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext).load(uri).into(holder.img);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(AccountInfoActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userID", user.getUserID());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView userName;
        public ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.txtUserName);
            img = itemView.findViewById(R.id.profile_img);
        }
    }
}
