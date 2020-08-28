package com.example.homeworkapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    MutableLiveData<List<NewsDTO>> newsList;
    MutableLiveData<List<String>> weather;
    String locality;

    public HomeViewModel() {
        this.newsList = new MutableLiveData<>();
        this.weather=new MutableLiveData<>();
        getNewsList();
    }

    public void getNewsList() {
        // TODO get data from backend
        DataRepository.getNews_homepage(newsList);
    }

    public void setLocality(String location_name){
this.locality=location_name;
DataRepository.getLocation(weather,locality);
    }
}
