package com.taboola.android.sdksamples.sdk_via_native;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.globalNotifications.GlobalNotificationReceiver;
import com.taboola.android.sdksamples.R;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.HashMap;
import java.util.List;

public class FeedWithMiddleArticleInsideRecyclerViewFragment extends Fragment implements GlobalNotificationReceiver.OnGlobalNotificationsListener {

    private static final String TAG             = "FeedWithMiddleArticle";
    private static final String TABOOLA_VIEW_ID = "123456";

    private static TaboolaWidget mMiddleTaboolaWidget;
    private static TaboolaWidget mBottomTaboolaWidget;

    private GlobalNotificationReceiver mGlobalNotificationReceiver = new GlobalNotificationReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlobalNotificationReceiver.registerNotificationsListener(this);
        mGlobalNotificationReceiver.registerReceiver(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMiddleTaboolaWidget = createTaboolaWidget(inflater.getContext(), false);
        mBottomTaboolaWidget = createTaboolaWidget(inflater.getContext(), true);

        buildMiddleArticleWidget(mMiddleTaboolaWidget);
        return inflater.inflate(R.layout.fragment_rv_sample, container, false);
    }

    static TaboolaWidget createTaboolaWidget(Context context, boolean infiniteWidget) {
        TaboolaWidget taboolaWidget = new TaboolaWidget(context);
        int           height        = infiniteWidget ? SdkDetailsHelper.getDisplayHeight(context) * 2 : ViewGroup.LayoutParams.WRAP_CONTENT;
        taboolaWidget.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
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
                .setViewId(TABOOLA_VIEW_ID);

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
        taboolaWidget.fetchContent();
    }

    private static void buildBottomArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester-demo")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed without video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setViewId(TABOOLA_VIEW_ID);

        taboolaWidget.setInterceptScroll(true);

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
        taboolaWidget.fetchContent();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView        recyclerView        = view.findViewById(R.id.feed_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new RecyclerViewAdapter(mMiddleTaboolaWidget, mBottomTaboolaWidget));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGlobalNotificationReceiver.unregisterNotificationsListener();
        mGlobalNotificationReceiver.unregisterReceiver(getActivity());
    }

    static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final List<ListItemsGenerator.FeedListItem> mData;
        private final TaboolaWidget                         mMiddleTaboolaWidget;
        private final TaboolaWidget                         mBottomTaboolaWidget;


        RecyclerViewAdapter(TaboolaWidget taboolaWidget, TaboolaWidget taboolaWidgetBottom) {
            mData = ListItemsGenerator.getGeneratedDataForWidgetDynamic(true);
            mMiddleTaboolaWidget = taboolaWidget;
            mBottomTaboolaWidget = taboolaWidgetBottom;
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
            // TODO 需求
            //  布局大概是 RecyclerView 里面的子item也是一个RecycierView， 然后加载SDK 请求信息流 ，可重新写一个demo 复现下

            View customParent = LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_item_layout, parent, false);
            switch (viewType) {
                case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_MID_ITEM:
                    return new ViewHolderTaboola((ViewGroup) customParent, mMiddleTaboolaWidget);

                case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_ITEM:
                    return new ViewHolderTaboola((ViewGroup) customParent, mBottomTaboolaWidget);

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
                RandomImageViewHolder         vh         = (RandomImageViewHolder) holder;
                ListItemsGenerator.RandomItem randomItem = (ListItemsGenerator.RandomItem) item;
                final ImageView               imageView  = vh.imageView;
                imageView.setBackgroundColor(randomItem.color);
                vh.textView.setText(randomItem.randomText);
            }
        }


        static class RandomImageViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imageView;
            private final TextView  textView;

            RandomImageViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.feed_item_iv);
                textView = view.findViewById(R.id.feed_item_tv);
            }
        }

        static class ViewHolderTaboola extends RecyclerView.ViewHolder {

            ViewHolderTaboola(ViewGroup viewGroup, View widget) {
                super(viewGroup);

                if (widget.getParent() != null) {
                    ((ViewGroup) widget.getParent()).removeView(widget);
                }

                viewGroup.addView(widget);

            }
        }
    }

    @Override
    public void taboolaDidReceiveAd(TaboolaWidget taboolaWidget) {
        Log.d(TAG, "taboolaDidReceiveAd() called with: taboolaWidget = [" + taboolaWidget + "]");
        if (taboolaWidget == mMiddleTaboolaWidget) {
            Log.d(TAG, "taboolaDidReceiveAd() called with: taboolaWidget = [" + taboolaWidget + "]");
            buildBottomArticleWidget(mBottomTaboolaWidget); //fetch content for the 2nd taboola asset only after completion of 1st item
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

        if (taboolaWidget.getId() == R.id.taboola_widget_middle) {
            buildBottomArticleWidget(mBottomTaboolaWidget); //fetch content for the 2nd taboola asset only after completion of 1st item
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

}