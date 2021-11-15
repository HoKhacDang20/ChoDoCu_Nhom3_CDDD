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
import com.example.chodocu_ver1.data_models.DatHang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DonMuaAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<DatHang> datHangList;

    public DonMuaAdapter(Context context, int layout, List<DatHang> datHangList) {
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
        storageReference.child(datHang.getSanPham().getSpImage()+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgSP);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        if(datHang.getTinhTrang() == 0){
            viewHolder.txtTinhTrang.setText("Chờ xác nhận");
        }
        else if(datHang.getTinhTrang() == 1){
            viewHolder.txtTinhTrang.setText("Đang đóng gói");
        }
        else if(datHang.getTinhTrang() == 2){
            viewHolder.txtTinhTrang.setText("Đóng gói hoàn tất");
        }
        else if(datHang.getTinhTrang() == 3){
            viewHolder.txtTinhTrang.setText("Chờ vận chuyển");
        }
        else if(datHang.getTinhTrang() == 4){
            viewHolder.txtTinhTrang.setText("Shipper đang lấy hàng");
        }
        else if(datHang.getTinhTrang() == 5){
            viewHolder.txtTinhTrang.setText("Shipper lấy hàng thành công");
        }
        else if(datHang.getTinhTrang() == 6){
            viewHolder.txtTinhTrang.setText("Đang giao hàng");
        }
        else if(datHang.getTinhTrang() == 7){
            viewHolder.txtTinhTrang.setText("Giao hàng thành công");
        }
        else if(datHang.getTinhTrang() == 8){
            viewHolder.txtTinhTrang.setText("Hoàn thành");
            viewHolder.txtTinhTrang.setTextColor(Color.GREEN);
        }
        else if(datHang.getTinhTrang() == -1){
            viewHolder.txtTinhTrang.setText("Hủy đơn");
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

        return  convertView;
    }
}
