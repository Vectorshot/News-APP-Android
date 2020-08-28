package com.example.homeworkapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Handler handler;
    MutableLiveData<List<String>> list;
    final String[] array_words = new String[]{"Amazon", "Bookstore"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        DataRepository.init(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostController navHostController = (NavHostController) Navigation.findNavController(this, R.id.fragment);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this, navHostController, configuration);
        NavigationUI.setupWithNavController(bottomNavigationView, navHostController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.maintoolbarmenu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();


        AutoCompleteTextView autoCompleteTextView = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        ArrayAdapter<String> autosuggestionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setAdapter(autosuggestionAdapter);

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String query = autosuggestionAdapter.getItem(position);
            autoCompleteTextView.setText(query);
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchOnlineActivity.class);
                intent.putExtra("keyword", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Log.d("mylog", "onQueryTextChange: " + newText);
                DataRepository.getsuggestions(autosuggestionAdapter, newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
