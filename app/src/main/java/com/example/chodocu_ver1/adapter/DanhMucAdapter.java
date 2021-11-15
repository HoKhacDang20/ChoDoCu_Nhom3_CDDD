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
import com.example.chodocu_ver1.data_models.DanhGia;
import com.example.chodocu_ver1.data_models.DanhMuc;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DanhMucAdapter extends BaseAdapter {
    //
//    private String danhMucID;
//    private String tenDanhMuc;
//    private String danhMucIMG;
    private Context context;
    private int layout;
    private List<DanhMuc> danhMucList;

    public DanhMucAdapter(Context context, int layout, List<DanhMuc> danhMucList) {
        this.context = context;
        this.layout = layout;
        this.danhMucList = danhMucList;
    }



    @Override
    public int getCount() {
        return danhMucList.size();
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
        ImageView imgDanhMuc;
        TextView txtTenDanhMuc;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder.imgDanhMuc = (ImageView) convertView.findViewById(R.id.imgDanhMuc);
            viewHolder.txtTenDanhMuc = (TextView) convertView.findViewById(R.id.txtTenDanhMuc);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DanhMuc danhMucData = danhMucList.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(danhMucData.getDanhMucIMG()+ ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgDanhMuc);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        viewHolder.txtTenDanhMuc.setText(danhMucData.getTenDanhMuc());
        return convertView;
    }


}
