package com.taboola.android.sdksamples.tb_api.feed;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.taboola.android.api.TBPlacement;
import com.taboola.android.api.TBPlacementRequest;
import com.taboola.android.api.TBRecommendationRequestCallback;
import com.taboola.android.api.TBRecommendationsRequest;
import com.taboola.android.api.TBRecommendationsResponse;
import com.taboola.android.api.TaboolaApi;
import com.taboola.android.sdksamples.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The initialization of TaboolaAPI is done in the Application class
 * If your project does not have an Application extending class, create one and then init TaboolaApi
 */

public class FeedApiFragment extends Fragment {

    private Snackbar snackbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private TBPlacement mPlacement;
    private List<Object> mData = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_api, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final Drawable placeHolder = ContextCompat.getDrawable(getContext(), R.drawable.image_placeholder);
        TaboolaApi.getInstance().setImagePlaceholder(placeHolder);

        mRecyclerView = view.findViewById(R.id.endless_feed_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        EndlessScrollListener scrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextRecommendationsBatch(); // Fetch new recommendations as we approach the end of the recyclerview
            }
        };

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(scrollListener);

        OnAttributionClick handleAttributionClickListener = () -> TaboolaApi.getInstance().handleAttributionClick(getActivity());

        mData.add(getString(R.string.lorem_ipsum_short));
        mAdapter = new FeedAdapter(mData, handleAttributionClickListener);
        mRecyclerView.setAdapter(mAdapter);
        snackbar = Snackbar.make(mRecyclerView, "Waiting for network", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();

        fetchTaboolaRecommendations();
    }

    private void fetchTaboolaRecommendations() {
        final String placementName = "list_item";
        Point screenSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(screenSize);

        TBPlacementRequest placementRequest = new TBPlacementRequest(placementName, 4)
                .setThumbnailSize(screenSize.x / 2, (screenSize.y / 6)); // ThumbnailSize is optional

        TBRecommendationsRequest request = new TBRecommendationsRequest("https://example.com", "text");
        request.addPlacementRequest(placementRequest);

        TaboolaApi.getInstance().fetchRecommendations(request, new TBRecommendationRequestCallback() {
            @Override
            public void onRecommendationsFetched(TBRecommendationsResponse response) {
                TBPlacement tbPlacement = response.getPlacementsMap().get(placementName);
                if (tbPlacement != null && tbPlacement.getItems() != null && !tbPlacement.getItems().isEmpty()) {
                    addRecommendationToFeed(tbPlacement);
                }
                snackbar.dismiss();
            }

            @Override
            public void onRecommendationsFailed(Throwable throwable) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Fetch failed: " + throwable.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                snackbar.dismiss();
            }
        });
    }

    private void loadNextRecommendationsBatch() {
        TaboolaApi.getInstance().getNextBatchForPlacement(mPlacement, new TBRecommendationRequestCallback() {
            @Override
            public void onRecommendationsFetched(TBRecommendationsResponse response) {
                TBPlacement placement = response.getPlacementsMap().values().iterator().next(); // there will be only one placement
                addRecommendationToFeed(placement);
            }

            @Override
            public void onRecommendationsFailed(Throwable throwable) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Fetch failed: " + throwable.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addRecommendationToFeed(TBPlacement placement) {
        placement.prefetchThumbnails();
        mPlacement = placement;
        int currentSize = mAdapter.getItemCount();

        if (currentSize == 1) { // The seconds item in the list is the attribution view
            mData.add(new FeedAdapter.HeaderItem(R.drawable.icon_attribution, getString(R.string.attribution_view_text)));
        }

        mData.addAll(placement.getItems());
        mAdapter.notifyItemRangeInserted(currentSize, placement.getItems().size());
    }


}
