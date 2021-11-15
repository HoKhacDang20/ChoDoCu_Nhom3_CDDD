package com.example.chodocu_ver1.ui.thongbao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewPostViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NewPostViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is new post fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
