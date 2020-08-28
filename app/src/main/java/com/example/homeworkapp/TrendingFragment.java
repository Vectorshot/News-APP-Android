package com.example.homeworkapp;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

public class TrendingFragment extends Fragment {

    private TrendingViewModel trendingViewModel;
    private LineChart lineChart;

    public static TrendingFragment newInstance() {
        return new TrendingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.trending_fragment, container, false);
        lineChart=root.findViewById(R.id.trendingchart);
        trendingViewModel=new ViewModelProvider(this).get(TrendingViewModel.class);
        EditText editText=root.findViewById(R.id.trendingtext);
        trendingViewModel.setTrendingQuery(editText.getText().toString());
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                trendingViewModel.setTrendingQuery(editText.getText().toString());
                return false;
            }
        });
        trendingViewModel.mutableLiveData.observe(getViewLifecycleOwner(),chart->{
            lineChart.setData(chart);
            lineChart.invalidate();
        });
        XAxis xAxis=lineChart.getXAxis();
        YAxis yAxisLeft=lineChart.getAxisLeft();
        YAxis yAxisRight=lineChart.getAxisRight();
        Legend legend=lineChart.getLegend();

//        legend.setTextColor(R.color.colorPrimary);
//        xAxis.setTextColor(R.color.colorPrimary);
//        yAxis.setTextColor(R.color.colorPrimary);

        yAxisLeft.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisRight.setDrawGridLines(false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trendingViewModel = ViewModelProviders.of(this).get(TrendingViewModel.class);
        // TODO: Use the ViewModel
    }

}
