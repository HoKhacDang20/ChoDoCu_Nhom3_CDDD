package com.example.chodocu_ver1.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {


    private Context context;
    private int layout;
    private ArrayList<UserData> userDataArrayList;

    public UserAdapter(Context context, int layout, ArrayList<UserData> userDataArrayList) {
        this.context = context;
        this.layout = layout;
        this.userDataArrayList = userDataArrayList;
    }


    @Override
    public int getCount() {
        return userDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        ImageView imgUser;
        TextView txtTenUser, txtSDT, txtDiaChi;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            viewHolder.imgUser = (ImageView) convertView.findViewById(R.id.imgUser);
            viewHolder.txtTenUser = (TextView) convertView.findViewById(R.id.txtTenUser);
            viewHolder.txtSDT = (TextView) convertView.findViewById(R.id.txtSDT);
            viewHolder.txtDiaChi = (TextView) convertView.findViewById(R.id.txtDiaChi);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        UserData userData = userDataArrayList.get(position);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(userData.getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgUser);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        viewHolder.txtDiaChi.setText("Địa chỉ: " + userData.getDiaChi());
        viewHolder.txtTenUser.setText("Họ tên: " + userData.getHoTen());
        viewHolder.txtSDT.setText("Số điện thoại: " + userData.getSoDienThoai());

        return convertView;
    }
}
