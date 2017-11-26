package io.maerlyn.newsreader;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Holds data about a specific news section
 * Implements {@link Parcelable} so we can pass instances as {@link android.os.Bundle} data
 *
 * @author Maerlyn Broadbent
 */
public class Section implements Parcelable {

    public static final Parcelable.Creator<Section> CREATOR = new Parcelable.Creator<Section>() {
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };

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

    public Section(Parcel in) {
        this.id = in.readString();
        this.webTitle = in.readString();
        this.webUrl = in.readString();
        this.apiUrl = in.readString();
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.webTitle);
        dest.writeString(this.webUrl);
        dest.writeString(this.apiUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}