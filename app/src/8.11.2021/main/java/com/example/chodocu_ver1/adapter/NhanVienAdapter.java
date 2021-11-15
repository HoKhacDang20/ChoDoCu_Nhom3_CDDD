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

public class NhanVienAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<UserData> userDataArrayList;

    public NhanVienAdapter(Context context, int layout, ArrayList<UserData> userDataArrayList) {
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
        ImageView imgEmployee;
        TextView txtTen, txtSDT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgEmployee = (ImageView) convertView.findViewById(R.id.imgEmployee);
            viewHolder.txtTen = (TextView) convertView.findViewById(R.id.txtTen);
            viewHolder.txtSDT = (TextView) convertView.findViewById(R.id.txtSDT);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UserData userData = userDataArrayList.get(position);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(userData.getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgEmployee);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        //viewHolder.txtTen.setText("Hồ Khắc Đẳng: " + userData.getsFullName());
        viewHolder.txtTen.setText("Họ và tên: " + userData.getHoTen());
        viewHolder.txtSDT.setText("Số điện thoại: " + userData.getSoDienThoai());
        return  convertView;
    }
}
