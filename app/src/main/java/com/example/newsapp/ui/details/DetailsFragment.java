package com.example.newsapp.ui.details;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.data.sharedPreferences.NewsSP;
import com.example.newsapp.databinding.FragmentDetailsBinding;

public class DetailsFragment extends Fragment {
    FragmentDetailsBinding binding;
    DetailsFragmentArgs args;
    NewsSP newsSP;
    String title;
    String content;
    String img;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_details, container, false);

        if (getArguments() != null) {
            args = DetailsFragmentArgs.fromBundle(getArguments());
        }

        newsSP = new NewsSP(binding.getRoot().getContext());

        title = args.getTitle();
        content = args.getContent();
        img = args.getImg();

        binding.txtTitle.setText(title);
        binding.txtNews.setText(content);
        if (img != null)
            Glide.with(requireContext()).load(img).into(binding.imgNews);
        else
            binding.imgNews.setImageResource(R.drawable.img_default);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });




        return binding.getRoot();
    }


}