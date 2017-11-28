package com.tads.movielistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tads on 25.2.2017.
 */

public class MovieListActivity extends AppCompatActivity {

    private static final String TAG = MovieListActivity.class.getSimpleName();

    DBHelper dbHelper;
    TextView movieListNameText;
    RecyclerView recyclerView;
    MovieListAdapter movieListAdapter;
    FloatingActionButton addListButton;
    Button deleteButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CreateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CreateList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        CreateList();
    }

    private void CreateList() {
        setContentView(R.layout.activity_movie_list);

        dbHelper = new DBHelper(this);
        final Movie movie = new Movie();
        ArrayList<Movie> movies = new ArrayList<Movie>();
        final int id = getIntent().getExtras().getInt("listId");
        Log.d(TAG, "Loading list with id: " + id);

        String movieName = dbHelper.getListNameById(id);
        getSupportActionBar().setTitle(movieName);

        movies = dbHelper.getProductsByListId(id);

        recyclerView = (RecyclerView)findViewById(R.id.movieListContentView);
        movieListAdapter = new MovieListAdapter(MovieListActivity.this, movies);
        recyclerView.setAdapter(movieListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MovieListActivity.this));

        addListButton = (FloatingActionButton) findViewById(R.id.addMovieButton);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieListActivity.this, SearchMovieActivity.class);
                Log.d(TAG, "Adding list id: " + id);
                intent.putExtra("listId", id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(MovieListActivity.this, MainActivity.class);;
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

}
