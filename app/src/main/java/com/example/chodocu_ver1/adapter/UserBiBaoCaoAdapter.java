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

import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.data_models.UserData;
import com.example.chodocu_ver1.data_models.UserReport;
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

public class UserBiBaoCaoAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<UserReport> userReportArrayList;

    public UserBiBaoCaoAdapter(Context context, int layout, ArrayList<UserReport> userReportArrayList) {
        this.context = context;
        this.layout = layout;
        this.userReportArrayList = userReportArrayList;
    }

    @Override
    public int getCount() {
        return userReportArrayList.size();
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
        ImageView imgUserReport;
        TextView txtTenUserReport, txtDiaChiUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgUserReport = (ImageView) convertView.findViewById(R.id.imgUserReport);
            viewHolder.txtTenUserReport = (TextView) convertView.findViewById(R.id.txtTenUserReport);
            viewHolder.txtDiaChiUser = (TextView) convertView.findViewById(R.id.txtDiaChiUser);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UserReport userReport = userReportArrayList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue(UserData.class).getUserID().equals(userReport.getDoiTuongReportID())) {
                    storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    viewHolder.txtTenUserReport.setText(snapshot.getValue(UserData.class).getUserName());
                    viewHolder.txtDiaChiUser.setText(snapshot.getValue(UserData.class).getDiaChi());
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
