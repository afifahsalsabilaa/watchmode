package com.example.projectfinal.response;

import com.example.projectfinal.models.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    @SerializedName("movies")
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }


    public List<Movie> getResults() {
        return movies;
    }

}
