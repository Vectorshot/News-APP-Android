package com.example.homeworkapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.CardViewHolder> {

    private List<NewsDTO> list = new ArrayList<>();
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MutableLiveData<List<NewsDTO>> mutableLiveData=new MutableLiveData<>();
//    HomeViewModel homeViewModel=null;
//    TabViewModel tabViewModel=null;
//    SearchViewModel searchViewModel=null;

    public NewsListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<NewsDTO> list) {
        this.list = list;
    }

    public void setMutableLiveData(MutableLiveData<List<NewsDTO>> list){
        this.mutableLiveData=list;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sharedPreferences = context.getSharedPreferences("Bookmarks", 0);
        editor=sharedPreferences.edit();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.newscard, parent, false);
        return new CardViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.TextTitle.setText(this.list.get(position).getTitle());
        holder.TextDate.setText(this.list.get(position).getDate());
        holder.Textsection.setText(this.list.get(position).getSection_id());
        String link_img=this.list.get(position).getImage();
        Picasso.with(this.context).load(link_img).resize(100,100).into(holder.CardImage);
        String link_article = this.list.get(position).getNewsid();
        String title_article=this.list.get(position).getTitle();
        String date_article=this.list.get(position).getDate();
        String section_article=this.list.get(position).getSection_id();
        String thumbnail_article=this.list.get(position).getImage();
        String url_article=this.list.get(position).getUrl();
        Set<String> bookmark_news = new HashSet<>();
        String shown_toast="\""+title_article+"\"";
        if (sharedPreferences.getStringSet(link_article, null) == null) {
            holder.imageButton.setImageResource(R.drawable.ic_bookmark_border_24px);
        } else {
            holder.imageButton.setImageResource(R.drawable.ic_bookmark_24px);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("id", link_article);
                v.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialog_longclick);
                TextView textView=dialog.findViewById(R.id.dialog_title);
                textView.setText(title_article);
                ImageButton button1=dialog.findViewById(R.id.dialog_favor);
                ImageButton button2=dialog.findViewById(R.id.dialog_share);
                ImageView imageView=dialog.findViewById(R.id.dialog_pic);
                Picasso.with(context).load(link_img).resize(1000,500).into(imageView);
                if (sharedPreferences.getStringSet(link_article, null) == null) {
                    button1.setImageResource(R.drawable.favor_icon);
                } else {
                    button1.setImageResource(R.drawable.ic_bookmark_24px);
                }
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sharedPreferences.getStringSet(link_article, null) == null) {
                            bookmark_news.add("title:"+title_article);
                            bookmark_news.add("date:"+date_article);
                            bookmark_news.add("image:"+thumbnail_article);
                            bookmark_news.add("section:"+section_article);
                            bookmark_news.add("id:"+link_article);
                            bookmark_news.add("url:"+url_article);
                            editor.putStringSet(link_article,bookmark_news);
                            editor.commit();
                            button1.setImageResource(R.drawable.ic_bookmark_24px);
                            List<NewsDTO> list=mutableLiveData.getValue();
                            mutableLiveData.setValue(list);
                            Toast.makeText(context,shown_toast+"was added to bookmarks",Toast.LENGTH_LONG).show();
                        } else {
                            editor.remove(link_article);
                            editor.commit();
                            button1.setImageResource(R.drawable.favor_icon);
                            List<NewsDTO> list=mutableLiveData.getValue();
                            mutableLiveData.setValue(list);
                            Toast.makeText(context,shown_toast+" was removed from bookmarks",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent tweet = new Intent(Intent.ACTION_VIEW);
                        tweet.setData(Uri.parse("http://twitter.com/intent/tweet/?text="+"Check out this Link:"+url_article+"#CSCI571NewsSearch"));
                        context.startActivity(tweet);
                    }
                });
                dialog.show();
                return true;
            }
        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getStringSet(link_article, null) == null) {
                    bookmark_news.add("title:"+title_article);
                    bookmark_news.add("date:"+date_article);
                    bookmark_news.add("image:"+thumbnail_article);
                    bookmark_news.add("section:"+section_article);
                    bookmark_news.add("id:"+link_article);
                    bookmark_news.add("url:"+url_article);
                    editor.putStringSet(link_article,bookmark_news);
                    editor.commit();
                    holder.imageButton.setImageResource(R.drawable.ic_bookmark_24px);
                    Toast.makeText(context,shown_toast+"was added to bookmarks",Toast.LENGTH_LONG).show();
                } else {
                    editor.remove(link_article);
                    editor.commit();
                    holder.imageButton.setImageResource(R.drawable.ic_bookmark_border_24px);
                    Toast.makeText(context,shown_toast+" was removed from bookmarks",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView TextTitle, TextDate, Textsection;

        ImageView CardImage;

        ImageButton imageButton;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            TextTitle = itemView.findViewById(R.id.card_title);
            TextDate = itemView.findViewById(R.id.card_time);
            Textsection = itemView.findViewById(R.id.section_card);
            CardImage = itemView.findViewById(R.id.card_image);
            imageButton = itemView.findViewById(R.id.bookmark);
        }
    }
}
