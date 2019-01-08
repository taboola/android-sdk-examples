package com.taboola.android.sdksamples.api.feed;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taboola.android.api.TBImageView;
import com.taboola.android.api.TBRecommendationItem;
import com.taboola.android.sdksamples.R;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private final int TYPE_HEADER = 0;
    private final int TYPE_TABOOLA = 1;
    private final int TYPE_ARTICLE = 2;

    private List<Object> mData;

    private OnAttributionClick mAttributionClickCallback;

    public FeedAdapter(List<Object> data, OnAttributionClick attributionClickCallback) {
        mData = data;
        mAttributionClickCallback = attributionClickCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_ARTICLE: {
                LinearLayout linearLayout = (LinearLayout) inflater
                        .inflate(R.layout.feed_article_item, parent, false);
                return new ArticleItemViewHolder(linearLayout);
            }
            case TYPE_HEADER: {
                LinearLayout linearLayout = (LinearLayout) inflater
                        .inflate(R.layout.feed_header_item, parent, false);
                return new HeaderItemViewHolder(linearLayout);
            }
            case TYPE_TABOOLA: {
                LinearLayout linearLayout = (LinearLayout) inflater
                        .inflate(R.layout.feed_taboola_item, parent, false);
                return new TaboolaItemViewHolder(linearLayout);
            }
            default: {
                throw new IllegalStateException("Unknown view type: " + viewType);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

            case TYPE_ARTICLE: {
                ArticleItemViewHolder articleHolder = (ArticleItemViewHolder) holder;
                String article = (String) mData.get(position);
                articleHolder.mArticleText.setText(article);
                break;
            }

            case TYPE_HEADER: {
                HeaderItemViewHolder headerHolder = (HeaderItemViewHolder) holder;
                headerHolder.attributionView.setOnClickListener(this);
                HeaderItemViewHolder demoHolder = (HeaderItemViewHolder) holder;
                HeaderItem dm = (HeaderItem) mData.get(position);
                demoHolder.mImageView.setImageResource(dm.getImageResourceId());
                demoHolder.mTextView.setText(dm.getText());
                break;
            }

            case TYPE_TABOOLA: {
                TBRecommendationItem item = (TBRecommendationItem) mData.get(position);
                LinearLayout adContainer = ((TaboolaItemViewHolder) holder).mAdContainer;
                TBImageView thumbnailView = item.getThumbnailView(adContainer.getContext());
                adContainer.addView(thumbnailView);
                adContainer.addView(item.getTitleView(adContainer.getContext()));
                adContainer.addView(item.getBrandingView(adContainer.getContext()));
                break;
            }

            default: {
                throw new IllegalStateException("Unknown view type: " + holder.getItemViewType());
            }
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof TaboolaItemViewHolder) {
            ((TaboolaItemViewHolder) holder).mAdContainer.removeAllViews();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof HeaderItem) {
            return TYPE_HEADER;
        } else if (mData.get(position) instanceof TBRecommendationItem) {
            return TYPE_TABOOLA;
        } else if (mData.get(position) instanceof String) {
            return TYPE_ARTICLE;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        mAttributionClickCallback.onAttributionClick();
    }


    static class HeaderItemViewHolder extends RecyclerView.ViewHolder {

        LinearLayout attributionView;
        ImageView mImageView;
        TextView mTextView;

        HeaderItemViewHolder(LinearLayout linearLayout) {
            super(linearLayout);
            mImageView = linearLayout.findViewById(R.id.ic_attribution);
            mTextView = linearLayout.findViewById(R.id.text_attribution);
            attributionView = linearLayout;
        }
    }

    static class TaboolaItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mAdContainer;

        TaboolaItemViewHolder(LinearLayout adContainer) {
            super(adContainer);
            mAdContainer = adContainer;
        }
    }

    static class ArticleItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mArticleView;
        TextView mArticleText;

        ArticleItemViewHolder(LinearLayout linearLayout) {
            super(linearLayout);
            mArticleText = linearLayout.findViewById(R.id.text_article);
            mArticleView = linearLayout;
        }
    }

    static class HeaderItem {
        private Integer mImageResourceId;
        private String mText;

        public HeaderItem(Integer imageResourceId, String text) {
            mImageResourceId = imageResourceId;
            mText = text;
        }

        public Integer getImageResourceId() {
            return mImageResourceId;
        }

        public void setImageResourceId(Integer imageResourceId) {
            mImageResourceId = imageResourceId;
        }

        public String getText() {
            return mText;
        }

        public void setText(String text) {
            mText = text;
        }
    }

}