package com.example.projectfinal.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.projectfinal.R;
import com.example.projectfinal.fragment.FavoriteFragment;
import com.example.projectfinal.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movies;
    private boolean isSearching = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;


    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
        this.sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        this.gson = new Gson();
        this.editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie, position, isSearching);

        boolean isFavorite = checkIfFavorite(movie.getId());
        movie.setFavorite(isFavorite);

        int favoriteIcon = movie.isFavorite() ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24;
        holder.favoriteButton.setImageResource(favoriteIcon);

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = movie.isFavorite();

                if (isFavorite) {
                    removeFavorite(movie);
                    showToast("Removed from favorites");
                } else {
                    addFavorite(movie);
                    showToast("Added to favorites");
                }

                movie.setFavorite(!isFavorite);

                int newFavoriteIcon = movie.isFavorite() ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24;
                holder.favoriteButton.setImageResource(newFavoriteIcon);

                if (context instanceof FavoriteFragment.FavoriteFragmentListener) {
                    ((FavoriteFragment.FavoriteFragmentListener) context).onFavoriteUpdated();
                }
            }
        });
    }

    private boolean checkIfFavorite(String songId) {
        return sharedPreferences.getBoolean(songId, false);
    }

    private void addFavorite(Movie movie) {
        editor.putBoolean(movie.getId(), true);
        editor.apply();
        updateFavoriteSongs(movie, true);
    }

    private void removeFavorite(Movie movie) {
        editor.putBoolean(movie.getId(), false);
        editor.apply();
        updateFavoriteSongs(movie, false);
    }

    private void updateFavoriteSongs(Movie movie, boolean add) {
        List<Movie> favoriteMovie = getFavoriteSongsFromSharedPreferences();
        if (favoriteMovie == null) {
            favoriteMovie = new ArrayList<>();
        }
        if (add) {
            favoriteMovie.add(movie);
        } else {
            favoriteMovie.removeIf(s -> s.getId().equals(movie.getId()));
        }
        String jsonFavorites = gson.toJson(favoriteMovie);
        editor.putString("favorites", jsonFavorites);
        editor.apply();
    }

    private List<Movie> getFavoriteSongsFromSharedPreferences() {
        String jsonFavorites = sharedPreferences.getString("favorites", null);
        if (jsonFavorites != null) {
            Type type = new TypeToken<List<Movie>>() {}.getType();
            return gson.fromJson(jsonFavorites, type);
        } else {
            return null; // Mengembalikan null jika tidak ada data favorit yang tersimpan
        }
    }

    public void setSearching(boolean searching) {
        isSearching = searching;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView movieTitle;
        private TextView movieRating;
        private ImageView favoriteButton;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieRating = itemView.findViewById(R.id.movieRating);
            favoriteButton = itemView.findViewById(R.id.favoriteButton); // Ambil referensi ke tombol favorite
        }

        public void bind(Movie movie, int position, boolean isSearching) {
            Glide.with(context).load(movie.getImage()).into(imageView);
            movieTitle.setText(movie.getTitle());
            movieRating.setText(String.valueOf(movie.getRating()));
        }
    }
}
