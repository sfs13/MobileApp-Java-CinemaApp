package com.example.cinemaapp.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class GenreModel {
    @Expose
    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }
}
