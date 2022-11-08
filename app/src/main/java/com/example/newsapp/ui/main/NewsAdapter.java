package com.example.newsapp.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.data.model.Article;
import com.example.newsapp.data.sharedPreferences.NewsSP;
import com.example.newsapp.databinding.ItemNewsBinding;
import com.example.newsapp.ui.interfaces.OnBookmarkClick;
import com.example.newsapp.ui.interfaces.OnNewsClick;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    ItemNewsBinding binding;
    Context context;
    ArrayList<Article> article;
    OnNewsClick onNewsClick;
    OnBookmarkClick onBookmarkClick;


    @SuppressLint("NotifyDataSetChanged")
    public void setNewsList(Context context, ArrayList<Article> article, OnNewsClick onNewsClick, OnBookmarkClick onBookmarkClick) {
        this.context = context;
        this.article = article;
        this.onNewsClick = onNewsClick;
        this.onBookmarkClick = onBookmarkClick;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = ItemNewsBinding.inflate(layoutInflater, parent, false);
        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bindView(context, article.get(position), onNewsClick, onBookmarkClick);
    }

    @Override
    public int getItemCount() {
        return article.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        ItemNewsBinding binding;
        SharedPreferences sharedPreferences;
        NewsSP newsSP;


        public NewsViewHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            newsSP = new NewsSP(binding.getRoot().getContext());
        }

        public void bindView(Context context, Article article, OnNewsClick onNewsClick, OnBookmarkClick onBookmarkClick) {

            String publishAt = dateFormatted(article.getPublishedAt());

            binding.txtTitle.setText(article.getTitle());
            binding.txtTime.setText(publishAt);
            Glide.with(context).load(article.getUrlToImage()).into(binding.imgNews);


            bookmarkMarked();

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNewsClick.onNewsClick(getAdapterPosition());
                }
            });

            binding.imgBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBookmarkClick.onBookmarkClick(getAdapterPosition());
                    int i = newsSP.checkNewsSP(article);
                    bookmarkMarked();
                    if (i  > -1) {
//                        removeAt(i);
                    }
                }
            });
        }

        public String dateFormatted(String datetime) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
                Date date = dateFormat.parse(datetime);
                dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.US);
                return dateFormat.format(date);

            } catch (ParseException e) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                Date date = null;
                try {
                    date = dateFormat.parse(datetime);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm", Locale.US);
                return dateFormat.format(date);

            }
        }


        private void bookmarkMarked() {

            for (int i = 0; i < newsSP.showNewsSP().size(); i++) {
                if (newsSP.showNewsSP().get(i).getTitle().equals(binding.txtTitle.getText().toString())) {
                    binding.imgBookmark.setImageResource(R.drawable.ic_baseline_bookmark_yellow_24);
                    Log.d("kerooo", "onCreateView: " + newsSP.showNewsSP().get(i).getTitle());
                    return;
                }
            }
            binding.imgBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
            Log.d("kerooo", "onCreateView: " + "false");

        }

    }

    public void removeAt(int position) {
        article.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, article.size());
    }
}
