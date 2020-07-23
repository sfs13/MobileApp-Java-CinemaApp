package com.example.cinemaapp.api;

import com.example.cinemaapp.models.CastModel;
import com.example.cinemaapp.models.GenreModel;
import com.example.cinemaapp.models.MovieModel;
import com.example.cinemaapp.models.PersonModel;
import com.example.cinemaapp.models.PictureModel;
import com.example.cinemaapp.models.Result;
import com.example.cinemaapp.models.VideoModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonApi {
    String key = "6ec432077f11687adceb28aea228f1cb";

    @GET("movie/now_playing?region=RU&language=ru-RU&api_key=" + key)
    Call<MovieModel> getNowPlaying(@Query("page") int page);

    @GET("movie/popular?region=RU&language=ru-RU&api_key=" + key)
    Call<MovieModel> getPopular(@Query("page") int page);

    @GET("movie/top_rated?region=RU&language=ru-RU&api_key=" + key)
    Call<MovieModel> getBest(@Query("page") int page);

    @GET("movie/upcoming?region=RU&language=ru-RU&api_key=" + key)
    Call<MovieModel> getUpcoming(@Query("page") int page);

    @GET("movie/{movie_id}?language=ru-RU&api_key=" + key)
    Call<Result> getMovieById(@Path("movie_id") String movie_id);

    @GET("movie/{movie_id}/credits?language=ru-RU&api_key=" + key)
    Call<CastModel> getCast(@Path("movie_id") String movie_id);

    @GET("credit/{credit_id}?language=ru-RU&api_key=" + key)
    Call<PersonModel> getPerson(@Path("credit_id") String credit_id);

    @GET("genre/movie/list?language=ru-RU&api_key=" + key)
    Call<GenreModel> getGenres();

    @GET("search/movie?language=ru-RU&api_key=" + key)
    Call<MovieModel> searchMovie(@Query("query") String query, @Query("page") int page);

    @GET("movie/{movie_id}/videos?language=ru-RU&api_key=" + key)
    Call<VideoModel> getVideos(@Path("movie_id") String movie_id);

    @GET("movie/{movie_id}/similar?language=ru-RU&api_key=" + key)
    Call<MovieModel> getSimilarMovies(@Path("movie_id") String movie_id);

    @GET("movie/{movie_id}/images?api_key=" + key)
    Call<PictureModel> getPictures(@Path("movie_id") String movie_id);
}