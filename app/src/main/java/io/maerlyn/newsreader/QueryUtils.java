package io.maerlyn.newsreader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * A set of static methods to retrieve data from given URLs
 *
 * @author Maerlyn Broadbent
 */
public final class QueryUtils {
    // used for log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String charType = "UTF-8";
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    private static final String HTTP_GET = "GET";

    // Guardian API response
    private static final String API_URL = "apiUrl";
    private static final String WEB_URL = "webUrl";
    private static final String WEB_TITLE = "webTitle";
    private static final String SECTION_NAME = "sectionName";
    private static final String WEB_PUBLICATION_DATE = "webPublicationDate";
    private static final String FIELDS = "fields";
    private static final String BY_LINE = "byline";
    private static final String ID = "id";
    private static final String THUMBNAIL = "thumbnail";
    private static final String STATUS = "status";
    private static final String OK = "ok";
    private static final String RESULTS = "results";
    private static final String RESPONSE = "response";
    private static final String ABOUT = "about";

    // private constructor to prevent instantiations
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Article} objects from a given URL
     *
     * @param requestUrl URL to query
     * @return list of {@link Article} objects
     */
    public static List<Article> fetchArticleData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            // get json string from url
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        if (jsonResponse == null || TextUtils.isEmpty(jsonResponse)) {
            // json string is empty so we shouldn't try to parse it
            Log.e(LOG_TAG, "Empty JSON string");
            return null;
        } else {
            try {
                // try and convert the json string to a JSONObject
                JSONObject raw = new JSONObject(jsonResponse);
                JSONObject response = raw.getJSONObject(RESPONSE);

                // have to got a good response from the serve?
                if (response.getString(STATUS).equals(OK)) {

                    // get a JSONArray of sections
                    JSONArray sections = response.getJSONArray(RESULTS);

                    // return a list of section objects
                    return extractArticles(sections);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "JSONExeption", e);
            }
        }

        // if we get here, something has gone wrong
        return null;
    }

    /**
     * Retun a list of {@link Section} objects from a given URL
     *
     * @param requestUrl URL to query
     * @return list of {@link Section} objects
     */
    public static List<Section> fetchSectionData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            // get json string from url
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        if (jsonResponse == null || TextUtils.isEmpty(jsonResponse)) {
            // json string is empty so we shouldn't try to parse it
            Log.e(LOG_TAG, "Empty JSON string");
            return null;
        } else {
            try {
                // try and convert the json string to a JSONObject
                JSONObject raw = new JSONObject(jsonResponse);
                JSONObject response = raw.getJSONObject(RESPONSE);

                // have to got a good response from the serve?
                if (response.getString(STATUS).equals(OK)) {

                    // get a JSONArray of sections
                    JSONArray sections = response.getJSONArray(RESULTS);

                    // return a list of section objects
                    return getSections(sections);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "JSONExeption", e);
            }
        }

        // if we get here, something has gone wrong
        return null;
    }

    /**
     * Return string response from a server  with a given url
     *
     * @param url of the server to query
     * @return String response
     * @throws IOException error getting the data
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod(HTTP_GET);
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException while getting data from the server ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(charType));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Returns a {@link URL} object for a given url string
     *
     * @param urlString string to convert to {@link URL}
     * @return {@link URL}
     */
    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "MalformedURLException ", e);
        }
        return url;
    }

    /**
     * Return a list of {@link Article} objects from a given JSON string
     *
     * @param results containing sections from the server
     * @return list of {@link Article objects}
     */
    public static ArrayList<Article> extractArticles(JSONArray results) {

        ArrayList<Article> articles = new ArrayList<>();
        try {
            for (int i = 0; i < results.length(); i++) {
                // create Section object for each item in the array
                // and add it to the sections ArrayList
                JSONObject articleJson = results.getJSONObject(i);
                Article article = new Article();
                article.setSectionName(articleJson.getString(SECTION_NAME));
                article.setHeadline(articleJson.getString(WEB_TITLE));
                article.setWebPublicationDate(articleJson.getString(WEB_PUBLICATION_DATE));
                article.setWebUrl(articleJson.getString(WEB_URL));

                if (articleJson.has(FIELDS)) {
                    JSONObject fields = articleJson.getJSONObject(FIELDS);

                    // Article Author
                    if (fields != null) {
                        if (fields.has(BY_LINE)) {
                            article.setAuthor(fields.getString(BY_LINE));
                        }
                    }

                    // Article Thumbnail
                    if (fields != null) {
                        if (fields.has(THUMBNAIL)) {
                            article.setThumbnailUrl(fields.getString(THUMBNAIL));
                        }
                    }
                }

                articles.add(article);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSONException ", e);
        }

        // Return the list of articles
        return articles;
    }

    /**
     * Return a list of {@link Section} objects from a given JSON string
     *
     * @param results containing sections from the server
     * @return list of {@link Section objects}
     */
    private static List<Section> getSections(JSONArray results) {
        ArrayList<Section> sections = new ArrayList<>();

        try {
            for (int i = 0; i < results.length(); i++) {
                // create Section object for each item in the array
                // and add it to the sections ArrayList
                JSONObject article = results.getJSONObject(i);

                if (article.getString(ID).equals(ABOUT)) {
                    continue;
                }

                sections.add(new Section(article.getString(ID),
                        article.getString(WEB_TITLE),
                        article.getString(WEB_URL),
                        article.getString(API_URL)));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSONException ", e);
        }

        // Return the list of sections
        return sections;
    }

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}