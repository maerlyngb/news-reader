package io.maerlyn.newsreader;

import android.app.LoaderManager;
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

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Section>>  {

    private NavDrawerHandler navDrawerHandler;
    private static final int SECTION_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_activity_title);

        this.navDrawerHandler = new NavDrawerHandler(this);

        setupNavigationView(toolbar);

        if (Util.hasInternetConnection(this)) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(SECTION_LOADER_ID, null, this);
        }

        //setupTabbedContent();
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
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this.navDrawerHandler);
    }

    /**
     * Setup tabbed attraction tabs and content
     */
    private void setupTabbedContent(List<Section> sections) {
        ViewPager viewPager = findViewById(R.id.viewpager);

        if (viewPager != null) {
            ArticleListPageAdapter adapter = new ArticleListPageAdapter(getFragmentManager());

            // each fragment is one of the attraction lists
            for(Section section : sections){
                adapter.addFragment(ArticleListFragment.newInstance(section), section.getWebTitle());
            }

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
     * Close the side nav pane when the back button is pressed.
     * If the nav pane is already closed, perform the standard os action.
     */
    @Override
    public void onBackPressed() {
        if (this.navDrawerHandler.isDrawerOpen()) {
            this.navDrawerHandler.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Loader<List<Section>> onCreateLoader(int id, Bundle args) {
        return new GuardianApi(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Section>> loader, List<Section> sections) {
        if (sections != null && sections.size() > 0) {
            setupTabbedContent(sections);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Section>> loader) {
        //this.adapter.clear();
    }
}
