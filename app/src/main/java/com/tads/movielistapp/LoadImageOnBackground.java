package com.tads.movielistapp;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Tads on 26.3.2017.
 */

public class LoadImageOnBackground extends AsyncTask<String, Void, Drawable>{

    private static final String TAG = LoadImageOnBackground.class.getSimpleName();

    String url;
    String name;
    View view;

    public LoadImageOnBackground(String url, String name, View view) {

        this.url = url;
        this.name = name;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Drawable doInBackground(String... params) {

        try {
            InputStream inputStream = new URL(params[0]).openConnection().getInputStream();
            Drawable drawable = Drawable.createFromStream(inputStream, name);
            inputStream.close();
            return drawable;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        super.onPostExecute(drawable);
        Log.d(TAG, "Setting background image: " + name);
        ImageView imageView = (ImageView) view;
        imageView.setImageDrawable(drawable);
        //view.setBackground(drawable);
    }
}
