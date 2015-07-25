package com.example.android.pmovie;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by zz on 7/23/2015.
 */
public class MovieAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<Movie> mMovies;
    private int mHeight;
    private int mWidth;

    public MovieAdapter(Context c) {
        mContext = c;
        mMovies = new ArrayList<>();
        mHeight = Math.round(mContext.getResources().getDimension(R.dimen.poster_height));
        mWidth = Math.round(mContext.getResources().getDimension(R.dimen.poster_width));
    }

    public void addAll(ArrayList<Movie> xs) {
        mMovies.addAll(xs);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        if (position < 0 || position >= mMovies.size()) {
            return null;
        }
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        Movie movie = getItem(position);
        if (movie == null) {
            return -1L;
        }
        return movie.id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        if (movie == null) {
            return null;
        }
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(mWidth, mHeight));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }

        String baseImageUrl = "http://image.tmdb.org/t/p/w185";

        Glide.with(mContext)
                .load(baseImageUrl + movie.poster_path)
                .into(imageView);

        return imageView;
    }
}
