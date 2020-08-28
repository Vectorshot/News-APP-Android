package com.example.homeworkapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SearchViewModel extends ViewModel {
    MutableLiveData<List<NewsDTO>> search;
    private String searching_q;

    public SearchViewModel() {
        search = new MutableLiveData<>();
    }

    public void setSearching_q(String text){
        this.searching_q=text;
        getResults();
    }

    public void getResults(){
DataRepository.getNews_searching(search,searching_q);
    }
}
