package com.example.homeworkapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {
    private String article;
    private DetailNewsViewModel detailNewsViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<String> articledetail;
    HomeViewModel homeViewModel;
    TabViewModel tabViewModel;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        sharedPreferences = this.getSharedPreferences("Bookmarks", 0);
        editor = sharedPreferences.edit();
        ImageView imageView = findViewById(R.id.full_news_img);
        TextView textView1 = findViewById(R.id.full_news_title);
        TextView textView2 = findViewById(R.id.full_news_section);
        TextView textView3 = findViewById(R.id.full_news_date);
        TextView textView4 = findViewById(R.id.full_news_article);
        TextView news_source = findViewById(R.id.full_news_source);
        ProgressBar progressBar = findViewById(R.id.article_progressing);
        TextView progress_text=findViewById(R.id.detail_progress_text);
        ScrollView scrollView = findViewById(R.id.scrollView2);
        scrollView.setVisibility(View.INVISIBLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("NewsApp");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        article = intent.getStringExtra("id");
        detailNewsViewModel = new ViewModelProvider(this).get(DetailNewsViewModel.class);
        detailNewsViewModel.Setid(article);
        detailNewsViewModel.mutableLiveData.observe(this, words -> {
            this.articledetail = words;
            textView1.setText(words.get(0));
            toolbar.setTitle(words.get(0));
            Picasso.with(this).load(words.get(1)).resize(1200, 750).into(imageView);
            textView2.setText(words.get(2));
            textView3.setText(words.get(3));
            textView4.setText(Html.fromHtml(words.get(4)));
            textView4.setLinkTextColor(Color.parseColor("#6200EE"));
            news_source.setText(
                    Html.fromHtml("<a href=\"" + words.get(5) + "\">View Full Article" + "</a>")
            );
            news_source.setMovementMethod(LinkMovementMethod.getInstance());
            news_source.setLinkTextColor(Color.parseColor("#6200EE"));
            progressBar.setVisibility(View.INVISIBLE);
            progress_text.setVisibility(View.INVISIBLE);
            toolbar.getMenu().findItem(R.id.favor).setVisible(true);
            toolbar.getMenu().findItem(R.id.share).setVisible(true);
            scrollView.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailmenu, menu);
        if (sharedPreferences.getStringSet(this.article,null)==null){
            menu.findItem(R.id.favor).setIcon(R.drawable.favor_icon);
        }
        else {
            menu.findItem(R.id.favor).setIcon(R.drawable.ic_bookmark_24px);
        }
        menu.findItem(R.id.favor).setVisible(false);
        menu.findItem(R.id.share).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.favor:
                String detailtitlestr="\""+this.articledetail.get(0)+"\"";
                if (sharedPreferences.getStringSet(this.article, null) == null) {
                    menuItem.setIcon(R.drawable.favor_icon);
                    Set<String> bookmark_news=new HashSet<>();
//                    String detailtitlestr=this.articledetail.get(0);
                    bookmark_news.add("title:"+this.articledetail.get(0));
                    bookmark_news.add("date:"+this.articledetail.get(3));
                    bookmark_news.add("image:"+this.articledetail.get(6));
                    bookmark_news.add("section:"+this.articledetail.get(2));
                    bookmark_news.add("id:"+this.article);
                    bookmark_news.add("url:"+this.articledetail.get(5));
                    editor.putStringSet(article,bookmark_news);
                    editor.commit();
                    menuItem.setIcon(R.drawable.ic_bookmark_24px);

                    Toast.makeText(getApplicationContext(),detailtitlestr+"was added to bookmarks",Toast.LENGTH_LONG).show();
                }
                else {
                    menuItem.setIcon(R.drawable.ic_bookmark_24px);
                    editor.remove(this.article);
                    editor.commit();
                    menuItem.setIcon(R.drawable.favor_icon);
                    Toast.makeText(getApplicationContext(),detailtitlestr+" was removed from bookmarks",Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.share:
                Intent tweet = new Intent(Intent.ACTION_VIEW);
                tweet.setData(Uri.parse("http://twitter.com/intent/tweet/?text="+"Check out this Link:"+this.articledetail.get(5)+"#CSCI571NewsSearch"));
                startActivity(tweet);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
