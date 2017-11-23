package io.maerlyn.newsreader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.HashMap;
import java.util.List;

/**
 * Created by maerlyn on 23/11/17.
 */

public class GuardianApi extends AsyncTaskLoader<List<Section>> {
    private final String baseUrl = "http://content.guardianapis.com/";
    private HashMap<String, String> queryParams = new HashMap<>();

    public GuardianApi(Context context) {
        super(context);
        queryParams.put("api-key", "2a2ac5ad-73bf-46e8-b416-ca457c21644a");
    }

    public String getUrl() {
        StringBuilder params = new StringBuilder();

        for (String key : queryParams.keySet()) {
            if (params.length() > 0) {
                params.append("&");
            }

            params.append(key);
            params.append("=");
            params.append(queryParams.get(key));
        }

        return baseUrl + "search?" + params.toString();
    }

    public void setCategory(String sectionId) {
        queryParams.put("section", sectionId);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Section> loadInBackground() {
        String apiParam = "api-key" + "=" + queryParams.get("api-key");
        return QueryUtils.fetchSectionData(baseUrl + "sections?" + apiParam);
    }
}
