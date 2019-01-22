package com.taboola.android.sdksamples.sdk_via_native;

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
import com.taboola.android.listeners.ScrollToTopListener;
import com.taboola.android.sdksamples.R;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.List;

public class FeedInsideRecycleViewCustomFragment extends Fragment implements ScrollToTopListener {

    private RecyclerView mRecyclerView;
    private CustomAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAdapter = new CustomAdapter(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rv_sample, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.feed_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        initScrollListeners();
    }

    private void initScrollListeners() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    // In this case: there is nothing to scroll in publisher content and taboola should get the scroll control
                    mAdapter.enableWidgetScrolling(true);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void onTaboolaWidgetOnTop() {
        mAdapter.enableWidgetScrolling(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mAdapter.removeOnScrollListener();
    }

    static class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        private final List<ListItemsGenerator.FeedListItem> mData;
        private TaboolaWidget mGlobalTaboolaView;
        private ScrollToTopListener mScrollToTopListener;
        private boolean mEnableWidgetScroll;


        CustomAdapter(ScrollToTopListener scrollToTopListener) {
            mData = ListItemsGenerator.getGeneratedData(false);
            this.mScrollToTopListener = scrollToTopListener;
        }

        public boolean isEnableWidgetScroll() {
            return mEnableWidgetScroll;
        }

        public void removeOnScrollListener() {
            if (mGlobalTaboolaView != null) {
                mGlobalTaboolaView.unregisterScrollToTopListener();
                mGlobalTaboolaView = null;
            }
        }

        public void enableWidgetScrolling(boolean enableWidgetScroll) {
            mEnableWidgetScroll = enableWidgetScroll;

            if (mGlobalTaboolaView != null) {
                mGlobalTaboolaView.setInterceptScroll(mEnableWidgetScroll);
                mGlobalTaboolaView.setScrollEnabled(mEnableWidgetScroll);
            }

        }

        @Override
        public int getItemViewType(int position) {
            ListItemsGenerator.FeedListItem item = getItem(position);
            return item.type;
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        @NonNull
        private ListItemsGenerator.FeedListItem getItem(int position) {
            return mData.get(position);
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_ITEM:
                    mGlobalTaboolaView = new TaboolaWidget(parent.getContext());
                    mGlobalTaboolaView.registerScrollToTopListener(mScrollToTopListener);
                    mGlobalTaboolaView.setAutoResizeHeight(false);
                    mGlobalTaboolaView.setInterceptScroll(mEnableWidgetScroll);
                    mGlobalTaboolaView.setScrollEnabled(mEnableWidgetScroll);
                    return new ViewHolderTaboola(mGlobalTaboolaView);

                default:
                case ListItemsGenerator.FeedListItem.ItemType.RANDOM_ITEM:
                    View appCompatImageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.random_item, parent, false);
                    return new RandomImageViewHolder(appCompatImageView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ListItemsGenerator.FeedListItem item = getItem(position);

            if (item.type == ListItemsGenerator.FeedListItem.ItemType.RANDOM_ITEM) {
                RandomImageViewHolder vh = (RandomImageViewHolder) holder;
                ListItemsGenerator.RandomItem randomItem = (ListItemsGenerator.RandomItem) item;
                final ImageView imageView = vh.imageView;
                imageView.setBackgroundColor(randomItem.color);
                vh.textView.setText(randomItem.randomText);
            } else if (item.type == ListItemsGenerator.FeedListItem.ItemType.TABOOLA_ITEM) {
                ViewHolderTaboola viewHolderTaboola = (ViewHolderTaboola) holder;
                if (viewHolderTaboola.mTaboolaWidget.isScrollEnabled() != mEnableWidgetScroll) {
                    viewHolderTaboola.mTaboolaWidget.setScrollEnabled(mEnableWidgetScroll);
                }
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
            private TaboolaWidget mTaboolaWidget;


            ViewHolderTaboola(TaboolaWidget taboolaWidget) {
                super(taboolaWidget);
                mTaboolaWidget = taboolaWidget;
                int height = SdkDetailsHelper.getDisplayHeight(taboolaWidget.getContext()) * 2;
                ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                taboolaWidget.setLayoutParams(params);
                taboolaWidget
                        .setPublisher("sdk-tester")
                        .setPageType("article")
                        .setPageUrl("https://blog.taboola.com")
                        .setPlacement("Feed without video")
                        .setMode("thumbs-feed-01")
                        .setTargetType("mix");
                taboolaWidget.fetchContent();
            }
        }

    }

}

