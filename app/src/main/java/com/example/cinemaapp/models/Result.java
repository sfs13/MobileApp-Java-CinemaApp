package com.example.cinemaapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("genre_ids")
    private List<Long> genreIds;

    @Expose
    private Long id;

    @SerializedName("original_language")
    private String originalLanguage;

    @Expose
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @Expose
    private String title;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("vote_count")
    private Long voteCount;

    @SerializedName("budget")
    private Long budget;

    @SerializedName("runtime")
    private Long runtime;

    public Long getBudget() {
        return budget;
    }

    public Long getRuntime() {
        return runtime;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public Long getId() {
        return id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Long getVoteCount() {
        return voteCount;
    }
}
