package com.example.projectfinal;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView movieTitle, movieRating, movieDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.moviePoster);
        movieTitle = findViewById(R.id.movieTitle);
        movieRating = findViewById(R.id.movieRating);
        movieDescription = findViewById(R.id.movieDescription);

        String imageUrl = getIntent().getStringExtra("movie_image");
        String title = getIntent().getStringExtra("movie_title");
        String rating = getIntent().getStringExtra("movie_rating");
        String description = getIntent().getStringExtra("movie_description");

        Glide.with(this).load(imageUrl).into(imageView);
        movieTitle.setText(title);
        movieRating.setText(rating);
        movieDescription.setText(description);
    }
}
