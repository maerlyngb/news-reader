package io.maerlyn.newsreader;

import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display a list of articles
 *
 * @author Maerlyn Broadbent
 */
public class ArticleListFragment extends Fragment
        implements LoaderCallbacks<List<Article>> {

    public static final String SECTION = "section";

    // generate a random loader id so each fragment is different
    private static int ARTICLE_LOADER_ID = Integer.MIN_VALUE + (int) (Math.random() * Integer.MAX_VALUE);

    Context context;
    private String sectionId;
    private ArticleRecyclerAdapter adapter;
    private ProgressBar spinner;

    /**
     * Return a new instance of ArticleListFragment
     *
     * @param section of attraction to display
     * @return new Attr list instance
     */
    public static ArticleListFragment newInstance(Section section) {
        Bundle args = new Bundle();

        // add the section ID in the bundle
        args.putString(SECTION, section.getId());
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
            // load the section ID so we know which articles to display
            this.sectionId = arguments.getString(SECTION);
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

        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.article_list, container, false
        );

        // save a reference to context so we don't have to perform needless function calls
        this.context = layout.getContext();

        // loading animation
        spinner = new ProgressBar(context);
        spinner.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // display loading animation
        layout.addView(spinner);

        // Recycler view to display the list of articles
        RecyclerView recyclerView = layout.findViewById(R.id.article_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        this.adapter = new ArticleRecyclerAdapter(new ArrayList<>());

        // attach the adapter responsible for creating the attraction list
        recyclerView.setAdapter(this.adapter);

        if (QueryUtils.hasInternetConnection(context)) {
            // load news article data as a background process
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        }

        return layout;
    }

    /**
     * Return a new {@link ArticleLoader} to load articles in this section
     *
     * @param id   of the loader
     * @param args {@link Bundle} data
     * @return List of {@link Article} objects
     */
    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        spinner.setVisibility(View.VISIBLE);
        return new ArticleLoader(context, sectionId);
    }

    /**
     * Display the articles
     *
     * @param loader   loader
     * @param articles list of {@link Article} objects
     */
    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        if (articles != null && articles.size() > 0) {
            this.adapter.newDataSet(articles);
        }
        this.spinner.setVisibility(View.GONE);
    }

    /**
     * clear data when the loader has reset
     *
     * @param loader loader
     */
    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        this.adapter.clear();
    }

}