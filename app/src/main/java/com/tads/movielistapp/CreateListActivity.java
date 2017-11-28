package com.tads.movielistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Tads on 25.2.2017.
 */

public class CreateListActivity extends AppCompatActivity {

    private static final String TAG = CreateListActivity.class.getSimpleName();

    DBHelper dbHelper;
    Button cancelButton;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_create_list);
        getSupportActionBar().setTitle("Create a new list");
        dbHelper = new DBHelper(this);

        Button createListButton = (Button) findViewById(R.id.createListButton);
        final EditText listNameText = (EditText) findViewById(R.id.editListNameText);

        createListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String listName = listNameText.getText().toString().trim();
                if (!listName.isEmpty()) {
                    int listId = dbHelper.insertList(listName);
                    Log.d(TAG, "Created a new list with id: " + listId);
                    Intent intent = new Intent(CreateListActivity.this, MovieListActivity.class);
                    intent.putExtra("listId", listId);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter a list name.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(CreateListActivity.this, MainActivity.class);;
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
