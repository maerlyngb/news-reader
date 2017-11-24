package io.maerlyn.newsreader;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Application starting screen
 *
 * @author Maerlyn Broadbent
 */
public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Section>> {

    private static final int SECTION_LOADER_ID = 0;

    private TextView noDataView;
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // displayed to the user when internet is not available
        // or of we get no data from the server
        this.noDataView = findViewById(R.id.no_tabs);

        // notify the user that we're getting data from the server
        this.spinner = findViewById(R.id.tab_spinner);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_activity_title);

        if (QueryUtils.hasInternetConnection(this)) {

            // if we have internet connectivity, start the loader to
            // get a list of news sections from the server
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(SECTION_LOADER_ID, null, this);

        } else {
            this.spinner.setVisibility(View.GONE);
            this.noDataView.setVisibility(View.VISIBLE);
            this.noDataView.setText(getString(R.string.no_internet_connection));
        }
    }

    /**
     * Setup the tabbed navigation view
     *
     * @param sections to display
     */
    private void setupTabbedContent(List<Section> sections) {
        ViewPager viewPager = findViewById(R.id.viewpager);

        if (viewPager != null) {
            ArticleListPageAdapter adapter = new ArticleListPageAdapter(getFragmentManager());

            // setup a fragment for each news section
            for (Section section : sections) {
                adapter.addFragment(ArticleListFragment.newInstance(section), section.getWebTitle());
            }

            // add all news section fragments as tabs
            viewPager.setAdapter(adapter);

            // display tabbed content
            TabLayout tabLayout = findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    /**
     * Triggered when creating the options menu in the toolbar
     *
     * @param menu to display in the toolbar
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Triggered when one of the items in the options menu was tapped
     *
     * @param item that was tapped
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Create a new loader to load news article sections
     *
     * @param id   of the loader
     * @param args {@link Bundle} data
     * @return list of sections
     */
    @Override
    public Loader<List<Section>> onCreateLoader(int id, Bundle args) {
        return new GuardianApi(this);
    }

    /**
     * When we have the section data, draw their tabs
     *
     * @param loader   data loader
     * @param sections returned data
     */
    @Override
    public void onLoadFinished(Loader<List<Section>> loader, List<Section> sections) {

        // we have sections!
        ProgressBar spinner = findViewById(R.id.tab_spinner);
        spinner.setVisibility(View.GONE);

        if (sections != null && sections.size() > 0) {
            setupTabbedContent(sections);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Section>> loader) {
        // loader reset
    }
}
