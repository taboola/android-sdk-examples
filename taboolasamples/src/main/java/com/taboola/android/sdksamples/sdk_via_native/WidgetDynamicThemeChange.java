package com.taboola.android.sdksamples.sdk_via_native;

import android.content.Context;
import android.graphics.Color;
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

import java.util.HashMap;
import java.util.List;

public class WidgetDynamicThemeChange extends Fragment {

    private DynamicRecyclerViewAdapter mAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rv_dynamic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.feed_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new DynamicRecyclerViewAdapter(DynamicRecyclerViewAdapter.Theme.WHITE);
        recyclerView.setAdapter(mAdapter);

        android.support.v7.widget.SwitchCompat switchCompat = view.findViewById(R.id.simpleSwitch);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mAdapter.changeTheme(isChecked);
            mAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDestroy() {

        //make sure you are calling this method
        mAdapter.onDestroy();
        super.onDestroy();
    }


    static class DynamicRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final List<ListItemsGenerator.FeedListItem> mData;
        private TaboolaWidget mTaboolaWidget;
        private Theme mTheme;


        DynamicRecyclerViewAdapter(Theme theme) {
            mData = ListItemsGenerator.getGeneratedDataForWidgetDynamic();
            mTheme = theme;
        }

        public enum Theme {
            BLACK, WHITE
        }

        private static TaboolaWidget createTaboolaWidget(Context context) {
            TaboolaWidget taboolaWidget = new TaboolaWidget(context);

            // You should call this flag in case you are calling notifyDataSetChanged in order to make taboolaWidget keepDependencies.
            // if this flag is set to true, you need to call TaboolaWidget.onDestroy() once the adapter is going to be cleared,
            // in this sample it's in the onDestroy method of the fragment
            HashMap<String, String> optionalPageCommands = new HashMap<>();
            optionalPageCommands.put("keepDependencies", "true");
            taboolaWidget.setOptionalPageCommands(optionalPageCommands);

            taboolaWidget.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return taboolaWidget;
        }

        /**
         * you must to call this method because you use the keepDependencies flag. like in this example and as you need for walla app.
         */
        public void onDestroy() {
            if (mTaboolaWidget != null) {
                mTaboolaWidget.onDestroy();
            }
        }


        private void loadWidget(TaboolaWidget taboolaWidget) {
            taboolaWidget
                    .setPublisher("sdk-tester")
                    .setPageType("article")
                    .setPageUrl("https://blog.taboola.com")
                    .setTargetType("mix")
                    .setInterceptScroll(true);

            switch (mTheme) {
                case BLACK:
                    taboolaWidget
                            .setPlacement("Mid Article")
                            .setMode("alternating-widget-without-video-1-on-1");
                    break;
                case WHITE:
                    taboolaWidget
                            .setPlacement("Mid Article")
                            .setMode("alternating-widget-with-video-1-on-1");
                    break;
            }

            taboolaWidget.fetchContent();

        }

        public void changeTheme(boolean isChecked) {
            mTheme = isChecked ? Theme.BLACK : Theme.WHITE;
            if (mTaboolaWidget != null) {
                mTaboolaWidget.reset();
                loadWidget(mTaboolaWidget);
            }
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {


                case ListItemsGenerator.FeedListItem.ItemType.TABOOLA_MID_ITEM:
                    if (mTaboolaWidget == null) {
                        mTaboolaWidget = createTaboolaWidget(parent.getContext());
                        loadWidget(mTaboolaWidget);
                    }
                    return new ViewHolderTaboola(mTaboolaWidget);

                case ListItemsGenerator.FeedListItem.ItemType.RANDOM_ITEM:
                default:
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
                vh.textView.setText(randomItem.randomText);
                if (mTheme == Theme.BLACK) {
                    int bgColor = randomItem.color;
                    vh.itemView.setBackgroundColor(Color.BLACK);
                    vh.textView.setTextColor(Color.WHITE);
                    int invert = Color.rgb(255 - Color.red(bgColor),
                            255 - Color.green(bgColor),
                            255 - Color.blue(bgColor));

                    imageView.setBackgroundColor(invert);

                } else {
                    imageView.setBackgroundColor(randomItem.color);
                    vh.itemView.setBackgroundColor(Color.TRANSPARENT);
                    vh.textView.setTextColor(Color.BLACK);
                }
            }
        }


        @NonNull
        private ListItemsGenerator.FeedListItem getItem(int position) {
            return mData.get(position);
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

        static class ViewHolderTaboola extends RecyclerView.ViewHolder {
            ViewHolderTaboola(View view) {
                super(view);
            }
        }

        static class RandomImageViewHolder extends RecyclerView.ViewHolder {
            final ImageView imageView;
            final TextView textView;

            RandomImageViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.feed_item_iv);
                textView = view.findViewById(R.id.feed_item_tv);
            }
        }
    }

}
