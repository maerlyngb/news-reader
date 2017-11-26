package io.maerlyn.newsreader;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    ProgressBar spinner;
    private NavDrawerHandler navDrawerHandler;
    private TextView noDataView;
    private NavigationView navigationView;
    private List<Section> sections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // displayed to the user when internet is not available
        // or of we get no data from the server
        this.noDataView = findViewById(R.id.no_tabs);

        // notify the user that we're getting data from the server
        this.spinner = findViewById(R.id.tab_spinner);
        ;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_activity_title);

        this.navDrawerHandler = new NavDrawerHandler(this);
        setupNavigationView(toolbar);

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
     * Setup the side navigation drawer
     *
     * @param toolbar to place the toggle button
     */
    private void setupNavigationView(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        // toggle button in action bar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // listen for nav items being clicked
        this.navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this.navDrawerHandler);
    }

    /**
     * Setup the tabbed navigation view
     *
     * @param sections to display
     */
    private void setupTabbedContent(List<Section> sections) {
        this.sections = sections;
        ViewPager viewPager = findViewById(R.id.viewpager);

        if (viewPager != null) {
            ArticleListPageAdapter adapter = new ArticleListPageAdapter(getFragmentManager());

            // menu object for adding sections to the side nav
            Menu menu = this.navigationView.getMenu();

            // clean any previously loaded sections
            menu.clear();

            Menu sectionMenu = menu.addSubMenu(R.string.side_nav_sections_title);
            int groupId = 1;

            // load user preferences
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

            // setup a fragment for each news section
            for (int i = 0; i < sections.size(); i++) {
                Section section = sections.get(i);

                // check if the user has said they don't want to see this section
                if (sharedPrefs.getBoolean(section.getId(), true)) {
                    adapter.addFragment(ArticleListFragment.newInstance(section), section.getWebTitle());
                    sectionMenu.add(groupId, i, i, section.getWebTitle());
                }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, AppSettings.class);
            Bundle bundle = new Bundle();

            for (Section section : this.sections) {
                bundle.putParcelable(section.getId(), section);
            }

            settingsIntent.putExtras(bundle);
            startActivity(settingsIntent);
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
