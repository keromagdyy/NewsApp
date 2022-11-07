package com.example.newsapp.data.remote;

import com.example.newsapp.data.api.NewsApiInterface;
import com.example.newsapp.data.model.NewsModel;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsClient {
    private static final String BASE_URL = "https://newsapi.org/v2/";
    private NewsApiInterface newsApiInterface;
    private static NewsClient INSTANCE;
    private static final String API_KEY = "bb45029a8e39462199083cb3e97a05d4";

    public NewsClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(3000, TimeUnit.SECONDS)
                .writeTimeout(3000, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        newsApiInterface = retrofit.create(NewsApiInterface.class);

    }

    public static NewsClient getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new NewsClient();

        return INSTANCE;
    }

    public Observable<NewsModel> getNewsByCountry(String country) {
        return newsApiInterface.getNewsByCountry(country, API_KEY);
    }

    public Observable<NewsModel> getNewsBySource(String source) {
        return newsApiInterface.getNewsBySource(source, API_KEY);
    }

    public Observable<NewsModel> getNewsSearch(String q) {
        return newsApiInterface.getNewsSearch(q, API_KEY);
    }
}
