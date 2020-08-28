package com.example.homeworkapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.ContactsContract;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.FavorViewHolder> {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private List<String> favors = new ArrayList<>();

    public MutableLiveData<List<String>> mutableLiveData = null;

    public BookmarkAdapter(Context context) {
        this.context = context;
    }

    public void setfavors(List<String> list) {
        this.favors = list;
    }

    @NonNull
    @Override
    public FavorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        sharedPreferences = context.getSharedPreferences("Bookmarks", 0);
        editor = sharedPreferences.edit();
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.bookmark, parent, false);
        return new FavorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavorViewHolder holder, int position) {
//        String title
        String favor_name = this.favors.get(position);
        String link = "";
        String favor_title = "", favor_image = "", link_article = "";
        for (String str : Objects.requireNonNull(sharedPreferences.getStringSet(favor_name, null))) {
            if (str.substring(0, 5).equals("title")) {
                favor_title = str.substring(6);
                holder.favor_title.setText(favor_title);
            } else if (str.substring(0, 4).equals("date")) {
                holder.favor_date.setText(str.substring(5,str.length()-4));
            } else if (str.substring(0, 7).equals("section")) {
                holder.favor_source.setText(str.substring(8));
            } else if (str.substring(0, 5).equals("image")) {
                favor_image = str.substring(6);
                Picasso.with(context).load(str.substring(6)).resize(195,150).into(holder.favor_img);
            } else if (str.substring(0, 2).equals("id")) {
                link = str.substring(3);
            } else if (str.substring(0, 3).equals("url")) {
                link_article = str.substring(4);
            }
        }
        String finalLink = link;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra("id", finalLink);
                v.getContext().startActivity(intent);
            }
        });
        String finalFavor_title = favor_title;
        String finalFavor_image = favor_image;
        String finalLink1 = link;
        Set<String> bookmark_news = new HashSet<>();
        String finalLink_article = link_article;
        String toast_title = "\"" + finalFavor_title + "\"";
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_longclick);
                TextView textView = dialog.findViewById(R.id.dialog_title);
                textView.setText(finalFavor_title);
                ImageButton button1 = dialog.findViewById(R.id.dialog_favor);
                ImageButton button2 = dialog.findViewById(R.id.dialog_share);
                ImageView imageView = dialog.findViewById(R.id.dialog_pic);
                Picasso.with(context).load(finalFavor_image).resize(1200,500).into(imageView);
                button1.setImageResource(R.drawable.ic_bookmark_24px);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editor.remove(finalLink1);
                        editor.commit();
                        button1.setImageResource(R.drawable.favor_icon);
//                        String toast_title="\""+finalFavor_title+"\"";
                        DataRepository.getNews_favor(mutableLiveData);
                        Toast.makeText(context, toast_title + " was removed from bookmarks", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent tweet = new Intent(Intent.ACTION_VIEW);
                        tweet.setData(Uri.parse("http://twitter.com/intent/tweet/?text=" + "Check out this Link:" + finalLink_article+"#CSCI571NewsSearch"));
                        context.startActivity(tweet);
                    }
                });
                dialog.show();
                return true;
            }
        });
        holder.favor_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove(favor_name);
                editor.commit();
                DataRepository.getNews_favor(mutableLiveData);
                Toast.makeText(context, toast_title + " was removed from bookmarks", Toast.LENGTH_LONG).show();
            }
        });
//        holder.favor_title.setText(this.title);
//        holder.favor_date.setText(this.date);
    }

    @Override
    public int getItemCount() {
        return favors.size();
    }

    static class FavorViewHolder extends RecyclerView.ViewHolder {
        TextView favor_title, favor_date, favor_source;
        ImageView favor_img;
        ImageButton favor_button;

        public FavorViewHolder(@NonNull View itemView) {
            super(itemView);
            favor_title = itemView.findViewById(R.id.bookmark_title);
            favor_date = itemView.findViewById(R.id.favor_date);
            favor_source = itemView.findViewById(R.id.favor_section);
            favor_img = itemView.findViewById(R.id.favor_image);
            favor_button = itemView.findViewById(R.id.collection_button);
        }
    }
}
