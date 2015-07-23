package com.example.android.pmovie;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by zz on 7/22/2015.
 */
public class MovieFragment extends Fragment {
    private final String LOG_TAG = MovieFragment.class.getSimpleName();

    private Context mContext;
    private GridView mGridView;

    public MovieFragment() {

    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        mContext = this.getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridView);

        mGridView.setAdapter(new MovieAdapter(mContext));

        return rootView;
    }

    public class MovieAdapter extends BaseAdapter {
        private Context mContext;

        public MovieAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mExampleImages.length;
        }
        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mExampleImages[position]);
            return imageView;
        }
        private Integer[] mExampleImages = {
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
                R.drawable.web_launcher, R.drawable.web_launcher,
        };
    }

}
