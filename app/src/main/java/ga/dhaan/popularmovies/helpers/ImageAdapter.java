package ga.dhaan.popularmovies.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


import java.util.List;

import ga.dhaan.popularmovies.R;

public class ImageAdapter extends ArrayAdapter<Movie> {
    private final LayoutInflater inflater;
    private Context context;

    private final List<Movie> urls;

    public ImageAdapter(Context context, List<Movie> urls) {
        super(context, R.layout.grid_image_tile, urls);
        this.context = context;
        this.urls = urls;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Movie getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public void add(Movie url) {
        urls.add(url);
        super.add(url);
    }

    @Override
    public void remove(Movie movie) {
        urls.remove(movie);
            Picasso.with(context)
                .invalidate(movie.getString("image", null));
        super.remove(movie);
    }

    @NonNull
    @Override
    public ImageView getView(int pos, View convertView, @NonNull ViewGroup parent) {
        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = (ImageView) inflater.inflate(R.layout.grid_image_tile, parent, false);
        }

        Movie movie = getItem(pos);
        Picasso.with(context)
            .load(movie.getString("image", null))
            .into(view);
        return view;
    }

    @Override
    public void clear() {
        urls.clear();
        super.clear();
    }
}
