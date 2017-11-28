package com.tads.movielistapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tads on 26.2.2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater layoutInflater;
    List<Movie> movies = Collections.emptyList();
    DBHelper dbHelper;
    private static final String TAG = MovieListActivity.class.getSimpleName();

    public MovieListAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.content_movie_list, parent, false);
        MovieHolder movieHolder = new MovieHolder(view);

        return movieHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final MovieHolder movieHolder = (MovieHolder) holder;
        final Movie current = movies.get(position);
        final int itemPosition = position;

        movieHolder.titleText.setText(current.mTitle);
        movieHolder.typeText.setText(current.mType);
        //movieHolder.genreText.setText(current.mGenre);
        movieHolder.releasedText.setText(current.mReleased);
        movieHolder.movieId = current.id;
        movieHolder.listId = current.listId;
        movieHolder.ratingBar.setRating(current.mRating);
        if (current.mWatched == 1) {
            movieHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
        }

        movieHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current.mWatched != 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Rate and set watched.");
                    builder.setMessage("Select rating for the movie.");
                    final RatingBar ratingBar = new RatingBar(context, null, R.attr.ratingBarStyleSmall);
                    ratingBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ratingBar.setNumStars(10);
                    ratingBar.setIsIndicator(false);
                    ratingBar.setStepSize(0.5f);

                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    linearLayout.addView(ratingBar);
                    builder.setView(linearLayout);
                    builder.setPositiveButton("Rate",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DBHelper dbHelper = new DBHelper(context);
                                    movieHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
                                    dbHelper.updateMovieWatched(current.id);
                                    dbHelper.updateMovieRating(current.id, ratingBar.getRating());
                                    current.mWatched = 1;
                                    current.mRating = ratingBar.getRating();
                                    movieHolder.ratingBar.setRating(current.mRating);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Movie rated", Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "SETTING MOVIE WATCHED");
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(true);
                    builder.setTitle("Reset movie");
                    builder.setMessage("Are you sure you want to reset the movie?");
                    builder.setPositiveButton("Reset",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    movieHolder.itemView.setBackgroundColor(Color.WHITE);
                                    DBHelper dbHelper = new DBHelper(context);
                                    dbHelper.updateMovieNotWatched(current.id);
                                    dbHelper.updateMovieRating(current.id, 0f);
                                    current.mRating = 0f;
                                    movieHolder.ratingBar.setRating(current.mRating);
                                    notifyDataSetChanged();
                                    current.mWatched = 0;
                                    Log.d(TAG, "SETTING MOVIE NOT WATCHED");
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        movieHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("Remove movie");
                builder.setMessage("Are you sure you want to remove the movie?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper dbHelper = new DBHelper(context);
                                dbHelper.deleteProduct(current.id);
                                movies.remove(itemPosition);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Movie removed.", Toast.LENGTH_LONG).show();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
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
        TextView ratingText;
        ImageButton deleteButton;
        RatingBar ratingBar;
        int movieId;
        int listId;

        public MovieHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.movieTitleText);
            releasedText = (TextView) itemView.findViewById(R.id.movieReleaseText);
            genreText = (TextView) itemView.findViewById(R.id.movieGenreText);
            typeText = (TextView) itemView.findViewById(R.id.movieTypeText);
            ratingBar = (RatingBar) itemView.findViewById(R.id.movieRatingBar);

        }
    }
}
