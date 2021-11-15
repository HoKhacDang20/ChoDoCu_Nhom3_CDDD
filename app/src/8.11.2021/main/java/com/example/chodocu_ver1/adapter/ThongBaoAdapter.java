package com.example.chodocu_ver1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.data_models.RemoveSanPham;
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

public class ThongBaoAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<RemoveSanPham> removeSanPhamArrayList;

    public ThongBaoAdapter(Context context, int layout, ArrayList<RemoveSanPham> removeSanPhamArrayList) {
        this.context = context;
        this.layout = layout;
        this.removeSanPhamArrayList = removeSanPhamArrayList;
    }

    @Override
    public int getCount() {
        return removeSanPhamArrayList.size();
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
        ImageView imgSp;
        TextView txtTenSp, btnSeen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            viewHolder.imgSp = (ImageView) convertView.findViewById(R.id.imgSp);
            viewHolder.txtTenSp = (TextView) convertView.findViewById(R.id.txtTenSp);
            viewHolder.btnSeen = (Button) convertView.findViewById(R.id.btnSeen);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RemoveSanPham removeSanPham = removeSanPhamArrayList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(removeSanPham.getSanPham().getSpImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgSp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        viewHolder.txtTenSp.setText("Tên sản phẩm: " + removeSanPham.getSanPham().getTenSP());
        viewHolder.btnSeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child("XoaSanPham").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                databaseReference.child("XoaSanPham").child(snapshot.getKey()).removeValue();
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
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn đã xem thông báo này!").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        });


        return convertView;
    }
}
