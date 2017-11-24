package io.maerlyn.newsreader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Asynchronously load articles from the Guardian API
 *
 * @author Maerlyn Broadbent
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    // section of articles we want to load
    String sectionId;

    public ArticleLoader(Context context, String sectionId) {
        super(context);
        this.sectionId = sectionId;
    }

    /**
     * Start the load
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Background task to load articles
     *
     * @return list of {@link Article objects}
     */
    @Override
    public List<Article> loadInBackground() {
        GuardianApi api = new GuardianApi(getContext());

        // set the section of articles we want to load
        api.setCategory(sectionId);
        return QueryUtils.fetchArticleData(api.getQueryUrl());
    }
}