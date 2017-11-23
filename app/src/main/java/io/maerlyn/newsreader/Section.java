package io.maerlyn.newsreader;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to differentiate between different categories of articles
 *
 * @author Maerlyn Broadbent
 */
public class Section {
    private String id;
    private String webTitle;
    private String webUrl;
    private String apiUrl;

    public Section (String id, String webTitle, String webUrl, String apiUrl){
        this.id = id;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.apiUrl = apiUrl;
    }

    public String getId() {
        return id;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}