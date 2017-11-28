package com.tads.movielistapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tads on 25.2.2017.
 */

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ListAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater layoutInflater;
    List<MovieList> movieLists = Collections.emptyList();

    public ListAdapter(Context context, List<MovieList> movieLists) {
        this.context = context;
        this.movieLists = movieLists;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.content_main, parent, false);
        ListAdapter.MovieListHolder movieListHolder = new ListAdapter.MovieListHolder(view);

        return movieListHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ListAdapter.MovieListHolder movieListHolder = (ListAdapter.MovieListHolder) holder;
        final MovieList current = movieLists.get(position);
        final int itemPosition = position;
        int listProgress = 0;

        movieListHolder.listId = current.id;
        movieListHolder.listName.setText(current.name);
        movieListHolder.progressBar.setMax(current.listSize);
        movieListHolder.progressBar.setProgress(current.listWatchedCount);

        movieListHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle("Remove list");
                builder.setMessage("Are you sure you want to remove the list?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper dbHelper = new DBHelper(context);
                                dbHelper.deleteList(current.id);
                                movieLists.remove(itemPosition);
                                notifyDataSetChanged();
                                Toast.makeText(context, "List removed.", Toast.LENGTH_LONG).show();
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
        return movieLists.size();
    }

    class MovieListHolder extends RecyclerView.ViewHolder {

        TextView listName;
        int listId;
        ProgressBar progressBar;

        public MovieListHolder(View itemView) {
            super(itemView);
            listName = (TextView) itemView.findViewById(R.id.movieListNameText);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBarList);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Adding list id: " + listId);
                    Intent intent = new Intent(context, MovieListActivity.class);
                    intent.putExtra("listId", listId);
                    context.startActivity(intent);
                }
            });

        }
    }
}
