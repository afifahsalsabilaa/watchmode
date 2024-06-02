package com.example.projectfinal.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectfinal.R;
import com.example.projectfinal.adapter.MovieAdapter;
import com.example.projectfinal.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private static final String TAG = "FavoriteFragment";
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private List<Movie> favoriteMovies;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private FavoriteFragmentListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        Log.d(TAG, "onCreateView: started");

        recyclerView = view.findViewById(R.id.recyclerView);
        Log.d(TAG, "onCreateView: RecyclerView initialized");

        sharedPreferences = getContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);
        gson = new Gson();

        favoriteMovies = new ArrayList<>();
        movieAdapter = new MovieAdapter(getContext(), favoriteMovies);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        recyclerView.setAdapter(movieAdapter);
        Log.d(TAG, "onCreateView: Adapter set");

        return view;
    }

    private List<Movie> getFavoriteMoviesFromSharedPreferences() {
        String jsonFavorites = sharedPreferences.getString("favorites", null);
        if (jsonFavorites != null) {
            Type type = new TypeToken<List<Movie>>() {}.getType();
            return gson.fromJson(jsonFavorites, type);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        favoriteMovies = getFavoriteMoviesFromSharedPreferences();
        movieAdapter.setMovies(favoriteMovies);
        movieAdapter.notifyDataSetChanged();
    }

    public interface FavoriteFragmentListener {
        void onFavoriteUpdated();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FavoriteFragmentListener) {
            listener = (FavoriteFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProfileFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void onFavoriteUpdated() {
        if (listener != null) {
            favoriteMovies = getFavoriteMoviesFromSharedPreferences();
            movieAdapter.setMovies(favoriteMovies);
            movieAdapter.notifyDataSetChanged();
        }
    }

}
