package com.example.chodocu_nhom3_cddd.adapter;

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
import com.example.chodocu_nhom3_cddd.R;
import com.example.chodocu_nhom3_cddd.data_models.DatHang;
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
            viewHolder.txtTinhTrang.setText("Ch??? x??c nh???n");
        }
        else if(datHang.getTinhTrang() == 1){
            viewHolder.txtTinhTrang.setText("??ang ????ng g??i");
        }
        else if(datHang.getTinhTrang() == 2){
            viewHolder.txtTinhTrang.setText("????ng g??i ho??n t???t");
        }
        else if(datHang.getTinhTrang() == 3){
            viewHolder.txtTinhTrang.setText("Ch??? v???n chuy???n");
        }
        else if(datHang.getTinhTrang() == 4){
            viewHolder.txtTinhTrang.setText("Shipper ??ang l???y h??ng");
        }
        else if(datHang.getTinhTrang() == 5){
            viewHolder.txtTinhTrang.setText("Shipper l???y h??ng th??nh c??ng");
        }
        else if(datHang.getTinhTrang() == 6){
            viewHolder.txtTinhTrang.setText("??ang giao h??ng");
        }
        else if(datHang.getTinhTrang() == 7){
            viewHolder.txtTinhTrang.setText("Giao h??ng th??nh c??ng");
        }
        else if(datHang.getTinhTrang() == 8){
            viewHolder.txtTinhTrang.setText("Ho??n th??nh");
            viewHolder.txtTinhTrang.setTextColor(Color.GREEN);
        }
        else if(datHang.getTinhTrang() == -1){
            viewHolder.txtTinhTrang.setText("H???y ????n");
            viewHolder.txtTinhTrang.setTextColor(Color.RED);
        }

        if(datHang.getLoaiDonHang() == 1){
            viewHolder.txtLoaiThanhToan.setText("Giao d???ch tr???c ti???p");
        }
        else if(datHang.getLoaiDonHang() == 2){
            viewHolder.txtLoaiThanhToan.setText("Thanh to??n COD");
        }
        else if(datHang.getLoaiDonHang() == 3){
            viewHolder.txtLoaiThanhToan.setText("???? thanh to??n");
        }

        return  convertView;
    }
}