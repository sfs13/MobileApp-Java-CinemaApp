package com.example.cinemaapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaapp.R;
import com.example.cinemaapp.adapters.SearchAdapter;
import com.example.cinemaapp.api.JsonApi;
import com.example.cinemaapp.api.RetrofitInstance;
import com.example.cinemaapp.models.MovieModel;
import com.example.cinemaapp.models.Result;
import com.github.florent37.shapeofview.shapes.RoundRectView;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    public static EditText searchInput = null;
    static MovieModel search;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        page = 1;

        final JsonApi retrofitInstance = RetrofitInstance.getRetrofitInstance();

        final RoundRectView searchProgressBarInList = findViewById(R.id.searchProgressBarInList);
        final ProgressBar searchProgress = findViewById(R.id.searchProgress);
        final ImageView searchCheckMark = findViewById(R.id.searchCheckMark);
        final ProgressBar searchProgressBar = findViewById(R.id.searchProgressBar);

        final RecyclerView searchRecycler = findViewById(R.id.searchRecycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
        searchRecycler.setLayoutManager(layoutManager);
        searchRecycler.setAdapter(null);
        searchRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (search.getResults() != null && layoutManager.findLastVisibleItemPosition() == search.getResults().size() - 1 && page != -1 && searchProgressBarInList.getVisibility() == View.GONE) {
                    page++;

                    searchProgressBarInList.setVisibility(View.VISIBLE);
                    searchProgress.setVisibility(View.VISIBLE);

                    retrofitInstance.searchMovie(searchInput.getText().toString(), page).enqueue(new Callback<MovieModel>() {
                        @Override
                        public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                            if (response.body() != null) {
                                List<Result> resultList = response.body().getResults();

                                if (resultList.isEmpty()) {
                                    page = -1;

                                    searchProgressBarInList.setVisibility(View.GONE);

                                    return;
                                }

                                search.getResults().addAll(resultList);

                                searchRecycler.getAdapter().notifyItemInserted(search.getResults().size() - resultList.size());
                                searchRecycler.smoothScrollBy(0, recyclerView.getScrollY() + 150);

                                searchProgress.setVisibility(View.GONE);
                                searchCheckMark.setVisibility(View.VISIBLE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlphaAnimation animation = new AlphaAnimation(1, 0);
                                        animation.setDuration(300);
                                        animation.setRepeatCount(0);
                                        animation.setAnimationListener(new Animation.AnimationListener() {
                                            @Override
                                            public void onAnimationStart(Animation animation) {
                                            }

                                            @Override
                                            public void onAnimationEnd(Animation animation) {
                                                searchCheckMark.setVisibility(View.GONE);
                                                searchProgressBarInList.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onAnimationRepeat(Animation animation) {
                                            }
                                        });
                                        searchProgressBarInList.startAnimation(animation);
                                    }
                                }, 700);
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieModel> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        searchInput = findViewById(R.id.searchInput);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                searchRecycler.setAdapter(null);

                if (s.length() != 0) {
                    page = 1;

                    searchProgressBar.setVisibility(View.VISIBLE);

                    retrofitInstance.searchMovie(s.toString(), page).enqueue(new Callback<MovieModel>() {
                        @Override
                        public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                            if (response.body() != null) {
                                search = response.body();

                                for (int i = 0; i < search.getResults().size(); i++) {
                                    for (int j = 0; j < search.getResults().size(); j++) {
                                        if (i != j && search.getResults().get(i).getVoteAverage() > search.getResults().get(j).getVoteAverage() && search.getResults().get(i).getVoteCount() >= 100) {
                                            Collections.swap(search.getResults(), i, j);
                                        }
                                    }
                                }

                                searchRecycler.setAdapter(new SearchAdapter(search, SearchActivity.this));

                                searchProgressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<MovieModel> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.searchBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.super.onBackPressed();
            }
        });
    }
}