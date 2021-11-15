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

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.data_models.DanhGia;
import com.example.chodocu_ver1.data_models.DatHang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ChoHangAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<DatHang> datHangList;

    public ChoHangAdapter(Context context, int layout, List<DatHang> datHangList) {
        this.context = context;
        this.layout = layout;
        this.datHangList = datHangList;
    }


    @Override
    public int getCount() {
        return datHangList.size();
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
        TextView txtTenSP, txtGiaSP, txtLoaiThanhToan, txtTinhTrang;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgSP = (ImageView) convertView.findViewById(R.id.imgSP);
            viewHolder.txtTenSP = (TextView) convertView.findViewById(R.id.txtTenSP);
            viewHolder.txtGiaSP = (TextView) convertView.findViewById(R.id.txtGiaSP);
            viewHolder.txtLoaiThanhToan = (TextView) convertView.findViewById(R.id.txtLoaiThanhToan);
            viewHolder.txtTinhTrang = (TextView) convertView.findViewById(R.id.txtTinhTrang);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DatHang datHang = datHangList.get(position);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(datHang.getSanPham() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgSP);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        viewHolder.txtTenSP.setText(datHang.getSanPham().getTenSP());
        viewHolder.txtGiaSP.setText("Tổng tiền: " + String.valueOf(datHang.getGiaTien()) + "vnđ");

        if(datHang.getTinhTrang() == 7){
            viewHolder.txtTinhTrang.setText("Chờ trả tiền");
        }
        else if(datHang.getTinhTrang() == -1){
            viewHolder.txtTinhTrang.setText("Chờ trả hàng");
            viewHolder.txtTinhTrang.setTextColor(Color.RED);
        }

        if(datHang.getLoaiDonHang() == 1){
            viewHolder.txtLoaiThanhToan.setText("Giao dịch trực tiếp");
        }
        else if(datHang.getLoaiDonHang() == 2){
            viewHolder.txtLoaiThanhToan.setText("Thanh toán COD");
        }
        else if(datHang.getLoaiDonHang() == 3){
            viewHolder.txtLoaiThanhToan.setText("Đã thanh toán");
        }

        return convertView;
    }
}
