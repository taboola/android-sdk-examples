package com.taboola.android.sdksamples.standard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.sdksamples.R;
import com.taboola.android.sdksamples.data.ListItems;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.List;

public class FeedWithMiddleArticleInsideRecycleViewFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rv_sample, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.feed_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new RecyclerViewAdapter());
    }


    static TaboolaWidget createTaboolaWidget(Context context, boolean infiniteWidget) {
        TaboolaWidget taboolaWidget = new TaboolaWidget(context);
        int height = infiniteWidget ? SdkDetailsHelper.getDisplayHeight(context) * 2 : ViewGroup.LayoutParams.WRAP_CONTENT;
        taboolaWidget.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        return taboolaWidget;
    }


    private static void buildMiddleArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Mid Article")
                .setMode("alternating-widget-without-video-1-on-1")
                .setTargetType("mix");

        taboolaWidget.fetchContent();
    }

    private static void buildBelowArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed without video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setInterceptScroll(true);

        taboolaWidget.fetchContent();
    }

    static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final List<ListItems.FeedListItem> mData;
        private TaboolaWidget mMiddleTaboolaWidget;
        private TaboolaWidget mInfiniteTaboolaView;


        RecyclerViewAdapter() {
            mData = ListItems.getGeneratedData(true);
        }


        @Override
        public int getItemViewType(int position) {
            ListItems.FeedListItem item = getItem(position);
            return item.type;
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        @NonNull
        private ListItems.FeedListItem getItem(int position) {
            return mData.get(position);
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {

                case ListItems.FeedListItem.ItemType.TABOOLA_MID_ITEM:
                    if (mMiddleTaboolaWidget == null) {
                        mMiddleTaboolaWidget = createTaboolaWidget(parent.getContext(), false);
                        buildMiddleArticleWidget(mMiddleTaboolaWidget);
                    }
                    return new ViewHolderTaboola(mMiddleTaboolaWidget);

                case ListItems.FeedListItem.ItemType.TABOOLA_ITEM:
                    if (mInfiniteTaboolaView == null) {
                        mInfiniteTaboolaView = createTaboolaWidget(parent.getContext(), true);
                        buildBelowArticleWidget(mInfiniteTaboolaView);
                    }
                    return new ViewHolderTaboola(mInfiniteTaboolaView);

                default:
                case ListItems.FeedListItem.ItemType.RANDOM_ITEM:
                    View appCompatImageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.random_item, parent, false);
                    return new RandomImageViewHolder(appCompatImageView);
            }
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ListItems.FeedListItem item = getItem(position);

            if (item.type == ListItems.FeedListItem.ItemType.RANDOM_ITEM) {
                RandomImageViewHolder vh = (RandomImageViewHolder) holder;
                ListItems.RandomItem randomItem = (ListItems.RandomItem) item;
                final ImageView imageView = vh.imageView;
                imageView.setBackgroundColor(randomItem.color);
                vh.textView.setText(randomItem.randomText);
            }
        }


        static class RandomImageViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imageView;
            private final TextView textView;

            RandomImageViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.feed_item_iv);
                textView = view.findViewById(R.id.feed_item_tv);
            }
        }

        static class ViewHolderTaboola extends RecyclerView.ViewHolder {

            ViewHolderTaboola(View view) {
                super(view);
            }
        }
    }
}
