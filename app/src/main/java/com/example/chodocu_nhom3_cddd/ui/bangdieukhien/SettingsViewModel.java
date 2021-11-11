package com.example.chodocu_nhom3_cddd.ui.bangdieukhien;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SettingsViewModel extends Fragment {
    private MutableLiveData<String> mText;

    public SettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is setting fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }
}
