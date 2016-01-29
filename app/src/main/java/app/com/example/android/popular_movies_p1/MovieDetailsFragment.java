package app.com.example.android.popular_movies_p1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
        TextView releaseDate = (TextView)rootView.findViewById(R.id.release_date_textView);
        TextView voteAverage = (TextView)rootView.findViewById(R.id.vote_average_textView);
        TextView overview = (TextView) rootView.findViewById(R.id.overview_textView);

        // Image View for Poster
        ImageView poster = (ImageView) rootView.findViewById(R.id.poster_imageView);

        /* Retrieve Data from Intent and Create Layout for the
        MovieDetails Fragments Display.
         */
        if (intent != null ) {
            Bundle bundleData = intent.getExtras();
            String movieString = bundleData.getString("MovieDetails");
            MovieGson movieDetails = gson.fromJson(movieString, MovieGson.class);
            Picasso.with(getContext()).load(movieDetails.getPoster_path()).fit().into(poster);

            // Create Textview for Movie Header Title
            header.setText(movieDetails.getTitle());
            releaseDate.setText(movieDetails.getRelease_date());
            voteAverage.setText(movieDetails.getVote_average());
            overview.setText(movieDetails.getOverview());
        }
        return rootView;
    }
}
