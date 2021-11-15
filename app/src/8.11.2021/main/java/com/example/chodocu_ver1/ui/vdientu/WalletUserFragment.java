package com.example.chodocu_ver1.ui.vdientu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chodocu_ver1.R;
import com.example.chodocu_ver1.UserMainActivity;
import com.example.chodocu_ver1.ViDienTuActivity;
import com.example.chodocu_ver1.data_models.UserData;
import com.example.chodocu_ver1.data_models.UserDepositData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WalletUserFragment extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private EditText edtSoTien;
    private Button btnXacNhan;
    private String sUserName, userID;
    private Intent intent;
    private ArrayList<UserData> userDataArrayList = ViDienTuActivity.userDataArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_nap_tien, container, false);

        edtSoTien = (EditText) view.findViewById(R.id.edtSoTien);
        btnXacNhan = (Button) view.findViewById(R.id.btnXacNhan);

        Bundle bundle = getArguments();

        if(bundle != null){
            sUserName = bundle.getString("UserName");
            userID = bundle.getString("UserID");
        }

        btnXacNhan.setOnClickListener(xacNhanClick);

        return view;
    }

    View.OnClickListener xacNhanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
                        case DialogInterface.BUTTON_POSITIVE:
                            String giaoDichID = databaseReference.push().getKey();
                            UserDepositData userDepositData = new UserDepositData(giaoDichID, userID, Long.valueOf(edtSoTien.getText().toString()), 0);
                            databaseReference.child("UserDepositRequest").child(giaoDichID).setValue(userDepositData);
                            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                            alert.setMessage("Yêu cầu của bạn đang được xử lí!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    intent = new Intent(v.getContext(), UserMainActivity.class);
                                    intent.putExtra("UserName", sUserName);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(intent);
                                    edtSoTien.setText("");
                                }
                            }).show();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
            alert.setMessage("Bạn muốn nạp " + edtSoTien.getText().toString() + "vnđ vào ví?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
        }
    };
}
