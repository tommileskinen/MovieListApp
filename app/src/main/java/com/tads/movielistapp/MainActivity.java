package com.tads.movielistapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("All lists");

        DBHelper dbHelper = new DBHelper(this);
        ArrayList<MovieList> movieLists = dbHelper.getAllLists();
        //Log.d(TAG, "Lists: " + Objects.toString(movieLists));

        RecyclerView resultView = (RecyclerView)findViewById(R.id.listView);
        ListAdapter listAdapter = new ListAdapter(MainActivity.this, movieLists);
        resultView.setAdapter(listAdapter);
        resultView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        FloatingActionButton addListButton = (FloatingActionButton) findViewById(R.id.addListButton);
        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
                startActivity(intent);
            }
        });
    }

    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }*/
}
