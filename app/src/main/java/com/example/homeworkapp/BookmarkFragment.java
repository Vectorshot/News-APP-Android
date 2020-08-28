package com.example.homeworkapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookmarkFragment extends Fragment {

    private BookmarkViewModel mViewModel;
    private RecyclerView recyclerView;
    private BookmarkAdapter bookmarkAdapter;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    private List<String> bookmarklist = new ArrayList<>();
    private MutableLiveData<List<String>> mutableLiveData;

    public static BookmarkFragment newInstance() {
        return new BookmarkFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences("Bookmarks", 0);
        editor = sharedPreferences.edit();
//        bookmarklist = new ArrayList<>(sharedPreferences.getAll().keySet());

        View root = inflater.inflate(R.layout.bookmark_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(BookmarkViewModel.class);
        TextView bookmark_text_info=root.findViewById(R.id.bookmark);
        recyclerView = root.findViewById(R.id.recyclerViewBookmark);
        bookmarkAdapter = new BookmarkAdapter(this.getContext());
        bookmarkAdapter.mutableLiveData = mViewModel.mutableLiveData;
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        recyclerView.setAdapter(bookmarkAdapter);

        mViewModel.mutableLiveData.observe(getViewLifecycleOwner(), Bookmarknames -> {
            if (Bookmarknames.size()==0){
                bookmark_text_info.setVisibility(View.VISIBLE);
            }
            else {
                bookmark_text_info.setVisibility(View.INVISIBLE);
            }
            bookmarkAdapter.setfavors(Bookmarknames);
            bookmarkAdapter.notifyDataSetChanged();
        });
        return root;
    }

}
