package io.maerlyn.newsreader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by maerlyn on 22/11/17.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private final String LOG_TAG = ArticleLoader.class.getName();
    String url;

    public ArticleLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if (url == null) {
            return null;
        }

        return QueryUtils.fetchEarthquakeData(url);
    }
}