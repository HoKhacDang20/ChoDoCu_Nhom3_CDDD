package com.example.chodocu_ver1.adapter;



import android.content.Context;
import android.graphics.Color;
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
import com.example.chodocu_ver1.GiaoDichActivity;
import com.example.chodocu_ver1.R;

import com.example.chodocu_ver1.data_models.DatHang;
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

public class GiaoDichAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<DatHang> datHangArrayList;

    public GiaoDichAdapter(Context context, int layout, ArrayList<DatHang> orderDataArrayList) {
        this.context = context;
        this.layout = layout;
        this.datHangArrayList = orderDataArrayList;
    }

    @Override
    public int getCount() {
        return 0;
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
        ImageView imgSP;
        TextView tenSP, txtMoney, txtSoLuongSP, txtTinhTrang, txtThuNhap, txtLoaiDonHang;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgSP = (ImageView) convertView.findViewById(R.id.imgSP);
            viewHolder.tenSP = (TextView) convertView.findViewById(R.id.txtTenSP);
            viewHolder.txtMoney = (TextView) convertView.findViewById(R.id.txtMoney);
            viewHolder.txtTinhTrang = (TextView) convertView.findViewById(R.id.txtTinhTrang);
            viewHolder.txtThuNhap = (TextView) convertView.findViewById(R.id.txtThuNhap);
            viewHolder.txtSoLuongSP = (TextView) convertView.findViewById(R.id.txtSoLuongSP);
            viewHolder.txtLoaiDonHang = (TextView) convertView.findViewById(R.id.txtLoaiDonHang);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DatHang datHang = datHangArrayList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference  = FirebaseStorage.getInstance().getReference();
        storageReference.child(datHang.getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgSP);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        if(datHang.getLoaiDonHang() == 1){
            viewHolder.txtLoaiDonHang.setText("Loại đơn: Trực tiếp");
        }
        else if(datHang.getLoaiDonHang() == 2){
            viewHolder.txtLoaiDonHang.setText("Loại đơn: Giao hàng COD");
        }
        else if(datHang.getLoaiDonHang() == 3){
            viewHolder.txtLoaiDonHang.setText("Loại đơn: Thanh toán E-Wallet");
        }
        databaseReference.child("LichSuGiaoDich").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(datHang.getDonHangID().equals(snapshot.getValue(DatHang.class).getDonHangID()) && datHang.getTinhTrang() != -1 && datHang.getLoaiDonHang() != 1){
                    long money = datHang.getGiaTien() * datHang.getSellerCommission() / 100;
                    viewHolder.txtThuNhap.setText(String.valueOf(money) + "vnd");
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

        viewHolder.tenSP.setText(datHang.getSanPham().getTenSP());
        viewHolder.txtSoLuongSP.setText(String.valueOf(datHang.getSanPham().getSoLuong()));
        viewHolder.txtMoney.setText(String.valueOf(datHang.getGiaTien() + "vnd"));
        if (datHang.getTinhTrang() == -1) {
            viewHolder.txtThuNhap.setText("0vnd");
            viewHolder.txtTinhTrang.setText("Thất bại");
            viewHolder.txtTinhTrang.setTextColor(Color.RED);
        }
        else if (datHang.getTinhTrang() == 8) {
            viewHolder.txtTinhTrang.setText("Thành công");
            viewHolder.txtTinhTrang.setTextColor(Color.GREEN);
        }

        return convertView;
    }
}
