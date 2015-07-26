package com.example.android.pmovie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private String API_KEY;
    private YouTubePlayer YPlayer;
    private String YoutubeDeveloperKey;
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private final static String OWN_ADULT = "adult";
    private final static String OWN_BUDGET = "budget";
    private final static String OWN_HOMEPAGE = "homepage";
    private final static String OWN_REVENUE = "revenue";
    private final static String OWN_RUNTIME = "runtime";

    private final static String OWN_TRAILERS = "trailers";
    private final static String OWN_YOUTUTBE = "youtube";
    private final static String OWN_TRAILERS_NAME = "name";
    private final static String OWN_TRAILERS_SOURCE = "source";

    private Boolean adult;
    private long buget;
    private String homepage;
    private long revenue;
    private int runtime;

    private String trailerName;
    private String trailerSource;


    private Context mContext;
    private Movie mMovie;

    private TextView mTitle;

    private TextView mReleaseDate;
    private TextView mRuntime;
    private TextView mVoteAverage;
    private TextView mVoteCount;
    private TextView mOverview;

    private ImageView imageView;

    private YouTubePlayerSupportFragment youTubePlayerFragment;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        mContext = this.getActivity().getApplicationContext();


        Intent intent = getActivity().getIntent();
        mMovie = (Movie)intent.getSerializableExtra("MOVIE");

        API_KEY = ((Constants) this.getActivity().getApplication()).API_KEY;
        YoutubeDeveloperKey = ((Constants) this.getActivity().getApplication()).YOUTUBE_KEY;

        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        fetchMovieTask.execute();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mTitle = (TextView) rootView.findViewById(R.id.title_textview);
        mTitle.setText(mMovie.title);

        mReleaseDate = (TextView) rootView.findViewById(R.id.release_date_textview);
        mReleaseDate.setText(mMovie.release_date);

        mRuntime = (TextView) rootView.findViewById(R.id.runtime_textview);


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

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
        youTubePlayerFragment = (YouTubePlayerSupportFragment)getChildFragmentManager().findFragmentById(R.id.youtube_fragment);

//        youTubePlayerFragment.initialize(YoutubeDeveloperKey, new YouTubePlayer.OnInitializedListener() {
//
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
//                if (!b) {
//                    YPlayer = youTubePlayer;
//                    YPlayer.setFullscreen(false);
//                    YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
//                    // YPlayer.loadVideo("RFinNxS5KN4"); // Auto play video
//                    YPlayer.cueVideo("RFinNxS5KN4"); // Use cueVideo if you don't want
//                    // to play it automatically.
//                }
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
//                // TODO Auto-generated method stub
//
//            }
//        });

        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Map<String, String>> {

        private String LOG_TAG = "FetchMovieTask";

        private Map<String, String> getMovieDataFromJson(String movieJsonStr) throws JSONException {
            Map<String, String> map = new HashMap<>();

            JSONObject movieJson = new JSONObject(movieJsonStr);

            adult = movieJson.getBoolean(OWN_ADULT);
            buget = movieJson.getLong(OWN_BUDGET);
            homepage = movieJson.getString(OWN_HOMEPAGE);
            revenue = movieJson.getLong(OWN_REVENUE);
            runtime = movieJson.getInt(OWN_RUNTIME);

            JSONObject trailerObj = movieJson.getJSONObject(OWN_TRAILERS);
            JSONObject youtubeObj = trailerObj.getJSONArray(OWN_YOUTUTBE).getJSONObject(0);

            trailerName = youtubeObj.getString(OWN_TRAILERS_NAME);
            trailerSource = youtubeObj.getString(OWN_TRAILERS_SOURCE);

            map.put(OWN_ADULT, adult.toString());
            map.put(OWN_BUDGET, String.valueOf(buget));
            map.put(OWN_HOMEPAGE, String.valueOf(homepage));
            map.put(OWN_REVENUE, String.valueOf(revenue));
            map.put(OWN_RUNTIME, String.valueOf(runtime));
            map.put(OWN_TRAILERS_NAME, String.valueOf(trailerName));
            map.put(OWN_TRAILERS_SOURCE, String.valueOf(trailerSource));

            return map;
        }

        @Override
        protected Map<String, String> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            String appendToResponse = "releases,trailers";
            try {

                final String BASE_URL = "https://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM = "api_key";
                final String APPEND_TO_RESPONSE = "append_to_response";

                Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(String.valueOf(mMovie.id))
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .appendQueryParameter(APPEND_TO_RESPONSE, appendToResponse).build();

                URL url = new URL(buildUri.toString());

                Log.d(LOG_TAG, "url = " + url);

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

        protected void onPostExecute(final Map<String, String> result) {
            mRuntime.setText(result.get(OWN_RUNTIME) + " min");

            youTubePlayerFragment.initialize(YoutubeDeveloperKey, new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                    if (!b) {
                        YPlayer = youTubePlayer;
                        YPlayer.setFullscreen(false);
                        YPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        YPlayer.cueVideo(result.get(OWN_TRAILERS_SOURCE));
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                    // TODO Auto-generated method stub

                }
            });
        }
    }
}
