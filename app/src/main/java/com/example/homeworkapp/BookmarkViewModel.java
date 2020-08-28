package com.example.homeworkapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookmarkViewModel extends ViewModel {

    public MutableLiveData<List<String>> mutableLiveData;

    public BookmarkViewModel(){
        this.mutableLiveData=new MutableLiveData<>();
        getCollection();
    }

    public void setMutableLiveData(List<String> list){
        this.mutableLiveData.setValue(list);
    }

    public void getCollection(){
        // xxxx.function(mu..)
        DataRepository.getNews_favor(mutableLiveData);
    }
}
