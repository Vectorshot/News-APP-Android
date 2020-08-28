package com.example.homeworkapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TabViewModel extends ViewModel {
    private String sectionname;
    MutableLiveData<List<NewsDTO>> section_list;

    public void  setSectionname(String section_name_news){
        this.section_list=new MutableLiveData<>();
        this.sectionname=section_name_news.toLowerCase();
        tab_getNewsList();
    }

    public void tab_getNewsList(){
DataRepository.getNews_headlines(section_list,sectionname);
    }
}
