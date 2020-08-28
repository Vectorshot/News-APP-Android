package com.example.homeworkapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class SearchOnlineActivity extends AppCompatActivity {
    private String query_word;
    private SearchViewModel searchViewModel;
    private NewsListAdapter newsListAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle SavedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(SavedInstanceState);
        Intent intent = getIntent();
        this.query_word = intent.getStringExtra("keyword");
        Log.d("search words", "onCreate: " + this.query_word);
        setContentView(R.layout.search_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Search Results for "+this.query_word);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ProgressBar progressBar = findViewById(R.id.search_page_progress);
        TextView progress_words=findViewById(R.id.search_progress_textview);
        swipeRefreshLayout = findViewById(R.id.searchpage_refreshing);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.setSearching_q(this.query_word);
        newsListAdapter = new NewsListAdapter(this);
        newsListAdapter.setMutableLiveData(searchViewModel.search);
        recyclerView = findViewById(R.id.searchrecycleView);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsListAdapter);
        searchViewModel.search.observe(this, NewsDTOS -> {
            newsListAdapter.setList(NewsDTOS);
            newsListAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
            progress_words.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchViewModel.getResults();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onResume() {
        if (searchViewModel.search.getValue() != null) {
            List<NewsDTO> list = new ArrayList<>();
            list = searchViewModel.search.getValue();
            searchViewModel.search.setValue(list);
        }
        super.onResume();
    }
}
