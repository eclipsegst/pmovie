package com.example.android.pmovie;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Context mContext;
    private Movie mMovie;

    private TextView mTitle;

    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private TextView mVoteCount;
    private TextView mOverview;

    private ImageView imageView;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        mContext = this.getActivity().getApplicationContext();


        Intent intent = getActivity().getIntent();
        mMovie = (Movie)intent.getSerializableExtra("MOVIE");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mTitle = (TextView) rootView.findViewById(R.id.title_textview);
        mTitle.setText(mMovie.title);

        mReleaseDate = (TextView) rootView.findViewById(R.id.release_date_textview);
        mReleaseDate.setText(mMovie.release_date);

        mVoteAverage = (TextView) rootView.findViewById(R.id.vote_average_textview);
        mVoteAverage.setText(String.valueOf(mMovie.vote_average) + "/10");

        mVoteCount = (TextView) rootView.findViewById(R.id.vote_count_textview);
        mVoteCount.setText(String.valueOf("Vote:" + mMovie.vote_count));

        mOverview = (TextView) rootView.findViewById(R.id.overivew_textview);
        mOverview.setText(mMovie.overview);

        imageView = (ImageView) rootView.findViewById(R.id.poster_imageview);

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setPadding(4, 4, 4, 4);
        String baseImageUrl = "http://image.tmdb.org/t/p/w185";
        Glide.with(mContext)
                .load(baseImageUrl + mMovie.poster_path)
                .into(imageView);

        return rootView;
    }
}
