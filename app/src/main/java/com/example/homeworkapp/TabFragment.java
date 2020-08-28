package com.example.homeworkapp;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TabFragment extends Fragment {

    private TabViewModel tabViewModel;
    private String section_of_news;
    private RecyclerView recyclerView;
    private NewsListAdapter newsListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView textView;
    TabFragment(String tabname){
        super();
        this.section_of_news=tabname;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        List<NewsDTO> templist=new ArrayList<>();
        View root=inflater.inflate(R.layout.tab_fragment, container, false);
        ProgressBar progressBar=root.findViewById(R.id.tab_progressing);
        TextView progress_text=root.findViewById(R.id.tab_progress_text);
        swipeRefreshLayout=root.findViewById(R.id.searchpage_refreshing);
        tabViewModel=new ViewModelProvider(this).get(TabViewModel.class);
        tabViewModel.setSectionname(this.section_of_news);
        recyclerView=root.findViewById(R.id.recyclerView_tab);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        newsListAdapter=new NewsListAdapter(this.getContext());
        recyclerView.setAdapter(newsListAdapter);

        newsListAdapter.setMutableLiveData(tabViewModel.section_list);
        tabViewModel.section_list.observe(getViewLifecycleOwner(),NewsDTOS->{
            newsListAdapter.setList(NewsDTOS);
            newsListAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            progress_text.setVisibility(View.INVISIBLE);
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tabViewModel.tab_getNewsList();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });



        return root;
    }

    @Override
    public void onResume(){
        if (tabViewModel.section_list.getValue()!=null)
        {List<NewsDTO> list=new ArrayList<>();
        list=tabViewModel.section_list.getValue();
        tabViewModel.section_list.setValue(list);}
        super.onResume();
    }

}
