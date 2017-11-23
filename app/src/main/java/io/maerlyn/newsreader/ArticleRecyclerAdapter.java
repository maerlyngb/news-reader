package io.maerlyn.newsreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Populates a RecyclerView with a list of articles
 *
 * @author Maerlyn Broadbent
 */
public class ArticleRecyclerAdapter
        extends RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder> {
    private List<Article> articles;

    /**
     * Load the list of articles to display in a list
     *
     * @param articles to be displayed
     */
    public ArticleRecyclerAdapter(List<Article> articles) {
        this.articles = articles;
    }

    /**
     * Inflate a view for each article
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
     * @param holder object to cache the {@link View} references
     * @param index  of the article currently being displayed
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {
        if (hasArticles()) {

            // display article data
            final Article article = articles.get(index);
            holder.title.setText(article.getHeadline());
            holder.section.setText(article.getSectionName());
            holder.date.setText(article.getWebPublicationDate());

            // open the article url when the user taps on it's card
            holder.cardView.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getWebUrl()));
                if (browserIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(browserIntent);
                }
            });
        }
    }

    /**
     * Return the number of attractions
     *
     * @return number of attractions
     */
    @Override
    public int getItemCount() {
        if (hasArticles()) {
            return articles.size();
        } else {
            return 0;
        }
    }

    /**
     * Replace the current list of articles with a new list
     *
     * @param articles to display
     */
    public void newDataSet(List<Article> articles) {
        if (articles != null && articles.size() > 0) {
            this.articles.clear();
            this.articles.addAll(articles);
            notifyDataSetChanged();
        }
    }

    /**
     * Is there currently and article data?
     *
     * @return boolean
     */
    public boolean hasArticles() {
        return this.articles != null && this.articles.size() > 0;
    }

    /**
     * Clear list of articles from display and from memory
     */
    public void clear() {
        this.articles.clear();
        notifyDataSetChanged();
    }

    /**
     * Hold views and data about for an attraction summary
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final CardView cardView;
        public final TextView title;
        public final TextView section;
        public final TextView date;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            cardView = view.findViewById(R.id.card_view);
            title = view.findViewById(R.id.article_title);
            section = view.findViewById(R.id.article_section);
            date = view.findViewById(R.id.article_date);
        }
    }

}
