package com.example.cinemaapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaapp.R;
import com.example.cinemaapp.activity.MainActivity;
import com.example.cinemaapp.adapters.GenreAdapter;
import com.example.cinemaapp.adapters.SlideAdapter;
import com.example.cinemaapp.api.JsonApi;
import com.example.cinemaapp.api.RetrofitInstance;
import com.example.cinemaapp.models.Genre;
import com.example.cinemaapp.models.GenreModel;
import com.example.cinemaapp.models.MovieModel;
import com.example.cinemaapp.models.Result;
import com.github.florent37.shapeofview.shapes.RoundRectView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSlide extends Fragment {
    public static MovieModel movie;
    public static int currentSlide = 1;
    public static RecyclerView slideRecycler;
    public static RecyclerView genresRecycler;
    public static RoundRectView slideGoBack;
    static int page;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_slide, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentSlide = bundle.getInt("currentSlide");
        }

        final JsonApi retrofitInstance = RetrofitInstance.getRetrofitInstance();

        page = 1;
        movie = new MovieModel(new ArrayList<Result>());

        genresRecycler = view.findViewById(R.id.genresRecycler);
        genresRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        slideRecycler = view.findViewById(R.id.slideRecycler);
        slideRecycler.setLayoutManager(layoutManager);

        slideGoBack = view.findViewById(R.id.slideGoBack);
        slideGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getContext()) {
                    @Override
                    protected int getHorizontalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };
                smoothScroller.setTargetPosition(0);
                layoutManager.startSmoothScroll(smoothScroller);
            }
        });

        final ProgressBar slideProgressBar = view.findViewById(R.id.slideProgressBar);
        final RoundRectView slideProgressBarInList = view.findViewById(R.id.slideProgressBarInList);
        final ProgressBar slideProgress = view.findViewById(R.id.slideProgress);
        final ImageView slideCheckMark = view.findViewById(R.id.slideCheckMark);

        slideProgressBar.setVisibility(View.VISIBLE);

        Call<MovieModel> slideCall;
        switch (MainActivity.slides[currentSlide - 1]) {
            case "В прокате":
            default: {
                slideCall = retrofitInstance.getNowPlaying(page);
                break;
            }
            case "Популярное": {
                slideCall = retrofitInstance.getPopular(page);
                break;
            }
            case "Лучшее": {
                slideCall = retrofitInstance.getBest(page);
                break;
            }
            case "Скоро": {
                slideCall = retrofitInstance.getUpcoming(page);
                break;
            }
        }
        slideCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.body() != null) {
                    movie = response.body();

                    slideRecycler.setAdapter(new SlideAdapter(movie, getContext(), false));

                    slideProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                t.printStackTrace();

                getFragmentManager().beginTransaction()
                        .replace(R.id.mainFrame, new FragmentError())
                        .commit();
            }
        });

        slideRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (layoutManager.findFirstVisibleItemPosition() >= 1 && slideGoBack.getVisibility() == View.GONE) {
                    slideGoBack.setVisibility(View.VISIBLE);

                    AlphaAnimation animation = new AlphaAnimation(0, 1);
                    animation.setDuration(300);
                    animation.setRepeatCount(0);
                    slideGoBack.startAnimation(animation);
                } else if (layoutManager.findFirstVisibleItemPosition() < 1 && slideGoBack.getVisibility() == View.VISIBLE) {
                    AlphaAnimation animation = new AlphaAnimation(1, 0);
                    animation.setDuration(300);
                    animation.setRepeatCount(0);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            slideGoBack.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    slideGoBack.startAnimation(animation);
                }

                if (page > 1 && movie.getResults().size() <= 20)
                    return;

                Call<MovieModel> newSlideCall;
                switch (currentSlide) {
                    case 1:
                    default: {
                        newSlideCall = retrofitInstance.getNowPlaying(page + 1);
                        break;
                    }
                    case 2: {
                        newSlideCall = retrofitInstance.getPopular(page + 1);
                        break;
                    }
                    case 3: {
                        newSlideCall = retrofitInstance.getBest(page + 1);
                        break;
                    }
                    case 4: {
                        newSlideCall = retrofitInstance.getUpcoming(page + 1);
                        break;
                    }
                }
                if (!newSlideCall.isExecuted() && slideProgress.getVisibility() == View.GONE) {
                    if (layoutManager.findLastVisibleItemPosition() == movie.getResults().size() - 1) {
                        slideProgressBarInList.setVisibility(View.VISIBLE);
                        slideProgress.setVisibility(View.VISIBLE);
                        slideCheckMark.setVisibility(View.GONE);

                        page++;

                        newSlideCall.enqueue(new Callback<MovieModel>() {
                            @Override
                            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                                if (response.body() != null) {
                                    List<Result> results = response.body().getResults();

                                    movie.getResults().addAll(results);

                                    slideRecycler.getAdapter().notifyItemInserted(movie.getResults().size() - results.size());

                                    recyclerView.smoothScrollBy(recyclerView.getScrollX() + 150, 0);

                                    slideProgress.setVisibility(View.GONE);
                                    slideCheckMark.setVisibility(View.VISIBLE);

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
                                                    slideProgressBarInList.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onAnimationRepeat(Animation animation) {
                                                }
                                            });
                                            slideProgressBarInList.startAnimation(animation);
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
            }
        });

        retrofitInstance.getGenres().enqueue(new Callback<GenreModel>() {
            @Override
            public void onResponse(Call<GenreModel> call, Response<GenreModel> response) {
                if (response.body() != null) {
                    response.body().getGenres().add(0, new Genre(0L, "Все"));
                    genresRecycler.setAdapter(new GenreAdapter(response.body(), getContext(), 0));
                }
            }

            @Override
            public void onFailure(Call<GenreModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return view;
    }
}
