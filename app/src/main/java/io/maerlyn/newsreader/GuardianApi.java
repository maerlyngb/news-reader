package io.maerlyn.newsreader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.HashMap;
import java.util.List;

/**
 * Handle Guardian API URLs
 *
 * @author Maerlyn Broadbent
 */
public class GuardianApi extends AsyncTaskLoader<List<Section>> {
    private final String baseUrl;
    private Context context;
    private HashMap<String, String> queryParams = new HashMap<>();

    public GuardianApi(Context context) {
        super(context);
        this.context = context;

        // base api url
        this.baseUrl = context.getString(R.string.base_url);

        // make sure the api key is in the query string
        queryParams.put(
                context.getString(R.string.api_key_key),
                context.getString(R.string.api_key_val));
    }

    public String getQueryUrl() {
        loadQueryParams();

        StringBuilder params = new StringBuilder();

        for (String key : queryParams.keySet()) {
            if (params.length() > 0) {
                params.append(context.getString(R.string.query_param_delim));
            }

            params.append(key);
            params.append(context.getString(R.string.key_val_delim));
            params.append(queryParams.get(key));
        }

        return baseUrl + context.getString(R.string.search_query_method) + context.getString(R.string.query_string_prefix) + params.toString();
    }

    private void loadQueryParams() {
        // which extra fields we want in the returned data
        queryParams.put(
                context.getString(R.string.show_fields_key),
                context.getString(R.string.show_fields_val));

        // how many results we want
        queryParams.put(
                context.getString(R.string.page_size_key),
                context.getString(R.string.page_size_val));
    }

    // set the news category that we want to query
    public void setCategory(String sectionId) {
        queryParams.put(context.getString(R.string.section_key), sectionId);
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
        // add api key to query string
        String apiParam = context.getString(R.string.api_key_key) +
                context.getString(R.string.key_val_delim) +
                queryParams.get(context.getString(R.string.api_key_key));

        // get data from server
        return QueryUtils.fetchSectionData(baseUrl +
                context.getString(R.string.section_query_method) +
                context.getString(R.string.query_string_prefix) + apiParam);
    }
}
