package com.taboola.android.sdksamples.sdk_via_native;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.sdksamples.R;
import com.taboola.android.sdksamples.tabs.BaseTaboolaFragment;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.HashMap;
import java.util.List;


public class FeedInsideRecyclerViewFragment extends BaseTaboolaFragment {


    // In RecyclerView the widget will be rendered only when the user will scroll to widget
    // so no need to fetch when page is selected

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
        recyclerView.setAdapter(new RecyclerViewAdapter(mViewId));
    }

    public static FeedInsideRecyclerViewFragment getInstance(String viewId) {
        FeedInsideRecyclerViewFragment baseTaboolaFragment = new FeedInsideRecyclerViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIEW_ID, viewId);
        baseTaboolaFragment.setArguments(bundle);
        return baseTaboolaFragment;
    }


    static TaboolaWidget createTaboolaWidget(Context context, boolean infiniteWidget) {
        TaboolaWidget taboolaWidget = new TaboolaWidget(context);
        int height = infiniteWidget ? SdkDetailsHelper.getDisplayHeight(context) * 2 : ViewGroup.LayoutParams.WRAP_CONTENT;
        taboolaWidget.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        return taboolaWidget;
    }

    /**
     * if you are using {@link RecyclerView.Adapter#notifyDataSetChanged()} then you need to add keepDependencies flag
     *
     * @see WidgetDynamicThemeChange where you can see full example how to use this flag.
     */
    static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final List<ListItemsGenerator.FeedListItem> mData;
        private TaboolaWidget mInfiniteTaboolaView;
        private String mViewId;


        RecyclerViewAdapter(String viewId) {
            mData = ListItemsGenerator.getGeneratedData(false);
            mViewId = viewId;
        }

        private void buildBelowArticleWidget(TaboolaWidget taboolaWidget) {
            taboolaWidget
                    .setPublisher("sdk-tester-demo")
                    .setPageType("article")
                    .setPageUrl("https://blog.taboola.com")
                    .setPlacement("Feed without video")
                    .setMode("thumbs-feed-01")
                    .setTargetType("mix")
                    .setInterceptScroll(true);

            //optional
            if (!TextUtils.isEmpty(mViewId)) {
                taboolaWidget.setViewId(mViewId);
            }

            //used for enable horizontal scroll
            HashMap<String, String> optionalPageCommands = new HashMap<>();
            optionalPageCommands.put("enableHorizontalScroll", "true");
            optionalPageCommands.put("useOnlineTemplate", "true");
            taboolaWidget.setExtraProperties(optionalPageCommands);
            mInfiniteTaboolaView.setExtraProperties(optionalPageCommands);

            taboolaWidget.fetchContent();
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
                    if (mInfiniteTaboolaView == null) {
                        mInfiniteTaboolaView = createTaboolaWidget(parent.getContext(), true);
                        buildBelowArticleWidget(mInfiniteTaboolaView);
                    }

                    return new ViewHolderTaboola(mInfiniteTaboolaView);

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
