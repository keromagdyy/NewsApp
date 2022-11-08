package com.example.newsapp.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.newsapp.R;
import com.example.newsapp.data.model.Article;
import com.example.newsapp.databinding.FragmentSearchBinding;
import com.example.newsapp.ui.interfaces.OnBookmarkClick;
import com.example.newsapp.ui.main.NewsAdapter;
import com.example.newsapp.ui.main.NewsViewModel;
import com.example.newsapp.ui.interfaces.OnNewsClick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SearchFragment extends Fragment implements OnNewsClick, OnBookmarkClick {
    FragmentSearchBinding binding;
    NewsViewModel newsViewModel;
    NewsAdapter newsAdapter = new NewsAdapter();
    Article[] articles = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<Article> articlesBookmarkList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search,container,false);

        newsViewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);

        sharedPreferences = getContext().getSharedPreferences("NEWS_BOOKMARK", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        binding.txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Search(s);
                Log.d("onChangedKero", "onChanged: " + s);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });



        return binding.getRoot();
    }

    private void Search(String s) {
        String q = s.toLowerCase(Locale.getDefault());
        if (!q.equals("") && q.equals(binding.txtSearch.getQuery().toString())) {
            newsViewModel.getNewsSearch(q);
            newsViewModel.newsSearchMLD.observe(requireActivity(), new Observer<Article[]>() {
                @Override
                public void onChanged(Article[] articles) {

                    SearchFragment.this.articles = articles;

                    ArrayList<Article> articleList = new ArrayList();
                    Collections.addAll(articleList, articles);

                    newsAdapter.setNewsList(getContext(), articleList, SearchFragment.this, SearchFragment.this);
                    binding.recyclerView.setAdapter(newsAdapter);
                    newsAdapter.notifyDataSetChanged();
                    if (articles.length == 0) {
                        binding.imgNoData.setVisibility(View.VISIBLE);
                    } else {
                        binding.imgNoData.setVisibility(View.GONE);
                    }

                }
            });
        }
    }


    @Override
    public void onNewsClick(int position) {
        SearchFragmentDirections.ActionSearchFragmentToDetailsFragment action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(articles[position].getTitle(), articles[position].getDescription(), articles[position].getUrlToImage());
        Navigation.findNavController(getView()).navigate(action);
    }


    @Override
    public void onBookmarkClick(int position) {

    }
}