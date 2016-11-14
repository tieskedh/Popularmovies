package ga.dhaan.popularmovies.helpers;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

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
import java.util.List;

import ga.dhaan.popularmovies.R;

/**
 * Created by thijs on 13-11-2016.
 */

public class FetchMovieListTask extends AsyncTask<Integer, Void, List<Movie>> {
    private Context context;
    public static final int POPULAR = 1;
    public static final int RATING = 2;
    private ImageAdapter adapter;
    public FetchMovieListTask(Context context, ImageAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected List<Movie> doInBackground(Integer ... integers) {
        HttpURLConnection connection;

        String uri = buildUri(integers);
        try {
            connection = (HttpURLConnection) new URL(uri).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            return parseJSONString(getJSONString(connection));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies!=null) {
            adapter.addAll(movies);
        }
    }

    private List<Movie> parseJSONString(String jsonString) throws JSONException {
        JSONArray results = new JSONObject(jsonString).getJSONArray("results");
        List<Movie> movies = new ArrayList<>(results.length());
        for (int movie = 0; movie < results.length(); movie++) {
            movies.add(new Movie(results.getJSONObject(movie)));
        }
        return movies;
    }

    private String getJSONString(HttpURLConnection connection) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        try {
            InputStream inputStream = connection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

    private String buildUri(Integer[] integers) {
        Uri.Builder builder = new Uri.Builder()
            .scheme("http")
            .authority("api.themoviedb.org")
            .appendPath("3")
            .appendPath("movie");
        if(integers[0]==POPULAR) {
            builder = builder.appendPath("popular");
        } else {
            builder = builder.appendPath("top_rated");
        }
        return builder.appendQueryParameter(
            "api_key", context.getString(R.string.movie_api_key)
        ).appendQueryParameter("page", ""+integers[1])
            .toString();
    }
}
