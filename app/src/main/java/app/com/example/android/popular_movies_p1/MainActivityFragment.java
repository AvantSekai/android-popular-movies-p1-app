package app.com.example.android.popular_movies_p1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Arrays;

import com.google.gson.Gson;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    ImageListAdapter imgListAdapter;
    ArrayList<MovieGson> movieGsonArrayList;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
        final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.most_popular_sort, menu);
        inflater.inflate(R.menu.highest_rated_sort, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.popular_sort) {
            FetchMovieDataTask movieDataTask = new FetchMovieDataTask();
            movieDataTask.execute("popularity.desc");
            return true;
        }
        else if (id == R.id.high_rated_sort){
            FetchMovieDataTask movieDataTask = new FetchMovieDataTask();
            movieDataTask.execute("vote_average.desc");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final String LOG_TAG = MainActivityFragment.class.getSimpleName();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Context currentContext = getContext();
        // Create a list of of images to use using ArrayList
         String[] stockimages = {
                "http://image.tmdb.org/t/p/w185/fqe8JxDNO8B8QfOGTdjh6sPCdSC.jpg",
                "http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg",
                "http://image.tmdb.org/t/p/w185/5aGhaIHYuQbqlHWvWYqMCnj40y2.jpg",
                "http://image.tmdb.org/t/p/w185/oXUWEc5i3wYyFnL1Ycu8ppxxPvs.jpg"
        };
        ArrayList<String> stocklistimages = new ArrayList<String>(Arrays.asList(stockimages));
        NetworkCheck network = new NetworkCheck(currentContext);

        // When no network is present for the User show alertDialog and kill app
        if (network.checkActiveNetwork()== null) {
            network.buildDialog();
        }
        else {
            Log.v(LOG_TAG, "Proceed with onCreateView options ");
        }

        // Set up ImageListAdapter for use with the main activity
        imgListAdapter = new ImageListAdapter(getActivity(), stocklistimages);
        GridView gridView = (GridView) rootView.findViewById(R.id.frag_main_gridView);
        gridView.setAdapter(imgListAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson gson = new Gson();
                // Check if Movie Array List is at startup mode
                if (movieGsonArrayList == null) { // When no objects instruct user with a toast
                    // Call FetchMovie onClick to retrieve objects
                    Context currentContext = getContext();
                    int duration = Toast.LENGTH_SHORT;
                    String message = "Select a \"SORT BY: Option\" from the Settings menu.";
                    Toast toast = Toast.makeText(currentContext, message, duration);
                    toast.show();
                }
                else { // Handle Intent for Details
                    MovieGson movie = movieGsonArrayList.get(position);
                    Intent intent = new Intent(getActivity(), MovieDetails.class);
                    intent.putExtra("MovieDetails", gson.toJson(movie));
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    public class FetchMovieDataTask extends AsyncTask<String, Void, Void> {
        private final String LOG_TAG = FetchMovieDataTask.class.getSimpleName();
        private final String baseImgPath = "http://image.tmdb.org/t/p/";

        // List of JSON objects
        final String TMDB_RESULTS = "results";
        final String RELATIVE_IMG_PATH = "poster_path";

        private JSONArray createFromJson(String movieJsonStr)
                throws JSONException {
            Gson gson = new Gson();

            // JSON Objects, Arrays and ArrayList
            JSONObject moviesJson = new JSONObject(movieJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);
            movieGsonArrayList = new ArrayList<MovieGson>(moviesArray.length());

            /* Clear out any old instances of the Movie Array list and instantiate
               with length of JsonArray */
            if (!movieGsonArrayList.isEmpty()) {movieGsonArrayList.clear();}


            for (int i=0; i < moviesArray.length(); i++) {
                JSONObject movieJson = moviesArray.getJSONObject(i);
                // Lets take the short path info and create a full path
                String part_temp_path = movieJson.getString(RELATIVE_IMG_PATH);
                // Build real path for poster image and replace "poster_path" data
                String pathResult = buildPosterPath(part_temp_path);
                movieJson.remove(RELATIVE_IMG_PATH);
                movieJson.put(RELATIVE_IMG_PATH, pathResult);

                // JSON to Gson conversion for MovieGson Class and add to Movie Array List
                MovieGson movieGToGSon = gson.fromJson(movieJson.toString(), MovieGson.class);
                movieGsonArrayList.add(movieGToGSon);
            }
            return moviesArray;
        }

        private String buildPosterPath(String relativePath) {
            final String IMG_WIDTH = "w185";
            Uri buildImgPath = Uri.parse(baseImgPath).buildUpon()
                    .appendPath(IMG_WIDTH)
                    . build();
            String full_path = buildImgPath.toString() + relativePath;
            return full_path;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            imgListAdapter.clear();
            // Retrieve and store the json data containing the Movie Details needed.
            for (int i =0; i<movieGsonArrayList.size(); i++) {
                try {
                    // Poster Path Data
                    MovieGson movieGson = movieGsonArrayList.get(i);
                    imgListAdapter.add(movieGson.getPoster_path());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            imgListAdapter.notifyDataSetChanged();
        }

        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String rawJsonStr = null;
            String sortBy  = params[0];

            //Log.v(LOG_TAG, "Sort By Method is " + sortBy);

            try {
                final String MOVIE_API_BASEURL = "https://api.themoviedb.org/3";
                final String DISCOVER = "discover";
                final String SORT = "sort_by";
                final String API_KEY = "api_key";

                // Build URI to create a URL Web address
                Uri uriBuild = Uri.parse(MOVIE_API_BASEURL).buildUpon()
                        .appendPath(DISCOVER)
                        .appendPath("movie")
                        .appendQueryParameter(SORT, sortBy)
                        .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();
                URL url = new URL(uriBuild.toString());
                //Log.v(LOG_TAG, "URL " + url);

                // Make a connection to the movie DB
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                rawJsonStr = buffer.toString();


                try {
                     createFromJson(rawJsonStr);
                    return null;
                }
                catch (JSONException e) {
                    Log.e(LOG_TAG, "Error " + e.getMessage(), e);
                    e.printStackTrace();
                }

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the data, there's no point in attemping
                // to parse it.
                return null;
            }
            finally {
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

    }
}
