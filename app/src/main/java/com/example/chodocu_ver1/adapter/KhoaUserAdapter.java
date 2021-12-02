package com.example.chodocu_ver1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.data_models.CuaHang;
import com.example.chodocu_ver1.data_models.KhoaUser;
import com.example.chodocu_ver1.data_models.SanPham;
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

public class KhoaUserAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<KhoaUser> khoaUserArrayList;

    public KhoaUserAdapter(Context context, int layout, ArrayList<KhoaUser> khoaUserArrayList) {
        this.context = context;
        this.layout = layout;
        this.khoaUserArrayList = khoaUserArrayList;
    }

    @Override
    public int getCount() {
        return khoaUserArrayList.size();
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
        TextView txtUserName;
        RadioGroup radGroup;
        RadioButton radDisable;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgUser = (ImageView) convertView.findViewById(R.id.imgUser);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            viewHolder.radGroup = (RadioGroup) convertView.findViewById(R.id.radGroup);
            viewHolder.radDisable = (RadioButton) convertView.findViewById(R.id.radDisable);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        KhoaUser khoaUser = khoaUserArrayList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        View finalConvertView = convertView;
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String sID = snapshot.getKey();
                String userID = snapshot.getValue(UserData.class).getUserID();
                if (snapshot.getValue(UserData.class).getUserID().equals(khoaUser.getUserID())) {
                    storageReference.child(snapshot.getValue(UserData.class).getImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (context.getApplicationContext() != null) {
                                Glide.with(context.getApplicationContext()).load(uri).into(viewHolder.imgUser);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    viewHolder.txtUserName.setText(snapshot.getValue(UserData.class).getUserName());
                }
                viewHolder.radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.radActive:
                                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                databaseReference.child("KhoaUser").child(snapshot.getKey()).removeValue();
                                                databaseReference.child("User").child(sID).child("tinhTrang").setValue(0);
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                viewHolder.radDisable.setChecked(true);
                                                return;
                                        }
                                    }
                                };
                                AlertDialog.Builder alert = new AlertDialog.Builder(finalConvertView.getContext());
                                alert.setMessage("Bạn chắn chắn muốn Active User!").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
                                break;
                            case R.id.radRemove:
                                DialogInterface.OnClickListener dialog1 = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                databaseReference.child("KhoaUser").addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                        databaseReference.child("KhoaUser").child(snapshot.getKey()).removeValue();
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
                                                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                        if (snapshot.getValue(UserData.class).getUserID().equals(userID)) {
                                                            databaseReference.child("User").child(snapshot.getKey()).removeValue();
                                                            UserData userData = new UserData(snapshot.getValue(UserData.class).getUserName(),snapshot.getValue(UserData.class).getShopID(),snapshot.getValue(UserData.class).getHoTen(),
                                                                    snapshot.getValue(UserData.class).getSoDienThoai(),snapshot.getValue(UserData.class).getGioiTinh(),snapshot.getValue(UserData.class).getDiaChi(),
                                                                    snapshot.getValue(UserData.class).getPassword(),snapshot.getValue(UserData.class).getImage(),snapshot.getValue(UserData.class).getUserID(), snapshot.getValue(UserData.class).getNgayThamGia()
                                                                    , snapshot.getValue(UserData.class).getSoCMND(),snapshot.getValue(UserData.class).getEmail(),snapshot.getValue(UserData.class).getCmndMatTruoc(),snapshot.getValue(UserData.class).getPermission(),
                                                                    snapshot.getValue(UserData.class).getHoaHong(), snapshot.getValue(UserData.class).getTinhTrang(), snapshot.getValue(UserData.class).getSoSPDaBan(),
                                                                    snapshot.getValue(UserData.class).getDiemThanhVien(), snapshot.getValue(UserData.class).getReport(),snapshot.getValue(UserData.class).getMoney());
                                                            databaseReference.child("BlackList").child(userID).setValue(userData);
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
                                                //Khóa user là khóa luôn shop, khóa luôn sản phẩm nhé nhóm mình(Đẳng)
                                                databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                        if (snapshot.getValue(CuaHang.class).getUserID().equals(userID)) {
                                                            databaseReference.child("Shop").child(snapshot.getKey()).removeValue();
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
                                                databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                        if (snapshot.getValue(SanPham.class).getUserID().equals(khoaUser.getUserID())) {
                                                            databaseReference.child("SanPham").child(snapshot.getKey()).removeValue();
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
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                viewHolder.radDisable.setChecked(true);
                                                return;
                                        }
                                    }
                                };
                                AlertDialog.Builder alertt = new AlertDialog.Builder(finalConvertView.getContext());
                                alertt.setMessage("Bạn chắn chắn muốn Remove User!").setNegativeButton("No", dialog1).setPositiveButton("Yes", dialog1).show();
                                break;
                        }
                    }
                });
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
