package ga.dhaan.popularmovies;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import ga.dhaan.popularmovies.helpers.EndlessScrolllistener;
import ga.dhaan.popularmovies.helpers.FetchMovieListTask;
import ga.dhaan.popularmovies.helpers.ImageAdapter;
import ga.dhaan.popularmovies.helpers.Movie;

import static ga.dhaan.popularmovies.helpers.FetchMovieListTask.POPULAR;
import static ga.dhaan.popularmovies.helpers.FetchMovieListTask.RATING;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private GridView grid;
    private String ordening="";
    private EndlessScrolllistener scrolllistener;
    private ImageAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        String ordening = PreferenceManager
            .getDefaultSharedPreferences(getContext())
            .getString(
                getString(R.string.pref_ordening_key),
                getString(R.string.pref_ordening_default)
            );
        if (ordeningChanged(ordening)) {
            final FetchMovieListTask fetcher = new FetchMovieListTask(getContext(), adapter);
            if (ordening.equals("popularity")) {
                fetcher.execute(POPULAR, 1);
            } else {
                fetcher.execute(RATING, 1);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        grid = (GridView) inflater.inflate(R.layout.fragment_main, container, false);

        List<Movie> urls = new ArrayList<>();
        adapter = new ImageAdapter(getContext(), urls);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent detailIntent = new Intent(getContext(), DetailActivity.class);
                detailIntent.putExtra("Movie", adapterView.getItemAtPosition(pos).toString());
                startActivity(detailIntent);
            }
        });

        final String ordening = PreferenceManager
            .getDefaultSharedPreferences(getContext())
            .getString(
                getString(R.string.pref_ordening_key),
                getString(R.string.pref_ordening_default)
            );

        scrolllistener = new EndlessScrolllistener() {
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                FetchMovieListTask fetcher = new FetchMovieListTask(getContext(), adapter);

                if (ordening.equals("popularity")) {
                    fetcher.execute(POPULAR, page);
                } else {
                    fetcher.execute(RATING, page);
                }
                return true;
            }
        };

        final FetchMovieListTask fetcher = new FetchMovieListTask(getContext(), adapter);
        if (ordening.equals("popularity")) {
            fetcher.execute(POPULAR, 1);
        } else {
            fetcher.execute(RATING, 1);
        }


        grid.setOnScrollListener(scrolllistener);
        return grid;
    }

    public boolean ordeningChanged(String ordening) {
        if (!this.ordening.equals(ordening)) {
            this.ordening = ordening;
            scrolllistener.reset();
            adapter.clear();
            return true;
        }

        return false;
    }

}
