package ga.dhaan.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import ga.dhaan.popularmovies.helpers.Helpers;
import ga.dhaan.popularmovies.helpers.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private ShareActionProvider shareActionProvider;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        intent.getExtras().size();

        try {
            Movie movie = new Movie(new JSONObject(intent.getStringExtra("Movie")));
            fillView(view, movie);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu.getItem(0));
        String shareText = "Check out this movie ";
        try {
            String movie = new Movie(
                new JSONObject(
                    getActivity()
                        .getIntent()
                        .getStringExtra("Movie")
                )
            ).getString("original_title", null);
            if (movie == null) {
                shareText = "Check out the app";
            } else {
                shareText+=movie;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            shareText = "Check out the app";
        }
        Helpers.setShareText(shareActionProvider, shareText+" #popular movies");
    }

    public void fillView(View view, Movie movie) {
        Picasso.with(getContext())
            .load("http://image.tmdb.org/t/p/w500/"+movie.getString("poster_path", null))
            .into((ImageView) view.findViewById(R.id.imgPoster));

        RatingBar bar = (RatingBar) view.findViewById(R.id.rbMovieRating);
        bar.setEnabled(false);
        bar.setNumStars(5);
        bar.setStepSize(0.01f);
        bar.setRating(((float) movie.getRating())/2);
        ((TextView) view.findViewById(R.id.tvRating)).setText(String.valueOf(movie.getRating()));
        ((TextView) view.findViewById(R.id.tvOverview))
            .setText(movie.getString("overview", "No overview available"));
        ((TextView) view.findViewById(R.id.tvReleaseDate)).setText(movie.getString("release_date", "No Releasedate available"));
        ((TextView) view.findViewById(R.id.tvTitle)).setText(movie.getString("original_title", "Title"));
    }
}
