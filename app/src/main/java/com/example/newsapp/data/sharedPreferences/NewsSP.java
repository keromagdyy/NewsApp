package com.example.newsapp.data.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.newsapp.data.model.Article;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NewsSP {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    ArrayList<Article> articlesBookmarkList = new ArrayList<>();

    public NewsSP(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("NEWS_BOOKMARK", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public int checkNewsSP(Article article) {
        articlesBookmarkList = showNewsSP();

        Gson gson = new Gson();

        for (int i = 0; i < articlesBookmarkList.size(); i++) {
            if (articlesBookmarkList.get(i).getTitle().equals(article.getTitle())) {
                articlesBookmarkList.remove(i);
                String json = gson.toJson(articlesBookmarkList);
                editor.putString("article_bookmark", json);
                editor.apply();
                return i;
            }
        }

        articlesBookmarkList.add(article);
        String json = gson.toJson(articlesBookmarkList);
        editor.putString("article_bookmark", json);
        editor.apply();
        return -1;
    }

    public ArrayList<Article> showNewsSP() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("article_bookmark", null);
        Type type = new TypeToken<ArrayList<Article>>() {
        }.getType();

        ArrayList<Article> articlesList = gson.fromJson(json, type);

        return articlesList;
    }

}
