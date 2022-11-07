package com.example.newsapp.ui.main;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newsapp.data.model.Article;
import com.example.newsapp.data.model.NewsModel;
import com.example.newsapp.data.remote.NewsClient;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsViewModel extends ViewModel {
    public MutableLiveData<Article[]> newsByCountryMLD = new MutableLiveData<>();
    public MutableLiveData<Article[]> newsBySourceMLD = new MutableLiveData<>();
    public MutableLiveData<Article[]> newsSearchMLD = new MutableLiveData<>();

    public void getNewsByCountry(String country) {

        Observable<NewsModel> observable = NewsClient.getINSTANCE().getNewsByCountry(country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(news -> newsByCountryMLD.setValue(news.getArticles()), e->Log.d("kerooo", "onFailure: " + e.getMessage()));

    }


    public void getNewsBySource(String source) {

        Observable<NewsModel> observable = NewsClient.getINSTANCE().getNewsBySource(source)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(news -> newsBySourceMLD.setValue(news.getArticles()), e->Log.d("kerooo", "onFailure: " + e.getMessage()));


    }


    public void getNewsSearch(String q) {

        Observable<NewsModel> observable = NewsClient.getINSTANCE().getNewsSearch(q)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(news -> newsSearchMLD.setValue(news.getArticles()), e->Log.d("kerooo", "onFailure: " + e.getMessage()));


    }

}


