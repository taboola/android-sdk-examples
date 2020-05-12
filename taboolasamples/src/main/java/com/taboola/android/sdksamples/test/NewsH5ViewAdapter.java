package com.taboola.android.sdksamples.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.sdksamples.R;
import com.taboola.android.sdksamples.sdk_via_native.ListItemsGenerator;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.HashMap;
import java.util.List;

/**
 * @author xingwei.huang (xwdz9989@gmail.com)
 * @since v1.0.0
 */
public class NewsH5ViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ListItemsGenerator.FeedListItem> mData;

    private TaboolaWidget mMiddleTaboolaWidget;
    private TaboolaWidget mBottomTaboolaWidget;

    private static final String TABOOLA_VIEW_ID = "123456";


    private static void buildBottomArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed without video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setViewId(TABOOLA_VIEW_ID);

        taboolaWidget.setInterceptScroll(true);

        HashMap<String, String> optionalPageCommands = new HashMap<>();
        optionalPageCommands.put("useOnlineTemplate", "true");
        taboolaWidget.setExtraProperties(optionalPageCommands);
        taboolaWidget.fetchContent();
    }

    static TaboolaWidget createTaboolaWidget(Context context, boolean infiniteWidget) {
        TaboolaWidget taboolaWidget = new TaboolaWidget(context);
        int           height        = infiniteWidget ? SdkDetailsHelper.getDisplayHeight(context) * 2 : ViewGroup.LayoutParams.WRAP_CONTENT;
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
                .setTargetType("mix")
                .setViewId(TABOOLA_VIEW_ID);

        HashMap<String, String> optionalPageCommands = new HashMap<>();
        optionalPageCommands.put("useOnlineTemplate", "true");
        taboolaWidget.setExtraProperties(optionalPageCommands);
        taboolaWidget.fetchContent();
    }

    public NewsH5ViewAdapter(Context context, TaboolaWidget taboolaWidget, TaboolaWidget taboolaWidgetBottom) {
        mData = ListItemsGenerator.getGeneratedDataForWidgetDynamic(true);
        mMiddleTaboolaWidget = createTaboolaWidget(context, false);
        mBottomTaboolaWidget = createTaboolaWidget(context, true);
        buildMiddleArticleWidget(mMiddleTaboolaWidget);
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
        View customParent = LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_item_layout, parent, false);

        switch (viewType) {
            case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_MID_ITEM:
                return new ViewHolderTaboola((ViewGroup) customParent,mMiddleTaboolaWidget);

            case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_ITEM:
                return new ViewHolderTaboola((ViewGroup) customParent,mBottomTaboolaWidget);
            default:
                return new ViewHolderTaboola((ViewGroup) customParent,mBottomTaboolaWidget);
        }
//        return new ViewHolderTaboola(new TextView(parent.getContext()));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListItemsGenerator.FeedListItem item = getItem(position);

    }


    static class ViewHolderTaboola extends RecyclerView.ViewHolder {

        ViewHolderTaboola(ViewGroup viewGroup,View widget) {
            super(viewGroup);

            if (widget.getParent() != null) {
                ((ViewGroup) widget.getParent()).removeView(widget);
            }

            viewGroup.addView(widget);
        }
    }
}
