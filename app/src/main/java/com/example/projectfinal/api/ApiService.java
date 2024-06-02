package com.example.projectfinal.api;

import com.example.projectfinal.models.Movie;
import com.example.projectfinal.response.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {
    @Headers({
            "X-RapidAPI-Key: a0bd4b2717msh0cf7724c766dba2p16f589jsn4d73e2e80d9d",
            "X-RapidAPI-Host: imdb-top-100-movies.p.rapidapi.com"
    })

    @GET("/")
    Call<List<Movie>> getMovies();

    @GET("/search")
    Call<Movie> searchMovies(@Query("query") String query);
}
