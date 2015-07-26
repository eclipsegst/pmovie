package com.example.android.pmovie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by zz on 7/22/2015.
 */
public class MovieFragment extends Fragment {
    private final String LOG_TAG = MovieFragment.class.getSimpleName();

    private String API_KEY;
    private Context mContext;
    private GridView mGridView;
    private MovieAdapter mMovieAdapter;

    public MovieFragment() {

    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        mContext = this.getActivity().getApplicationContext();

        API_KEY = ((Constants) this.getActivity().getApplication()).API_KEY;

        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridView);

        mMovieAdapter = new MovieAdapter(mContext);
        mGridView.setAdapter(mMovieAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v,
                                    int position,
                                    long id) {
                MovieAdapter movieAdapter = (MovieAdapter) parent.getAdapter();
                Movie movie = movieAdapter.getItem(position);
                if (movie == null) {
                    return ;
                }
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra("MOVIE", movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException {
            ArrayList<Movie> movieArrayList = new ArrayList<>();

            final String OWN_RESULTS = "results";
            final String OWN_ID = "id";
            final String OWN_TITLE = "title";
            final String OWN_OVERVIEW = "overview";
            final String OWN_POSTER_PATH = "poster_path";
            final String OWN_VOTE_AVERAGE = "vote_average";
            final String OWN_VOTE_COUNT = "vote_count";
            final String OWN_RELEASE_DATE = "release_date";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movies = movieJson.getJSONArray(OWN_RESULTS);

            JSONObject movie;
            Movie newMovie;

            for (int i = 0; i < movies.length(); i++) {
                movie = movies.getJSONObject(i);
                newMovie = new Movie(
                        movie.getLong(OWN_ID),
                        movie.getString(OWN_TITLE),
                        movie.getString(OWN_OVERVIEW),
                        movie.getString(OWN_POSTER_PATH),
                        movie.getDouble(OWN_VOTE_AVERAGE),
                        movie.getLong(OWN_VOTE_COUNT),
                        movie.getString(OWN_RELEASE_DATE)
                );
                movieArrayList.add(newMovie);
            }

            Log.d(LOG_TAG, "movieArrayList" + movieArrayList.size());
            return movieArrayList;
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;
            String sortBy = "popularity.desc";
            try {

                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String API_KEY_PARAM = "api_key";
                final String SORT_BY_PARAM = "sort_by";

                Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .appendQueryParameter(SORT_BY_PARAM,sortBy)
                        .build();

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

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(ArrayList<Movie> result) {
            Log.d(LOG_TAG, "onPostExecute result.size() = " + result.size());

            mMovieAdapter.addAll(result);

        }
    }
}
