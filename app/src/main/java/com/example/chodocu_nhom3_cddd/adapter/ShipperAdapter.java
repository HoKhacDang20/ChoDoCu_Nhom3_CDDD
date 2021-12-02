package com.example.chodocu_nhom3_cddd.adapter;

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
import com.example.chodocu_nhom3_cddd.R;
import com.example.chodocu_nhom3_cddd.data_models.DatHang;
import com.example.chodocu_nhom3_cddd.data_models.UserData;
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

public class ShipperAdapter extends BaseAdapter {


    private Context context;
    private int layout;
    private ArrayList<UserData> shipperList;

    public ShipperAdapter(Context context, int layout, ArrayList<UserData> shipperList) {
        this.context = context;
        this.layout = layout;
        this.shipperList = shipperList;
    }



    @Override
    public int getCount() {
        return shipperList.size();
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
        ImageView imgShipper;
        TextView txtTenShipper, txtSDTShipper, txtShipperUserName, txtSoDonDangGiao;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return null;


        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            viewHolder.imgShipper = (ImageView) convertView.findViewById(R.id.imgShipper);
            viewHolder.txtTenShipper = (TextView) convertView.findViewById(R.id.txtTenShipper);
            viewHolder.txtSDTShipper = (TextView) convertView.findViewById(R.id.txtSDTShipper);
            viewHolder.txtShipperUserName = (TextView) convertView.findViewById(R.id.txtShipperUserName);
            viewHolder.txtSoDonDangGiao = (TextView) convertView.findViewById(R.id.txtSoDonDangGiao);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        UserData userData = shipperList.get(position);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(userData.getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgShipper);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.txtShipperUserName.setText("User name: " + userData.getUserName());
        viewHolder.txtTenShipper.setText("Họ tên: " + userData.getHoTen());
        viewHolder.txtSDTShipper.setText("Số điện thoại: " + userData.getSoDienThoai());

        ArrayList<DatHang> orderDataArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Shipper").child(userData.getUserID()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                orderDataArrayList.add(snapshot.getValue(DatHang.class));
                viewHolder.txtSoDonDangGiao.setText(String.valueOf(orderDataArrayList.size()));
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
