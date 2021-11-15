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
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.data_models.CuaHang;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class YeuCauTaoShopAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<CuaHang> cuaHangArrayList;

    @Override
    public int getCount() {
        return cuaHangArrayList.size();
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
        TextView txtUserName, txtSanPhamDaBan, txtDiemThanhVien, txtReported;
    }

    public YeuCauTaoShopAdapter(Context context, int layout, ArrayList<CuaHang> cuaHangArrayList) {
        this.context = context;
        this.layout = layout;
        this.cuaHangArrayList = cuaHangArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgUser = (ImageView) convertView.findViewById(R.id.imgUser);
            viewHolder.txtDiemThanhVien = (TextView) convertView.findViewById(R.id.txtDiemThanhVien);
            viewHolder.txtReported = (TextView) convertView.findViewById(R.id.txtReported);
            viewHolder.txtSanPhamDaBan = (TextView) convertView.findViewById(R.id.txtSanPhamDaBan);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CuaHang cuaHang = cuaHangArrayList.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserData.class).getUserID().equals(cuaHang.getUserID())){
                    storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context).load(uri).into(viewHolder.imgUser);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    viewHolder.txtUserName.setText(snapshot.getValue(UserData.class).getUserName() + " - " + snapshot.getValue(UserData.class).getHoTen());
                    viewHolder.txtSanPhamDaBan.setText("Số sản phẩm đã bán: " + String.valueOf(snapshot.getValue(UserData.class).getSoSPDaBan()));
                    viewHolder.txtDiemThanhVien.setText("Điểm thành viên: " + String.valueOf(snapshot.getValue(UserData.class).getDiemThanhVien()));
                    viewHolder.txtReported.setText("Report: " + String.valueOf(snapshot.getValue(UserData.class).getReport()));

                }
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

        return convertView;
    }
}
