package ga.dhaan.popularmovies.helpers;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thijs on 13-11-2016.
 */

public class Movie{

    private int id;
    private String baseImgUrl = "http://image.tmdb.org/t/p/w185/";
    private double rating;
    private JSONObject json;

//    {
//        "poster_path": "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg",
//        "adult": false,
//        "overview": "Framed in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope.",
//        "release_date": "1994-09-10",
//        "genre_ids": [
//        18,
//            80
//        ],
//        "id": 278,
//        "original_title": "The Shawshank Redemption",
//        "original_language": "en",
//        "title": "The Shawshank Redemption",
//        "backdrop_path": "/xBKGJQsAIeweesB79KC89FpBrVr.jpg",
//        "popularity": 8.100274,
//        "vote_count": 5493,
//        "video": false,
//        "vote_average": 8.34
//    },

    public Movie(JSONObject json) {
        this.json = json;
        try {
            this.id = json.getInt("id");
            this.rating = json.getDouble("vote_average");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public double getRating() {
        return rating;
    }


    public String getString(String property, String fallback) {
        try {
            switch (property) {
                case "image":
                    return baseImgUrl + json.getString("poster_path");
                default:
                    return json.optString(property, fallback);
            }
        } catch (JSONException e){
            return fallback;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Movie) && ((Movie) obj).id == id;
    }

    @Override
    public String toString() {
        return json.toString();
    }
}
