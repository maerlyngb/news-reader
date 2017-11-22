package io.maerlyn.newsreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maerlyn on 22/11/17.
 */

public class DataUtil {
    public static List<Article> getCat1(){
        List<Article> articles = new ArrayList<>();

        articles.add(new Article("headline1"));
        articles.add(new Article("headline1"));
        articles.add(new Article("headline1"));
        articles.add(new Article("headline1"));

        return articles;
    }
    public static List<Article> getCat2(){
        List<Article> articles = new ArrayList<>();

        articles.add(new Article("headline2"));
        articles.add(new Article("headline2"));
        articles.add(new Article("headline2"));
        articles.add(new Article("headline2"));

        return articles;
    }
    public static List<Article> getCat3(){
        List<Article> articles = new ArrayList<>();

        articles.add(new Article("headline3"));
        articles.add(new Article("headline3"));
        articles.add(new Article("headline3"));
        articles.add(new Article("headline3"));

        return articles;
    }
    public static List<Article> getCat4(){
        List<Article> articles = new ArrayList<>();

        articles.add(new Article("headline4"));
        articles.add(new Article("headline4"));
        articles.add(new Article("headline4"));
        articles.add(new Article("headline4"));

        return articles;
    }
}
