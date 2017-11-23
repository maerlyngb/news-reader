package io.maerlyn.newsreader;

/**
 * Holds data about a specific news section
 *
 * @author Maerlyn Broadbent
 */
public class Section {
    private String id;
    private String webTitle;
    private String webUrl;
    private String apiUrl;

    public Section(String id, String webTitle, String webUrl, String apiUrl) {
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