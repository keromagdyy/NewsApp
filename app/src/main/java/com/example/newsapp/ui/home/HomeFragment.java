package com.example.newsapp.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.newsapp.R;
import com.example.newsapp.data.model.Article;
import com.example.newsapp.data.sharedPreferences.NewsSP;
import com.example.newsapp.databinding.FragmentHomeBinding;
import com.example.newsapp.ui.interfaces.OnNewsClick;
import com.example.newsapp.ui.main.NewsAdapter;
import com.example.newsapp.ui.main.NewsViewModel;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment implements OnNewsClick {
    FragmentHomeBinding binding;
    NewsViewModel newsViewModel;
    NewsAdapter newsAdapter = new NewsAdapter();
    Article[] articles;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    NewsSP newsSP ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        sharedPreferences = getContext().getSharedPreferences("NEWS_BOOKMARK", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        newsSP = new NewsSP(requireContext());

        if (!isConnection()) {
            dialog();
        }


        newsViewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class);

        newsViewModel.getNewsByCountry("eg");
        newsViewModel.newsByCountryMLD.observe(requireActivity(), new Observer<Article[]>() {
            @Override
            public void onChanged(Article[] articles) {

                ArrayList<SlideModel> newsSliderList = getSliderList(articles);

                binding.newsSlider.setImageList(newsSliderList);

                binding.newsSlider.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onItemSelected(int position) {
                        HomeFragmentDirections.ActionHomeFragmentToDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(articles[position].getTitle(), articles[position].getDescription(), articles[position].getUrlToImage());
                        Navigation.findNavController(getView()).navigate(action);
                    }
                });

                Log.d("onChangedFun", "onChanged: " + articles[0].getTitle());
            }
        });


        newsViewModel.getNewsBySource("bbc-news,the-next-web");
        newsViewModel.newsBySourceMLD.observe(requireActivity(), new Observer<Article[]>() {
            @Override
            public void onChanged(Article[] articles) {
                HomeFragment.this.articles = articles;

                ArrayList<Article> articleList = new ArrayList();
                Collections.addAll(articleList, articles);

                newsAdapter.setNewsList(getContext(), articleList, HomeFragment.this);
                binding.recyclerView.setAdapter(newsAdapter);

                Log.d("onChangedFun", "onChanged: " + articles[0].getTitle());
            }
        });


        return binding.getRoot();
    }

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_message)
                .setNegativeButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().recreate();
                    }
                });
        builder.setCancelable(false);
        builder.show();

    }

    private ArrayList<SlideModel> getSliderList(Article[] articles) {
        ArrayList<SlideModel> sliderList = new ArrayList<>();
        for (int i = 0; i < articles.length; i++) {
            if (articles[i].getUrlToImage() != null)
                sliderList.add(new SlideModel(articles[i].getUrlToImage(), articles[i].getTitle(), ScaleTypes.CENTER_CROP));
            else
                sliderList.add(new SlideModel(R.drawable.img_default, articles[i].getTitle(), ScaleTypes.CENTER_CROP));
        }
        return sliderList;
    }

    @Override
    public void onNewsClick(int position) {
        HomeFragmentDirections.ActionHomeFragmentToDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(articles[position].getTitle(), articles[position].getDescription(), articles[position].getUrlToImage());
        Navigation.findNavController(getView()).navigate(action);
    }




    boolean isConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.isConnected())
                return true;
            else
                return false;
        } else
            return false;

    }

}