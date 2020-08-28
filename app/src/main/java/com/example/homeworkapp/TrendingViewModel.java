package com.example.homeworkapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;

public class TrendingViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    MutableLiveData<LineData> mutableLiveData;

    public TrendingViewModel() {
        this.mutableLiveData = new MutableLiveData<>();
        DataRepository.getTrending(mutableLiveData,"CoronaVirus");
    }

    public void setTrendingQuery(String query) {
        getData(mutableLiveData, query);
    }

    public void getData(MutableLiveData<LineData> dataMutableLiveData, String query) {
        DataRepository.getTrending(dataMutableLiveData, query);
    }
}
