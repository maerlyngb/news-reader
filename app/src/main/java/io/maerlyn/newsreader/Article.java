package io.maerlyn.newsreader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Holds data about a specific news article
 *
 * @author Maerlyn Broadbent
 */
public class Article {
    private String headline;
    private String author;
    private String sectionName;
    private String webPublicationDate;
    private String webUrl;
    private String thumbnailUrl;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getWebPublicationDate() {
        DateTimeFormatter fromStringFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime localDateTime = LocalDateTime.parse(this.webPublicationDate, fromStringFormatter);

        return localDateTime.format(DateTimeFormatter.ofPattern("dd MMM HH:mm"));
    }

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
