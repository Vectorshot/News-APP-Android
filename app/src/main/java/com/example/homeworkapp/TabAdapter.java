package com.example.homeworkapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabAdapter extends FragmentStatePagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }
    private String Tabnames[]={"WORLD","BUSINESS","POLITICS","SPORTS","TECHNOLOGY","SCIENCE"};

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new TabFragment(this.Tabnames[i]);
        return fragment;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Tabnames[position];
    }
}