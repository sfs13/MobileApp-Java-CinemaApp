package com.example.cinemaapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaapp.R;
import com.example.cinemaapp.adapters.SearchAdapter;
import com.example.cinemaapp.models.MovieModel;
import com.example.cinemaapp.models.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

@SuppressLint("StaticFieldLeak")
public class FavoriteActivity extends AppCompatActivity {

    static RecyclerView favoriteRecycler;
    static Context context;
    static MovieModel favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        favoriteRecycler = findViewById(R.id.favoriteRecycler);
        favoriteRecycler.setLayoutManager(new LinearLayoutManager(this));

        context = this;

        SharedPreferences prefs = getSharedPreferences("data", MODE_PRIVATE);
        if (prefs.contains("favoriteList")) {
            List<Result> favoriteList = new Gson().fromJson(prefs.getString("favoriteList", ""), new TypeToken<List<Result>>() {
            }.getType());

            favorite = new MovieModel(favoriteList);
            favoriteRecycler.setAdapter(new SearchAdapter(favorite, this));
        }

        findViewById(R.id.favoriteBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteActivity.super.onBackPressed();
            }
        });
    }
}