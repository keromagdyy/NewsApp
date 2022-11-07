package com.example.newsapp.data.api;

import com.example.newsapp.data.model.NewsModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiInterface {

    @GET("top-headlines")
    public Observable<NewsModel> getNewsByCountry(@Query("country") String country, @Query("apiKey") String apiKey);

    @GET("top-headlines")
    public Observable<NewsModel> getNewsBySource(@Query("sources") String sources, @Query("apiKey") String apiKey);

    @GET("top-headlines")
    public Observable<NewsModel> getNewsSearch(@Query("q") String q, @Query("apiKey") String apiKey);
}
