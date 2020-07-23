package com.example.cinemaapp.models;

import com.google.gson.annotations.Expose;

public class PersonModel {
    @Expose
    private Person person;

    public Person getPerson() {
        return person;
    }
}
