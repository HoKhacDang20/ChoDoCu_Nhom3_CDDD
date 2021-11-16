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

import com.example.chodocu_nhom3_cddd.R;
import com.example.chodocu_nhom3_cddd.data_models.SanPhamReport;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SanPhamBiBaoCaoAdapter extends BaseAdapter {

    private Context context;
    private int layout;

    public SanPhamBiBaoCaoAdapter(Context context, int layout, ArrayList<SanPhamReport> sanPhamReportArrayList) {
        this.context = context;
        this.layout = layout;
        this.sanPhamReportArrayList = sanPhamReportArrayList;
    }

    private ArrayList<SanPhamReport> sanPhamReportArrayList;



    @Override
    public int getCount() {
        return sanPhamReportArrayList.size();
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
        TextView txtTenSP, txtGiaTien;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgSP = (ImageView) convertView.findViewById(R.id.imgSpReport);
            viewHolder.txtTenSP = (TextView) convertView.findViewById(R.id.txtTenSpReport);
            viewHolder.txtGiaTien = (TextView) convertView.findViewById(R.id.txtGiaTienSpReport);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SanPhamReport sanPhamReport = sanPhamReportArrayList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(sanPhamReport.getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        viewHolder.txtTenSP.setText(sanPhamReport.getSanPham().getTenSP());
        viewHolder.txtGiaTien.setText(String.valueOf(sanPhamReport.getSanPham().getGiaTien()));

        return convertView;
    }
}
