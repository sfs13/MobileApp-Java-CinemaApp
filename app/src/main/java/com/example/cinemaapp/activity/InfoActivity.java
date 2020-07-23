package com.example.cinemaapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.cinemaapp.R;
import com.example.cinemaapp.adapters.CastAdapter;
import com.example.cinemaapp.adapters.PicturesAdapter;
import com.example.cinemaapp.adapters.SearchAdapter;
import com.example.cinemaapp.adapters.SlideAdapter;
import com.example.cinemaapp.api.JsonApi;
import com.example.cinemaapp.api.RetrofitInstance;
import com.example.cinemaapp.fragments.FragmentSlide;
import com.example.cinemaapp.models.Cast;
import com.example.cinemaapp.models.CastModel;
import com.example.cinemaapp.models.Genre;
import com.example.cinemaapp.models.GenreModel;
import com.example.cinemaapp.models.MovieModel;
import com.example.cinemaapp.models.PictureModel;
import com.example.cinemaapp.models.Result;
import com.example.cinemaapp.models.VideoModel;
import com.example.cinemaapp.models.VideoResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoActivity extends AppCompatActivity {
    static MovieModel similarList;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        JsonApi retrofitInstance = RetrofitInstance.getRetrofitInstance();

        Bundle bundle = getIntent().getExtras();
        boolean isSearch = bundle.getBoolean("isSearch");
        boolean isFavorite = bundle.getBoolean("isFavorite");
        boolean isSimilarList = bundle.getBoolean("isSimilarList");
        boolean callFromGenres = bundle.getBoolean("callFromGenres");

        final ProgressBar imageProgressBar = findViewById(R.id.imageProgressBar);
        final TextView imageNotFound = findViewById(R.id.imageNotFound);
        final ImageView infoImage = findViewById(R.id.infoImage);
        final CardView infoTrailer = findViewById(R.id.infoTrailer);
        final TextView infoDuration = findViewById(R.id.infoDuration);
        final TextView infoBudget = findViewById(R.id.infoBudget);
        final ImageView infoFavorite = findViewById(R.id.infoFavorite);
        final LinearLayout infoGenres = findViewById(R.id.infoGenres);
        TextView infoRating = findViewById(R.id.infoRating);
        TextView infoTitle = findViewById(R.id.infoTitle);
        TextView infoRealiseDate = findViewById(R.id.infoRealiseDate);
        TextView infoOriginalLanguage = findViewById(R.id.infoOriginalLanguage);
        TextView infoPlot = findViewById(R.id.infoPlot);

        imageProgressBar.setVisibility(View.VISIBLE);

        final List<Result> results;
        if (isSearch)
            results = SearchActivity.search.getResults();
        else if (isFavorite)
            results = FavoriteActivity.favorite.getResults();
        else if (isSimilarList)
            results = similarList.getResults();
        else if (callFromGenres)
            results = SlideAdapter.movie.getResults();
        else
            results = FragmentSlide.movie.getResults();

        final Result currentMovie = results.get(bundle.getInt("currentMovie"));
        String currentMovieId = currentMovie.getId().toString();

        final Gson gson = new Gson();
        final SharedPreferences prefs = getSharedPreferences("data", MODE_PRIVATE);

        if (prefs.contains("favoriteList")) {
            List<Result> favoriteList = gson.fromJson(prefs.getString("favoriteList", ""), new TypeToken<List<Result>>() {
            }.getType());

            for (Result i : favoriteList) {
                if (i.getId().equals(currentMovie.getId())) {
                    infoFavorite.setImageResource(R.drawable.ic_heart_white);

                    break;
                }
            }
        }

        infoRating.setText(String.format("%s/5\nГолосов: %s", currentMovie.getVoteAverage() / 2, currentMovie.getVoteCount()));
        infoTitle.setText(currentMovie.getTitle());

        if (currentMovie.getReleaseDate() != null && !currentMovie.getReleaseDate().equals(""))
            infoRealiseDate.setText(currentMovie.getReleaseDate());
        else
            infoRealiseDate.setVisibility(View.GONE);

        if (currentMovie.getOriginalLanguage() != null && !currentMovie.getOriginalLanguage().equals(""))
            infoOriginalLanguage.setText(currentMovie.getOriginalLanguage().toUpperCase());
        else
            infoOriginalLanguage.setVisibility(View.GONE);

        retrofitInstance.getMovieById(currentMovieId).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null) {
                    Long runtime = response.body().getRuntime();
                    Long budget = response.body().getBudget();

                    if (runtime != null && runtime != 0) {
                        int hours = (int) (runtime / 60);
                        int minutes = (int) (runtime - hours * 60);

                        infoDuration.setText(String.format("%dh %s%dmin", hours, (minutes < 10) ? ("0") : (""), minutes));
                    } else
                        infoDuration.setVisibility(View.GONE);

                    if (budget != null && budget != 0)
                        infoBudget.setText(budget.toString() + "$");
                    else
                        infoBudget.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                t.printStackTrace();
            }
        });

        if (currentMovie.getOverview() == null || currentMovie.getOverview().equals(""))
            infoPlot.setText("Отсутствует");
        else
            infoPlot.setText(currentMovie.getOverview());

        retrofitInstance.getVideos(currentMovieId).enqueue(new Callback<VideoModel>() {
            @Override
            public void onResponse(Call<VideoModel> call, final Response<VideoModel> response) {
                if (response.body() != null) {
                    final List<VideoResult> videoResults = response.body().getResults();
                    if (!videoResults.isEmpty() && videoResults.get(0).getSite().equals("YouTube")) {
                        infoTrailer.setVisibility(View.VISIBLE);
                        infoTrailer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + videoResults.get(0).getKey())));
                            }
                        });

                        AlphaAnimation animation = new AlphaAnimation(0, 1);
                        animation.setDuration(300);
                        animation.setRepeatCount(0);
                        infoTrailer.startAnimation(animation);
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

        retrofitInstance.getGenres().enqueue(new Callback<GenreModel>() {
            @Override
            public void onResponse(Call<GenreModel> call, Response<GenreModel> response) {
                if (response.body() != null) {
                    if (currentMovie.getGenreIds() != null && !currentMovie.getGenreIds().isEmpty()) {
                        for (Long i : currentMovie.getGenreIds()) {
                            for (Genre j : response.body().getGenres()) {
                                if (i.equals(j.getId())) {
                                    View genre = getLayoutInflater().inflate(R.layout.genre_button, null);

                                    TextView genreText = genre.findViewById(R.id.genreText);
                                    genreText.setText(StringUtils.capitalize(j.getName()));

                                    infoGenres.addView(genre);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GenreModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

        final RecyclerView cast_recycler = findViewById(R.id.castRecycler);
        cast_recycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        cast_recycler.setLayoutManager(new GridLayoutManager(InfoActivity.this, 4));

        retrofitInstance.getCast(currentMovieId).enqueue(new Callback<CastModel>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onResponse(Call<CastModel> call, Response<CastModel> response) {
                if (response.body() != null) {
                    List<Cast> castList = response.body().getCast();

                    if (castList.size() > 12)
                        cast_recycler.setAdapter(new CastAdapter(new CastModel(castList.subList(0, 12)), InfoActivity.this));
                    else
                        cast_recycler.setAdapter(new CastAdapter(response.body(), InfoActivity.this));

                    if (castList.isEmpty()) {
                        cast_recycler.setVisibility(View.GONE);
                        findViewById(R.id.castNotFound).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CastModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w1280" + currentMovie.getBackdropPath())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        imageNotFound.setVisibility(View.VISIBLE);
                        imageProgressBar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageNotFound.setVisibility(View.GONE);

                        return false;
                    }
                })
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(infoImage);

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoActivity.super.onBackPressed();
            }
        });

        infoFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editor editor = prefs.edit();
                List<Result> favoriteList;

                if (prefs.contains("favoriteList")) {
                    favoriteList = gson.fromJson(prefs.getString("favoriteList", ""), new TypeToken<List<Result>>() {
                    }.getType());

                    int indexOfMovie = -1;
                    for (Result i : favoriteList) {
                        if (i.getId().equals(currentMovie.getId())) {
                            indexOfMovie = favoriteList.indexOf(i);

                            break;
                        }
                    }

                    if (indexOfMovie != -1) {
                        favoriteList.remove(indexOfMovie);

                        if (FavoriteActivity.favoriteRecycler != null) {
                            FavoriteActivity.favoriteRecycler.setAdapter(new SearchAdapter(new MovieModel(favoriteList), FavoriteActivity.context));
                            FavoriteActivity.favoriteRecycler.scrollToPosition(indexOfMovie);
                        }

                        infoFavorite.setImageResource(R.drawable.ic_heart);

                        Toast.makeText(InfoActivity.this, "Удалено из избранного", Toast.LENGTH_SHORT).show();
                    } else {
                        favoriteList.add(0, currentMovie);

                        infoFavorite.setImageResource(R.drawable.ic_heart_white);

                        Toast.makeText(InfoActivity.this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                    }

                    FavoriteActivity.favorite = new MovieModel(favoriteList);
                } else {
                    favoriteList = new ArrayList<>();
                    favoriteList.add(0, currentMovie);

                    FavoriteActivity.favorite = new MovieModel(favoriteList);

                    infoFavorite.setImageResource(R.drawable.ic_heart_white);

                    Toast.makeText(InfoActivity.this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                }

                editor.putString("favoriteList", gson.toJson(favoriteList));
                editor.apply();
            }
        });

        final RecyclerView infoSimilar = findViewById(R.id.infoSimilar);
        infoSimilar.setLayoutManager(new LinearLayoutManager(this));
        infoSimilar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        retrofitInstance.getSimilarMovies(currentMovieId).enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.body() != null) {
                    List<Result> resultList = response.body().getResults();

                    if (resultList.size() > 3)
                        similarList = new MovieModel(resultList.subList(0, 3));
                    else
                        similarList = new MovieModel(resultList);

                    infoSimilar.setAdapter(new SearchAdapter(similarList, InfoActivity.this));

                    if (similarList.getResults().size() == 0) {
                        infoSimilar.setVisibility(View.GONE);
                        findViewById(R.id.similarNotFound).setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

        retrofitInstance.getPictures(currentMovieId).enqueue(new Callback<PictureModel>() {
            @Override
            public void onResponse(Call<PictureModel> call, Response<PictureModel> response) {
                if (response.body() != null) {
                    ViewPager picturesViewPager = findViewById(R.id.picturesViewPager);

                    if (response.body().getBackdrops().isEmpty()) {
                        findViewById(R.id.picturesNotFound).setVisibility(View.VISIBLE);
                        picturesViewPager.setVisibility(View.GONE);
                    } else
                        picturesViewPager.setAdapter(new PicturesAdapter(InfoActivity.this, response.body()));
                }
            }

            @Override
            public void onFailure(Call<PictureModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}