package com.example.homeworkapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SuggestionAdapter extends ArrayAdapter {
    private List<String> list;
    public SuggestionAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        list=new ArrayList<>();
    }

    public void setSuggestions(List<String> suggestions){
        this.list=suggestions;
    }

    public int getCount(){
        return list.size();
    }
}
