package io.maerlyn.newsreader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by maerlyn on 22/11/17.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private final String LOG_TAG = ArticleLoader.class.getName();

    String sectionId;

    public ArticleLoader(Context context, String sectionId) {
        super(context);
        this.sectionId = sectionId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        GuardianApi api = new GuardianApi(getContext());
        api.setCategory(sectionId);
        return QueryUtils.fetchArticleData(api.getUrl());
    }
}