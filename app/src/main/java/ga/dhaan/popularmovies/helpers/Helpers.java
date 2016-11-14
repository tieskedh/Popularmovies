package ga.dhaan.popularmovies.helpers;

import android.content.Intent;
import android.support.v7.widget.ShareActionProvider;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by thijs on 14-11-2016.
 */

public class Helpers {
    public static void setShareText(ShareActionProvider provider, String text) {
        if(provider!=null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            if(SDK_INT < 21) {
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            } else {
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            }
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            shareIntent.setType("text/plain");
            provider.setShareIntent(shareIntent);
        }
    }
}
