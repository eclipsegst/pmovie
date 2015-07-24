package com.example.android.pmovie;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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

        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();
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

    public class FetchMovieTask extends AsyncTask<String, Void, List> {

        @Override
        protected List doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;
            try {
                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=";
                Uri buildUri = Uri.parse(BASE_URL).buildUpon().build();
                URL url = new URL(buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a lot easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream is empty. No point in parsing.
                    return null;
                }

                movieJsonStr = buffer.toString();
                Log.d(LOG_TAG, "Movie Json = " + movieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            return null;
        }

        protected void onPostExecute(List result) {

        }
    }
}
