package com.taboola.android.sdksamples.sdk_via_native;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.sdksamples.R;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.HashMap;
import java.util.List;

public class FeedWithMiddleArticleInsideListViewFragment extends Fragment {

    /**
     * We recommend using {@link android.support.v7.widget.RecyclerView
     */

    private static final String TABOOLA_VIEW_ID = "123456";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lv_sample, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = view.findViewById(R.id.feed_lv);
        listView.setAdapter(new ListViewAdapter());
    }

    static TaboolaWidget createTaboolaWidget(Context context, boolean infiniteWidget) {
        TaboolaWidget taboolaWidget = new TaboolaWidget(context);
        int height = infiniteWidget ? SdkDetailsHelper.getDisplayHeight(context) : ViewGroup.LayoutParams.WRAP_CONTENT;
        taboolaWidget.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        HashMap<String, String> optionalPageCommands = new HashMap<>();
        optionalPageCommands.put("useOnlineTemplate", "true");
        taboolaWidget.setExtraProperties(optionalPageCommands);
        return taboolaWidget;
    }


    private static void buildMiddleArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Mid Article")
                .setMode("alternating-widget-with-video-1-on-1")
                .setTargetType("mix")
                .setViewId(TABOOLA_VIEW_ID); // setViewId - used in order to prevent duplicate recommendations between widgets on the same page view

        taboolaWidget.fetchContent();
    }

    private static void buildBelowArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed with video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setViewId(TABOOLA_VIEW_ID)
                .setInterceptScroll(true);

        taboolaWidget.fetchContent();
    }


    static class ListViewAdapter extends BaseAdapter {

        private final List<ListItemsGenerator.FeedListItem> mData;
        private TaboolaWidget mMiddleTaboolaWidget;


        ListViewAdapter() {
            mData = ListItemsGenerator.getGeneratedData(true);
        }


        @Override
        public @ListItemsGenerator.FeedListItem.ItemType
        int getItemViewType(int position) {
            ListItemsGenerator.FeedListItem item = getItem(position);
            return item.type;
        }


        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public ListItemsGenerator.FeedListItem getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {

                case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_MID_ITEM:
                    if (mMiddleTaboolaWidget == null) {
                        mMiddleTaboolaWidget = createTaboolaWidget(parent.getContext(), false);
                        buildMiddleArticleWidget(mMiddleTaboolaWidget);
                    }
                    return new ViewHolderTaboola(mMiddleTaboolaWidget, viewType);

                case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_ITEM:
                    TaboolaWidget taboolaWidgetInfinite = createTaboolaWidget(parent.getContext(), true);
                    buildBelowArticleWidget(taboolaWidgetInfinite);
                    return new ViewHolderTaboola(taboolaWidgetInfinite, viewType);

                default:
                case ListItemsGenerator.FeedListItem.ItemType.RANDOM_ITEM:
                    View appCompatImageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.random_item, parent, false);
                    return new RandomImageViewHolder(appCompatImageView, viewType);
            }
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            @ListItemsGenerator.FeedListItem.ItemType int viewType = getItemViewType(position);
            ViewHolder viewHolder;
            if (convertView == null || convertView.getTag() == null || ((ViewHolder) convertView.getTag()).mViewType != viewType) {
                viewHolder = onCreateViewHolder(parent, viewType);
                convertView = viewHolder.mView;
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            if (viewType == ListItemsGenerator.FeedListItem.ItemType.RANDOM_ITEM) {
                RandomImageViewHolder vh = (RandomImageViewHolder) viewHolder;
                ListItemsGenerator.FeedListItem item = getItem(position);
                ListItemsGenerator.RandomItem randomItem = (ListItemsGenerator.RandomItem) item;
                final ImageView imageView = vh.imageView;
                imageView.setBackgroundColor(randomItem.color);
                vh.textView.setText(randomItem.randomText);
            }


            return convertView;
        }


        static class RandomImageViewHolder extends ViewHolder {
            private final ImageView imageView;
            private final TextView textView;

            RandomImageViewHolder(View view, int viewType) {
                super(view, viewType);
                imageView = view.findViewById(R.id.feed_item_iv);
                textView = view.findViewById(R.id.feed_item_tv);
            }
        }

        static abstract class ViewHolder {

            private final @ListItemsGenerator.FeedListItem.ItemType
            int mViewType;
            View mView;

            ViewHolder(View view, int viewType) {
                mView = view;
                this.mViewType = viewType;
                view.setTag(this);
            }
        }

        static class ViewHolderTaboola extends ViewHolder {
            ViewHolderTaboola(View view, int viewType) {
                super(view, viewType);
            }
        }

    }
}
