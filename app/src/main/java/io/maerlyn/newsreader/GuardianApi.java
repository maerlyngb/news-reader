package io.maerlyn.newsreader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import java.util.HashMap;
import java.util.List;

/**
 * Handle Guardian API URLs
 *
 * @author Maerlyn Broadbent
 */
public class GuardianApi extends AsyncTaskLoader<List<Section>> {
    private final Uri apiUrl;
    private HashMap<String, String> queryParams = new HashMap<>();
    private String sectionId;

    public GuardianApi(Context context) {
        super(context);

        this.apiUrl = Uri.parse(context.getString(R.string.base_url));

        // make sure the api key is in the query string
        queryParams.put(
                context.getString(R.string.api_key_key),
                context.getString(R.string.api_key_val));
    }

    public String getQueryUrl() {
        Context c = getContext();

        Uri.Builder uriBuilder = this.apiUrl.buildUpon();

        uriBuilder.appendPath(c.getString(R.string.search_query_method));

        uriBuilder.appendQueryParameter(
                c.getString(R.string.api_key_key),
                c.getString(R.string.api_key_val));

        uriBuilder.appendQueryParameter(
                c.getString(R.string.section_key),
                this.sectionId);

        uriBuilder.appendQueryParameter(
                c.getString(R.string.show_fields_key),
                c.getString(R.string.show_fields_val));

        uriBuilder.appendQueryParameter(
                c.getString(R.string.page_size_key),
                c.getString(R.string.page_size_val));

        return uriBuilder.toString();
    }

    // set the news category that we want to query
    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * start background task to load section data
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Return a list of available news sections
     *
     * @return list of {@link Section} objects
     */
    @Override
    public List<Section> loadInBackground() {
        Context c = getContext();

        Uri.Builder uriBuilder = this.apiUrl.buildUpon();
        uriBuilder.appendPath(c.getString(R.string.section_query_method));

        uriBuilder.appendQueryParameter(
                c.getString(R.string.api_key_key),
                c.getString(R.string.api_key_val));

        // get data from server
        return QueryUtils.fetchSectionData(uriBuilder.toString());
    }
}
