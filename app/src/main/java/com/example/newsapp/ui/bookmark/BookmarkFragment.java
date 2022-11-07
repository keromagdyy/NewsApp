package com.example.newsapp.ui.bookmark;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsapp.R;
import com.example.newsapp.data.model.Article;
import com.example.newsapp.databinding.FragmentBookmarkBinding;
import com.example.newsapp.ui.main.NewsAdapter;
import com.example.newsapp.ui.interfaces.OnNewsClick;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class BookmarkFragment extends Fragment implements OnNewsClick {
    FragmentBookmarkBinding binding;
    NewsAdapter newsAdapter = new NewsAdapter();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Article[] articles;
    ArrayList<Article> articlesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false);

        sharedPreferences = getContext().getSharedPreferences("NEWS_BOOKMARK", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("article_bookmark", null);
        Type type = new TypeToken<ArrayList<Article>>(){}.getType();

        articlesList = gson.fromJson(json, type);

        if (articlesList == null) {
            articlesList = new ArrayList<>();
        }

        Article[] articles = new Article[articlesList.size()];
        articles = articlesList.toArray(articles);

        Log.d("kerooo", "onCreateView: " + articles.length);

        ArrayList<Article> articleList = new ArrayList();
        Collections.addAll(articleList, articles);

        newsAdapter.setNewsList(getContext(), articleList, BookmarkFragment.this);
        binding.recyclerView.setAdapter(newsAdapter);

        return binding.getRoot();
    }


    @Override
    public void onNewsClick(int position) {
        BookmarkFragmentDirections.ActionFavoriteFragmentToDetailsFragment action = BookmarkFragmentDirections.actionFavoriteFragmentToDetailsFragment(articlesList.get(position).getTitle(), articlesList.get(position).getContent(), articlesList.get(position).getUrlToImage());
        Navigation.findNavController(getView()).navigate(action);

    }

}