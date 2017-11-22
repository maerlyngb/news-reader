package io.maerlyn.newsreader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maerlyn on 22/11/17.
 */

public class ArticleRecyclerAdapter
        extends RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder> {
    private List<Article> articles;

    /**
     * Load the list of attractions to display in a list
     *
     * @param articles to be displayed
     */
    public ArticleRecyclerAdapter(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * Inflate a view for each attraction summary
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_summary, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Bind data for each attraction summary
     *
     * @param holder
     * @param index  of the attraction in the dataset
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {
        final Article article = articles.get(index);
        holder.headline.setText(article.getHeadline());
    }

    /**
     * Return the number of attractions
     *
     * @return number of attractions
     */
    @Override
    public int getItemCount() {
        return articles.size();
    }

    /**
     * Hold views and data about for an attraction summary
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final CardView cardView;
        public final TextView headline;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            cardView = view.findViewById(R.id.card_view);
            headline = view.findViewById(R.id.article_headline);
        }
    }

}
