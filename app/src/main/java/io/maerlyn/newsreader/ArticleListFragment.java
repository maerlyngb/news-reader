package io.maerlyn.newsreader;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display a list of articles
 *
 * @author Maerlyn Broadbent
 */
public class ArticleListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Article>> {

    public static final String LOG_TAG = ArticleListFragment.class.getName();
    public static final String CATEGORY = "section";
    private static final int ARTICLE_LOADER_ID = 1;
    private String sectionId;
    private ArticleRecyclerAdapter adapter;

    /**
     * Return a new instance of AttrListFragment
     *
     * @param section of attraction to display
     * @return new Attr list instance
     */
    public static ArticleListFragment newInstance(Section section) {
        Bundle args = new Bundle();
        args.putString(CATEGORY, section.getId());
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called when the fragment is initially created.
     * loads any saved state and retrieves Bundle arguments.
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();

        if (arguments != null) {
            this.sectionId = arguments.getString(CATEGORY);
        }
    }

    /**
     * Called when the views are ready to be inflated
     *
     * @param inflater           used to inflate the views
     * @param container          parent views
     * @param savedInstanceState any saved state
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // used to display a scrolling list of attractions
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.article_list, container, false);

        // improves performance when items don't change size
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        this.adapter = new ArticleRecyclerAdapter(new ArrayList<Article>());

        // attach the adapter responsible for creating the attraction list
        recyclerView.setAdapter(this.adapter);

        if (Util.hasInternetConnection(getContext())) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        }

        return recyclerView;
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(getContext(), sectionId);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        if (articles != null && articles.size() > 0) {
            this.adapter.newDataSet(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        this.adapter.clear();
    }

}