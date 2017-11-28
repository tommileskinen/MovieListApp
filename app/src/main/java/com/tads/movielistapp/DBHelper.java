package com.tads.movielistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


/**
 * Created by Tads on 25.2.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MovieList.db";
    public static final String MOVIE_TABLE_NAME = "movies";
    public static final String MOVIE_COLUMN_ID = "id";
    public static final String MOVIE_COLUMN_LISTID = "listId";
    public static final String MOVIE_COLUMN_TITLE = "title";
    public static final String MOVIE_COLUMN_RELEASED = "released";
    public static final String MOVIE_COLUMN_GENRE = "genre";
    public static final String MOVIE_COLUMN_TYPE = "type";
    public static final String MOVIE_COLUMN_IMDBRATING = "imdbrating";
    public static final String MOVIE_COLUMN_WATCHED = "watched";
    public static final String MOVIE_COLUMN_RATING = "rating";

    public static final String LISTS_TABLE_NAME = "lists";
    public static final String LISTS_COLUMN_ID = "id";
    public static final String LISTS_COLUMN_NAME = "name";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table lists" +
                        "(id integer primary key autoincrement, name text);"
        );
        db.execSQL(
                "create table movies" +
                        "(id integer primary key autoincrement, listId integer, title text, type text, released text, genre text, rating real default 0, watched integer default 0, foreign key (listId) references lists(id));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS movies");
        db.execSQL("DROP TABLE IF EXISTS lists");
        onCreate(db);
    }

    public int insertList(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        long id = db.insert("lists", null, contentValues);
        db.close();
        return (int)id;
    }

    public int updateMovieWatched(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("watched", 1);
        int response = db.update("movies", contentValues , "id = ?", new String[] {
                Integer.toString(id)
        });
        db.close();
        return response;
    }

    public int updateMovieRating(Integer id, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rating", rating);
        int response = db.update("movies", contentValues , "id = ?", new String[] {
                Integer.toString(id)
        });
        db.close();
        return response;
    }


    public int updateMovieNotWatched(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("watched", 0);
        int response = db.update("movies", contentValues , "id = ?", new String[] {
                Integer.toString(id)
        });
        db.close();
        return response;
    }

    public boolean insertProduct(int listId, String mTitle, String mReleased, String mType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("listId", listId);
        contentValues.put("title", mTitle);
        contentValues.put("released", mReleased);
        contentValues.put("type", mType);
        db.insert("movies", null, contentValues);
        db.close();
        return true;
    }

    public ArrayList<Movie> getProductsByListId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from movies where listId='" + id + "'", null);
        ArrayList<Movie> movies = new ArrayList<Movie>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Movie movie = new Movie();
            movie.id = cursor.getInt(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_ID));
            movie.listId = cursor.getInt(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_LISTID));
            movie.mTitle = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_TITLE));
            movie.mReleased = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_RELEASED));
            movie.mType = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_TYPE));
            movie.mWatched = cursor.getInt(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_WATCHED));
            movie.mRating = cursor.getFloat(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_RATING));
            movies.add(movie);
            cursor.moveToNext();
        }
        db.close();
        return movies;
    }

    public String getListNameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name from lists where id='" + id + "'", null);
        String movieName = "";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            movieName = cursor.getString(cursor.getColumnIndex(DBHelper.LISTS_COLUMN_NAME));
            cursor.moveToNext();
        }
        db.close();
        return movieName;
    }

    public boolean checkIfProductExists(String name, int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from movies where listId='" + id + "' and title='" + name + "'", null);
        boolean exists = false;
        String movieName = "";
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            movieName = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_TITLE));
            if (movieName.equals(name)) {
                exists = true;
            }
            cursor.moveToNext();
        }
        db.close();
        return exists;
    }


    public int getListsRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int rows = (int) DatabaseUtils.queryNumEntries(db, LISTS_TABLE_NAME);
        db.close();
        return rows;
    }

    public int getProductsRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int rows = (int) DatabaseUtils.queryNumEntries(db, MOVIE_TABLE_NAME);
        db.close();
        return rows;
    }

    public Cursor getListData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from lists where id='" + id + "'", null);
        db.close();
        return res;
    }

    public Cursor getProductData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from movies where id='" + id + "'", null);
        db.close();
        return res;
    }

    public Integer deleteList(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("lists", "id = ?",
                new String[] {
                        Integer.toString(id)
                });
    }

    public Integer deleteProduct(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("movies", "id = ?",
                new String[] {
                    Integer.toString(id)
                });
    }

    public ArrayList<MovieList> getAllLists() {
        ArrayList<MovieList> movieLists = new ArrayList<MovieList>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from lists", null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            MovieList movieList = new MovieList();
            movieList.id = cursor.getInt(cursor.getColumnIndex(LISTS_COLUMN_ID));
            movieList.name = cursor.getString(cursor.getColumnIndex(LISTS_COLUMN_NAME));
            Cursor cursor1 = db.rawQuery("select title from movies where listId='" + movieList.id + "' and watched='" + 1 + "'", null);
            Cursor cursor2 = db.rawQuery("select title from movies where listId='" + movieList.id + "'", null);
            movieList.listWatchedCount = cursor1.getCount();
            movieList.listSize = cursor2.getCount();
            movieLists.add(movieList);
            cursor.moveToNext();
        }
        db.close();
        return movieLists;
    }

    public ArrayList<MovieList> getAllListsAndMovies() {
        ArrayList<MovieList> movieLists = new ArrayList<MovieList>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from lists", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            MovieList movieList = new MovieList();
            movieList.id = cursor.getInt(cursor.getColumnIndex(LISTS_COLUMN_ID));
            movieList.name = cursor.getString(cursor.getColumnIndex(LISTS_COLUMN_NAME));
            Cursor cursor1 = db.rawQuery("select * from movies where listId=" + movieList.id, null);
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()) {
                Movie movie = new Movie();
                movie.id = cursor.getInt(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_ID));
                movie.listId = cursor.getInt(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_LISTID));
                movie.mTitle = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_TITLE));
                movie.mImdbRating = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_IMDBRATING));
                movie.mReleased = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_RELEASED));
                movie.mType = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_TYPE));
                movie.mGenre = cursor.getString(cursor.getColumnIndex(DBHelper.MOVIE_COLUMN_GENRE));
                movieList.movies.add(movie);
                cursor.moveToNext();
            }
            movieLists.add(movieList);
            cursor.moveToNext();
        }
        db.close();
        return movieLists;
    }

    public ArrayList<String> getAllMovies() {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from movies", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex(MOVIE_COLUMN_TITLE)));
            cursor.moveToNext();
        }
        db.close();
        return arrayList;
    }
}
