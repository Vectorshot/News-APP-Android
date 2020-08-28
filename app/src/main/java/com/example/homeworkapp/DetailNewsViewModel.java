package com.example.homeworkapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class DetailNewsViewModel extends ViewModel {
    MutableLiveData<List<String>> mutableLiveData;
    private String id;

    public DetailNewsViewModel(){
        mutableLiveData=new MutableLiveData<>();
    }

    public void Setid(String l){
        this.id=l;
        getNewsInformation();
    }

    public void getNewsInformation(){
        DataRepository.getNews_detail(this.mutableLiveData,this.id);
    }
}
