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
import com.example.chodocu_ver1.data_models.DanhGia;
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

public class DanhGiaAdapter extends BaseAdapter {
    //Sử dụng BaseAdapter với ListView: Hồ Khắc Đẳng
    private Context context;
    private int layout;
    private ArrayList<DanhGia> danhGiaArrayList;

    public DanhGiaAdapter(Context context, int layout, ArrayList<DanhGia> danhGiaArrayList) {
        this.context = context;
        this.layout = layout;
        this.danhGiaArrayList = danhGiaArrayList;
    }

    @Override
    public int getCount() {
        return danhGiaArrayList.size();
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
        ImageView imgCommentUser, imgStar;
        TextView txtUserFullName, txtComment, txtCommentDate, txtSanPhamID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            viewHolder.imgCommentUser = (ImageView) convertView.findViewById(R.id.imgCommentUser);
            viewHolder.imgStar = (ImageView) convertView.findViewById(R.id.imgStar);
            viewHolder.txtComment = (TextView) convertView.findViewById(R.id.txtComment);
            //viewHolder.txtSanPhamID = (TextView) convertView.findViewById(R.id.txtTenSP);
            viewHolder.txtUserFullName = (TextView) convertView.findViewById(R.id.txtUserFullName);
            viewHolder.txtCommentDate = (TextView) convertView.findViewById(R.id.txtCommentDate);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DanhGia comment = danhGiaArrayList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //Muốn dùng StorageReference phải thêm thư viện
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserData.class).getUserID().equals(comment.getUserID())){
                    storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context).load(uri).into(viewHolder.imgCommentUser);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Chỗ này không biết làm gì hết
                        }
                    });
                    viewHolder.txtUserFullName.setText(snapshot.getValue(UserData.class).getHoTen());
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
        if(comment.getSoSao() == 1){
            viewHolder.imgStar.setImageResource(R.mipmap.danhgia_1sao);
        }
        else if(comment.getSoSao() == 2){
            viewHolder.imgStar.setImageResource(R.mipmap.danhgia_2sao);
        }
        else if(comment.getSoSao() == 3){
            viewHolder.imgStar.setImageResource(R.mipmap.danhgia_3sao);
        }
        else if(comment.getSoSao() == 4){
            viewHolder.imgStar.setImageResource(R.mipmap.danhgia_4sao);
        }
        else if(comment.getSoSao() == 5){
            viewHolder.imgStar.setImageResource(R.mipmap.danhgia_5sao);
        }

        viewHolder.txtComment.setText(comment.getMoTaComment());
        viewHolder.txtCommentDate.setText(comment.getNgayComment());

        return convertView;
    }
}
