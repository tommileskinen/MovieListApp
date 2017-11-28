package com.tads.movielistapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tads on 22.2.2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater layoutInflater;
    List<Movie> movies = Collections.emptyList();
    DBHelper dbHelper;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.content_movie, parent, false);
        MovieHolder movieHolder = new MovieHolder(view);
        return movieHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MovieHolder movieHolder = (MovieHolder) holder;
        final Movie current = movies.get(position);

        movieHolder.titleText.setText(current.mTitle);
        //movieHolder.ratingBar.setRating(Float.parseFloat(current.mImdbRating));
        movieHolder.typeText.setText(current.mType);
        //movieHolder.genreText.setText(current.mGenre);
        movieHolder.releasedText.setText(current.mReleased);
        movieHolder.listId = current.listId;
        //new LoadImageOnBackground(current.mPosterUrl, current.mTitle, movieHolder.poster).execute(current.mPosterUrl);
        //movieHolder.itemView.setBackground(LoadImageFromUrl(current.mPosterUrl));

        dbHelper = new DBHelper(context);
        movieHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean movieExists = dbHelper.checkIfProductExists(current.mTitle, current.listId);
                if (!movieExists) {
                    Log.d(TAG, "Saving movie/show to list with id: " + current.listId);
                    dbHelper.insertProduct(current.listId, current.mTitle, current.mReleased, current.mType);
                    int listId = current.listId;
                    Intent intent = new Intent(context, MovieListActivity.class);
                    intent.putExtra("listId", listId);
                    context.startActivity(intent);
                    Toast.makeText(context, "Item added to list.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(context, "Movie is already added to list.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        TextView titleText;
        TextView releasedText;
        TextView genreText;
        TextView typeText;
        ImageButton addButton;
        int listId;
        RatingBar ratingBar;
        ImageView poster;

        public MovieHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.titleText);
            releasedText = (TextView) itemView.findViewById(R.id.releasedText);
            genreText = (TextView) itemView.findViewById(R.id.genreText);
            typeText = (TextView) itemView.findViewById(R.id.typeText);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBarSearchMovie);
            addButton = (ImageButton) itemView.findViewById(R.id.addButton);
            //poster = (ImageView) itemView.findViewById(R.id.moviePoster);
        }
    }

    public static Drawable LoadImageFromUrl(String url) {
        try {
            InputStream inputStream = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            return drawable;
        }
        catch (Exception e) {
            return null;
        }
    }
}
