package ga.dhaan.popularmovies.helpers;

import android.widget.AbsListView;
import android.widget.GridView;

/**
 * Created by thijs on 13-11-2016.
 * https://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView
 */

public abstract class EndlessScrolllistener implements GridView.OnScrollListener {
    private int visibleTreshHold;
    private int currentPage;
    private int previousTotalItemCount;
    private boolean loading;
    private int startingPageindex;

    public void reset(){
        visibleTreshHold = 12;
        currentPage = 0;
        previousTotalItemCount = 0;
        loading = true;
        startingPageindex = 1;
    }

    public EndlessScrolllistener(){
        reset();
    }
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageindex;
            previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.loading = true; }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        if (!loading && (firstVisibleItem + visibleItemCount + visibleTreshHold) >= totalItemCount) {
            loading = onLoadMore(currentPage + 1, totalItemCount);
        }
    }

    public abstract boolean onLoadMore(int page, int totalItemCount);
}
