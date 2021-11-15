package com.example.chodocu_ver1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.data_models.CuocHen;
import com.example.chodocu_ver1.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;

public class CuocHenAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<CuocHen> cuocHenArrayList;
    private String userID;

    public CuocHenAdapter(Context context, int layout, ArrayList<CuocHen> cuocHenArrayList, String userID) {
        this.context = context;
        this.layout = layout;
        this.cuocHenArrayList = cuocHenArrayList;
        this.userID = userID;
    }

    @Override
    public int getCount() {
        return cuocHenArrayList.size();
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
        TextView txtTieuDe, txtNguoiHen, txtChiTiet;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.txtChiTiet = (TextView) convertView.findViewById(R.id.txtChiTiet);
            viewHolder.txtNguoiHen = (TextView) convertView.findViewById(R.id.txtNguoiHen);
            viewHolder.txtTieuDe = (TextView) convertView.findViewById(R.id.txtTieuDe);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CuocHen cuocHen  = cuocHenArrayList.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        viewHolder.txtTieuDe.setText("Tiêu đề: " + cuocHen.getTieuDe());
        if(userID.equals(cuocHen.getNguoiHenID())){
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getUserID().equals(cuocHen.getNguoiDuocHenID())){
                        viewHolder.txtNguoiHen.setText("Bạn hẹn gặp " + snapshot.getValue(UserData.class).getHoTen() + " vào ngày " + cuocHen.getNgayHen());
                        if(cuocHen.getMoTaCuocHen().length() > 30){
                            viewHolder.txtChiTiet.setText("Chi tiết: " + cuocHen.getMoTaCuocHen().substring(0, 28) + "..." + " - Liên hệ: " + snapshot.getValue(UserData.class).getSoDienThoai());
                        }
                        else{
                            viewHolder.txtChiTiet.setText("Chi tiết: " + cuocHen.getMoTaCuocHen() + " - Liên hệ: " + snapshot.getValue(UserData.class).getSoDienThoai());
                        }
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
        }
        else if(userID.equals(cuocHen.getNguoiDuocHenID())){
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getUserID().equals(cuocHen.getNguoiHenID())){
                        viewHolder.txtNguoiHen.setText("Bạn có một cuộc hẹn với " + snapshot.getValue(UserData.class).getHoTen() + " vào ngày " + cuocHen.getNgayHen());
                        if(cuocHen.getMoTaCuocHen().length() > 30){
                            viewHolder.txtChiTiet.setText("Chi tiết: " + cuocHen.getMoTaCuocHen().substring(0, 30) + "..." + " - Liên hệ: " + snapshot.getValue(UserData.class).getSoDienThoai());
                        }
                        else{
                            viewHolder.txtChiTiet.setText("Chi tiết: " + cuocHen.getMoTaCuocHen() + " - Liên hệ: " + snapshot.getValue(UserData.class).getSoDienThoai());
                        }
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
        }
        return convertView;
    }
}
