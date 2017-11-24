package io.maerlyn.newsreader;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

/**
 * Handles actions for the side navigation drawer
 *
 * @author Maerlyn Broadbent
 */
public class NavDrawerHandler implements NavigationView.OnNavigationItemSelectedListener {

    private Activity activity;
    private DrawerLayout drawer;

    public NavDrawerHandler(Activity activity) {
        this.activity = activity;
        drawer = activity.findViewById(R.id.drawer_layout);
    }

    /**
     * Handle menu item taps
     *
     * @param item that was tapped
     * @return boolean
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        ViewPager pager = activity.findViewById(R.id.viewpager);
        pager.setCurrentItem(item.getItemId());

        closeDrawer();

        // returning false so the menu item isn't highlighted
        return false;
    }

    /**
     * Close the drawer if it's currently open
     */
    public void closeDrawer() {
        if (isDrawerOpen()) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * @return true of the drawer is currently open
     */
    public boolean isDrawerOpen() {
        return drawer.isDrawerOpen(GravityCompat.START);
    }
}