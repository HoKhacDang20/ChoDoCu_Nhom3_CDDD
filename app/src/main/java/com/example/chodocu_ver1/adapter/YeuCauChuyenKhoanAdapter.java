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

import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.data_models.UserData;
import com.example.chodocu_ver1.data_models.UserDepositData;
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

public class YeuCauChuyenKhoanAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<UserDepositData> userDepositDataArrayList;

    public YeuCauChuyenKhoanAdapter(Context context, int layout, ArrayList<UserDepositData> userDepositDataArrayList) {
        this.context = context;
        this.layout = layout;
        this.userDepositDataArrayList = userDepositDataArrayList;
    }

    @Override
    public int getCount() {
        return userDepositDataArrayList.size();
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
        TextView txtUserName, txtTinhTrang;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder.imgUser = (ImageView) convertView.findViewById(R.id.imgUser);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            viewHolder.txtTinhTrang = (TextView) convertView.findViewById(R.id.txtTinhTrang);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        UserDepositData userDepositData = userDepositDataArrayList.get(position);

        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserData.class).getUserID().equals(userDepositData.getUserID())){
                    storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    viewHolder.txtUserName.setText("User: " + snapshot.getValue(UserData.class).getUserName() + "-" + snapshot.getValue(UserData.class).getHoTen() +
                            " yêu cầu nạp " + String.valueOf(userDepositData.getMoney()) + " vào ví!");
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

        if(userDepositData.getTinhTrang() == 0){
            viewHolder.txtTinhTrang.setText("Pending");
        }
        else if(userDepositData.getTinhTrang() == 1){
            viewHolder.txtTinhTrang.setText("Completed");
            viewHolder.txtTinhTrang.setTextColor(Color.GREEN);
        }
        else if(userDepositData.getTinhTrang() == -1){
            viewHolder.txtTinhTrang.setText("Canceled");
            viewHolder.txtTinhTrang.setTextColor(Color.RED);
        }

        return convertView;

    }
}
