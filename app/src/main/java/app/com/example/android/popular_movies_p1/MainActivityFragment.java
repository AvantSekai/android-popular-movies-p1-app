package app.com.example.android.popular_movies_p1;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    FetchMovieDataTask movieDataTask;
    ArrayList<String> posterImages;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
        final String LOG_TAG = MainActivityFragment.class.getSimpleName();
        NetworkCheck network = new NetworkCheck(getContext());

        // When no network is present for the User show alertDialog and kill app
        if (network.checkActiveNetwork()== null) {
            network.buildDialog();
        }
        else {
            movieDataTask = new FetchMovieDataTask(getContext());
            movieDataTask.execute("popularity.desc");
            try {
                if (movieDataTask.get() != null) {
                    posterImages = new ArrayList<String>(movieDataTask.get());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
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
            movieDataTask = new FetchMovieDataTask(getContext());
            movieDataTask.execute("popularity.desc");
            return true;
        }
        else if (id == R.id.high_rated_sort){
            movieDataTask = new FetchMovieDataTask(getContext());
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
        // Set up ImageListAdapter for use with the main activity
        if (posterImages != null) {
            movieDataTask.imgListAdapter = new ImageListAdapter(getActivity(), posterImages);
        }
        GridView gridView = (GridView) rootView.findViewById(R.id.frag_main_gridView);
        gridView.setAdapter(movieDataTask.imgListAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson gson = new Gson();
                MovieGson movie = movieDataTask.movieGsonArrayList.get(position);
                Intent intent = new Intent(getActivity(), MovieDetails.class);
                intent.putExtra("MovieDetails", gson.toJson(movie));
                startActivity(intent);
            }
        });
        return rootView;
    }
}
