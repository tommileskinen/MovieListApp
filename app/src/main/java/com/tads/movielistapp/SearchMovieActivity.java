package com.tads.movielistapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchMovieActivity extends AppCompatActivity {

    private static final String TAG = SearchMovieActivity.class.getSimpleName();

    EditText nameText;
    Button findButton;
    RecyclerView resultView;
    MovieAdapter movieAdapter;
    String url = "";
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        getSupportActionBar().setTitle("Add a new movie or series");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameText = (EditText) findViewById(R.id.nameText);
        findButton = (Button) findViewById(R.id.findButton);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findButton.setEnabled(false);
                title = nameText.getText().toString().trim();
                if (!title.isEmpty()) {
                    title = title.replace(' ', '+');
                    url = "";
                    new GetGameAsyncTask().execute(url);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter title to search.", Toast.LENGTH_LONG).show();
                }
                findButton.setEnabled(true);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.addButton) {
            Log.d(TAG, "ITEM ID: " + item.getItemId());
            Log.d(TAG, "ADD BUTTON ID: " + R.id.addButton);
            int listId = getIntent().getExtras().getInt("listId");
            Intent intent = new Intent(SearchMovieActivity.this, MovieListActivity.class);;
            intent.putExtra("listId", listId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetGameAsyncTask extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog = new ProgressDialog(SearchMovieActivity.this);

        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    GetGameAsyncTask.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                    Log.d(TAG, "JSON: " + line);
                }

                reader.close();

                return stringBuffer.toString();

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Log.d(TAG, "RESULT: " + result);
            if (result != null) {
                try {
                    ArrayList<Movie> movies = new ArrayList<>();
                    //JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = new JSONObject(result);
                    String response = jsonObject.getString("Response");
                    if (!result.isEmpty() && response.equals("True")) {
                        for (int i = 0; i < jsonObject.getJSONArray("Search").length(); i++) {
                            Movie movie = new Movie();
                            movie.listId = getIntent().getExtras().getInt("listId");
                            movie.mTitle = jsonObject.getJSONArray("Search").getJSONObject(i).getString("Title");
                            //movie.mImdbRating = jsonObject.getJSONArray("Search").getJSONObject(i).;
                            movie.mReleased = jsonObject.getJSONArray("Search").getJSONObject(i).getString("Year");
                            movie.mType = jsonObject.getJSONArray("Search").getJSONObject(i).getString("Type");
                            movie.mPosterUrl = jsonObject.getJSONArray("Search").getJSONObject(i).getString("Poster");
                            //movie.mGenre = jsonArray.getJSONObject(i).getString("Genre");
                            movies.add(movie);
                        }


                        resultView = (RecyclerView)findViewById(R.id.resultView);
                        movieAdapter = new MovieAdapter(SearchMovieActivity.this, movies);
                        resultView.setAdapter(movieAdapter);
                        resultView.setLayoutManager(new LinearLayoutManager(SearchMovieActivity.this));
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No movies found.", Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
