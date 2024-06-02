package com.example.projectfinal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectfinal.DetailActivity;
import com.example.projectfinal.R;
import com.example.projectfinal.models.Movie;

import java.util.List;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private Context context;

    public FavoriteAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.ratingTextView.setText(String.valueOf(movie.getRating()));
        Glide.with(holder.itemView.getContext())
                .load(movie.getImage())
                .into(holder.movieImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Menampilkan detail film saat item favorit diklik
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("movie_title", movie.getTitle());
                intent.putExtra("movie_rating", String.valueOf(movie.getRating()));
                intent.putExtra("movie_description", movie.getDescription());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImageView;
        TextView titleTextView, ratingTextView;

        MovieViewHolder(View itemView) {
            super(itemView);
            movieImageView = itemView.findViewById(R.id.moviePoster);
            titleTextView = itemView.findViewById(R.id.movieTitle);
            ratingTextView = itemView.findViewById(R.id.movieRating);
        }
    }
}
