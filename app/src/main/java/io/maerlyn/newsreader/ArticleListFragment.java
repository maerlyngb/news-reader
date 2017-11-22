package io.maerlyn.newsreader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class ArticleListFragment extends Fragment {
    public static final String CATEGORY = "category";
    private Category type;

    /**
     * Return a new instance of AttrListFragment
     *
     * @param category of attraction to display
     * @return new Attr list instance
     */
    public static ArticleListFragment newInstance(Category category) {
        Bundle args = new Bundle();
        args.putInt(CATEGORY, category.getValue());
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
            int typeVal = arguments.getInt(CATEGORY);
            // the type of attraction list to display
            type = Category.valueOf(typeVal);
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
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        // attach the adapter responsible for creating the attraction list
        recyclerView.setAdapter(new ArticleRecyclerAdapter(getData()));

        return recyclerView;
    }

    /**
     * Load attraction data
     *
     * @return list of attractions
     */
    private List<Article> getData() {
        switch (type) {
            case cat1:
                return DataUtil.getCat1();
            case cat2:
                return DataUtil.getCat2();
            case cat3:
                return DataUtil.getCat3();
            case cat4:
                return DataUtil.getCat4();
        }
        return null;
    }

}