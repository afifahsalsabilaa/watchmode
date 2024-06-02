package com.example.projectfinal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectfinal.DetailActivity;
import com.example.projectfinal.R;
import com.example.projectfinal.adapter.SearchAdapter;
import com.example.projectfinal.api.ApiConfig;
import com.example.projectfinal.api.ApiService;
import com.example.projectfinal.models.Movie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements SearchAdapter.OnMovieClickListener {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchAdapter searchAdapter;
    private Handler handler;
    private Runnable searchRunnable;
    private String lastQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);

        // Set up RecyclerView and adapter
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        searchAdapter = new SearchAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(searchAdapter);

        handler = new Handler(Looper.getMainLooper());
        searchRunnable = () -> {
            String query = searchView.getQuery().toString();
            if (!query.equals(lastQuery)) {
                lastQuery = query;
                if (query.isEmpty()) {
                    searchAdapter.setMovieList(new ArrayList<>());
                } else {
                    searchMovie(query);
                }
            }
        };

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handler.removeCallbacks(searchRunnable);
                handler.post(searchRunnable);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handler.removeCallbacks(searchRunnable);
                handler.postDelayed(searchRunnable, 300); // Debounce with 300ms delay
                return false;
            }
        });

        return view;
    }

    private void searchMovie(String query) {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = ApiConfig.getRetrofit().create(ApiService.class);
        Call<List<Movie>> call = apiService.getMovies();

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> filteredMovies = filterMovies(response.body(), query);
                    searchAdapter.setMovieList(filteredMovies);
                    if (filteredMovies.isEmpty()) {
                        showErrorMessage("No movies found");
                    }
                } else {
                    showErrorMessage("Failed to fetch movies");
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                showErrorMessage("Failed to connect to server: " + t.getMessage());
            }
        });
    }

    private List<Movie> filterMovies(List<Movie> allMovies, String query) {
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : allMovies) {
            if (movie.getTitle() != null && movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("movie_image", movie.getImage());
        intent.putExtra("movie_title", movie.getTitle());
        intent.putExtra("movie_rating", String.valueOf(movie.getRating())); // Convert to String if necessary
        intent.putExtra("movie_description", movie.getDescription());
        startActivity(intent);
    }


    private void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
