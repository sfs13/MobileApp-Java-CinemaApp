package com.example.cinemaapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieModel {
    @SerializedName("results")
    private List<Result> results;

    public MovieModel(List<Result> results) {
        this.results = results;
    }

    public List<Result> getResults() {
        return results;
    }
}
