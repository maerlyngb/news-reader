package io.maerlyn.newsreader;

/**
 * Created by maerlyn on 22/11/17.
 */

public class Article {
    private String headline;
    private String sectionName;
    private String webPublicationDate;
    private String webUrl;

    public Article(String sectionName, String headline, String webPublicationDate, String webUrl) {
        this.sectionName = sectionName;
        this.headline = headline;
        this.webPublicationDate = webPublicationDate;
        this.webUrl = webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
