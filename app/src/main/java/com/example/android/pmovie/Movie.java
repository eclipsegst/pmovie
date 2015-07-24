package com.example.android.pmovie;

/**
 * Created by zz on 7/23/2015.
 */
public class Movie {

    public final long id;
    public final String title;
    public final String overview;
    public final String poster_path;
    public final double vote_average;
    public final long vote_count;
    public final String release_date;

    public Movie(long id,
                 String title, String overview, String poster_path,
                 double vote_average, long vote_count, String release_date) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.release_date = release_date;
    }

}

