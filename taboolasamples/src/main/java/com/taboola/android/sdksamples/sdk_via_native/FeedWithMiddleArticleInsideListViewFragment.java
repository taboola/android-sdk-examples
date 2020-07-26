package com.taboola.android.sdksamples.sdk_via_native;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.globalNotifications.GlobalNotificationReceiver;
import com.taboola.android.sdksamples.R;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.HashMap;
import java.util.List;

public class FeedWithMiddleArticleInsideListViewFragment extends Fragment implements GlobalNotificationReceiver.OnGlobalNotificationsListener {

    private GlobalNotificationReceiver mGlobalNotificationReceiver = new GlobalNotificationReceiver();

    /**
     * We recommend using {@link android.support.v7.widget.RecyclerView
     */
    private static final String TAG = "FeedWithMiddleArticleIn";
    private static final String TABOOLA_VIEW_ID = "123456";
    private static TaboolaWidget mTaboolaWidgetBottom;
    private static TaboolaWidget mTaboolaWidgetMiddle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlobalNotificationReceiver.registerNotificationsListener(this);
        mGlobalNotificationReceiver.registerReceiver(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lv_sample, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTaboolaWidgetMiddle = createTaboolaWidget(view.getContext(), false);
        mTaboolaWidgetBottom = createTaboolaWidget(view.getContext(), true);

        buildMiddleArticleWidget(mTaboolaWidgetMiddle);

        ListView listView = view.findViewById(R.id.feed_lv);
        listView.setAdapter(new ListViewAdapter(mTaboolaWidgetMiddle, mTaboolaWidgetBottom));
    }

    /**
     * Helper method to create {@link TaboolaWidget} objects with height of 1 screen (for feed) or WRAP_CONTENT
     *
     * @param context
     * @param infiniteWidget
     * @return {@link TaboolaWidget} instance
     */
    static TaboolaWidget createTaboolaWidget(Context context, boolean infiniteWidget) {
        TaboolaWidget taboolaWidget = new TaboolaWidget(context);
        int height = infiniteWidget ? SdkDetailsHelper.getDisplayHeight(context) : ViewGroup.LayoutParams.WRAP_CONTENT;
        taboolaWidget.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        HashMap<String, String> extraProperties = new HashMap<>();
        extraProperties.put("useOnlineTemplate", "true");

        /*
            Adding this flag will require handling of collapsing Taboola on taboolaDidFailAd()
            in case Taboola fails to render (set to "true" by default):
            extraProperties.put("autoCollapseOnError", "false");
         */

        /*
            "detailedErrorCodes" set to "true" will stop the use of unorganized and unmaintained error reasons strings
            and will instead use detailed error codes (see taboolaDidFailAd() for more details)
         */
        extraProperties.put("detailedErrorCodes", "true");

        taboolaWidget.setExtraProperties(extraProperties);
        return taboolaWidget;
    }


    private static void buildMiddleArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester-demo")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Mid Article")
                .setMode("alternating-widget-without-video-1x1")
                .setTargetType("mix")
                .setViewId(TABOOLA_VIEW_ID) // setViewId - used in order to prevent duplicate recommendations between widgets on the same page view
                .fetchContent();
    }

    private static void buildBelowArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester-demo")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed without video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setViewId(TABOOLA_VIEW_ID)
                .setInterceptScroll(true);
        taboolaWidget.fetchContent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGlobalNotificationReceiver.unregisterNotificationsListener();
        mGlobalNotificationReceiver.unregisterReceiver(getActivity());
    }

    @Override
    public void taboolaDidReceiveAd(TaboolaWidget taboolaWidget) {
        if (taboolaWidget == mTaboolaWidgetMiddle) { //When Middle Article widget returns - we fetch the below article widget
            buildBelowArticleWidget(mTaboolaWidgetBottom);
        }
    }

    @Override
    public void taboolaDidFailAd(TaboolaWidget taboolaWidget, String reason) {
        // If "detailedErrorCodes is set to "true":
        switch (reason) {
            case "NO_ITEMS":
                Log.d(TAG, "Taboola server returned a valid response, but without any items");
                break;
            case "TIMEOUT":
                Log.d(TAG, "no response from Taboola server after 10 seconds");
                break;
            case "WRONG_PARAMS":
                Log.d(TAG, "wrong Taboola mode");
                break;
            case "RESPONSE_ERROR":
                Log.d(TAG, "Taboola server is not reachable, or it returned a bad response");
                break;
            default:
                Log.d(TAG, "UNKNOWN_ERROR");
        }

        if (taboolaWidget == mTaboolaWidgetMiddle) { //When Middle Article widget returns - we fetch the below article widget
            buildBelowArticleWidget(mTaboolaWidgetBottom);
        }
    }

    @Override
    public void taboolaViewResized(TaboolaWidget taboolaWidget, int i) {
        Log.d(TAG, "taboolaViewResized() called with: taboolaWidget = [" + taboolaWidget + "], i = [" + i + "]");
    }

    @Override
    public void taboolaItemDidClick(TaboolaWidget taboolaWidget) {
        Log.d(TAG, "taboolaItemDidClick() called with: taboolaWidget = [" + taboolaWidget + "]");
    }


    static class ListViewAdapter extends BaseAdapter {

        private final List<ListItemsGenerator.FeedListItem> mData;
        private TaboolaWidget mTaboolaWidgetMiddle;
        private TaboolaWidget mTaboolaWidgetBottom;


        ListViewAdapter(TaboolaWidget middleWidget, TaboolaWidget bottomWidget) {
            mData = ListItemsGenerator.getGeneratedData(true);
            mTaboolaWidgetMiddle = middleWidget;
            mTaboolaWidgetBottom = bottomWidget;
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
                    if (mTaboolaWidgetMiddle == null) {
                        buildMiddleArticleWidget(mTaboolaWidgetMiddle);
                    }
                    return new ViewHolderTaboola(mTaboolaWidgetMiddle, viewType);


                case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_ITEM:
                    if (mTaboolaWidgetBottom == null) {
                        mTaboolaWidgetBottom = createTaboolaWidget(parent.getContext(), true);
                    }
                    return new ViewHolderTaboola(mTaboolaWidgetBottom, viewType);

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
