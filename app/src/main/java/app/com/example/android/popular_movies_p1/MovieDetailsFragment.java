package app.com.example.android.popular_movies_p1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {
    private final String LOG_TAG = MovieDetails.class.getSimpleName();

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Intent intent = new Intent(getActivity().getIntent());
        Gson gson = new Gson();

        // TextViews
        TextView header = (TextView) rootView.findViewById(R.id.movie_title_textView);
        //TextView releaseDate = (TextView)rootView.findViewById(R.id.release_date_textView);
        //TextView voteAverage = (TextView)rootView.findViewById(R.id.vote_average_textView);

        /* Retrieve Data from Intent and Create Layout for the
        MovieDetails Fragments Display.
         */
        if (intent != null ) {
            Log.v(LOG_TAG, "Inside true for !=null");
            Bundle bundleData = intent.getExtras();
            Log.v(LOG_TAG, "Bundle contains " + bundleData.getString("MovieDetails"));
            String movieString = bundleData.getString("MovieDetails");
            MovieGson movieDetails = gson.fromJson(movieString, MovieGson.class);
            Log.v(LOG_TAG, "MovieGson is " + movieDetails.getTitle());

            // Create Textview for Movie Header Title
            header.setText(movieDetails.getTitle());
           // releaseDate.setText(movieDetails.getRelease_date());
          //  voteAverage.setText(movieDetails.getVote_average().toString());
        }

        else {
            Log.v(LOG_TAG, "Intent is null");
        }

        return rootView;
    }
}
