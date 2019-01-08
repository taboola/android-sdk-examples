package com.taboola.android.sdksamples.api;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.taboola.android.api.TBPlacement;
import com.taboola.android.api.TBPlacementRequest;
import com.taboola.android.api.TBRecommendationItem;
import com.taboola.android.api.TBRecommendationRequestCallback;
import com.taboola.android.api.TBRecommendationsRequest;
import com.taboola.android.api.TBRecommendationsResponse;
import com.taboola.android.api.TBTextView;
import com.taboola.android.api.TaboolaApi;
import com.taboola.android.sdksamples.R;

import java.util.Map;

public class FourItemsApiFragment extends Fragment {

    private static final String TAG = FourItemsApiFragment.class.getSimpleName();
    private LinearLayout adContainer;
    private View attributionView;
    private Handler handler = new Handler();
    private ProgressBar progressBar;


    public FourItemsApiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_widget_api, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initTaboolaApi();

        adContainer = view.findViewById(R.id.ad_container);
        progressBar = view.findViewById(R.id.progress_bar);

        attributionView = view.findViewById(R.id.attribution_view);
        attributionView.setOnClickListener(v -> TaboolaApi.getInstance().handleAttributionClick(getContext()));

        String placementName = "article";
        int recCount = 4; // specify how many recommendations should be returned

        Point mScreenSize = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(mScreenSize); // Screen size to be used as part of ThumbnailSize calculation in placement request

        String pageUrl = "http://example.com";
        String sourceType = "text";

        // prepare a placement request
        TBPlacementRequest placementRequest = new TBPlacementRequest(placementName, recCount)
                .setThumbnailSize(mScreenSize.x, (mScreenSize.y / 3)); // ThumbnailSize is optional

        // prepare the recommendation request
        TBRecommendationsRequest recommendationsRequest = new TBRecommendationsRequest(pageUrl, sourceType);
        recommendationsRequest.addPlacementRequest(placementRequest);

        // fetch recommendations
        TaboolaApi.getInstance().fetchRecommendations(recommendationsRequest, new TBRecommendationRequestCallback() {
            @Override
            public void onRecommendationsFetched(TBRecommendationsResponse response) {
                // map where key is the placement's name (you can store it as a member variable for convenience)
                Map<String, TBPlacement> placementsMap = response.getPlacementsMap();
                extractViewsFromItems(placementsMap, placementName);
            }

            @Override
            public void onRecommendationsFailed(Throwable throwable) {
                //TODO: handle error
                Log.d(TAG, "Failed: " + throwable.getMessage());
            }
        });

        setProgressBar();
    }

    private void setProgressBar() {
        // Start long running operation in a background thread
        new Thread(new Runnable() {
            int progressStatus = 0;

            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Animate the disappearance of the progress bar
                progressBar.animate().scaleY(0f).alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(250);

            }
        }).start();


    }

    /**
     * Method to extract recommendation items from placement
     *
     * @param placementsMap
     * @param placementName
     */
    private void extractViewsFromItems(Map<String, TBPlacement> placementsMap, String placementName) {
        TBPlacement placement = placementsMap.get(placementName);
        if (placement != null) {
            for (TBRecommendationItem item : placement.getItems()) {
                attributionView.setVisibility(View.VISIBLE);

                adContainer.addView(item.getThumbnailView(getContext()));
                adContainer.addView(item.getTitleView(getContext()));

                TBTextView brandingView = item.getBrandingView(getContext());
                if (brandingView != null) { // If branding text is not available null is returned
                    adContainer.addView(brandingView);
                }

                TBTextView descriptionView = item.getDescriptionView(getContext());
                if (descriptionView != null) {
                    adContainer.addView(descriptionView);
                }

                View v = new View(getContext());
                v.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        2
                ));
                v.setBackgroundColor(Color.parseColor("#B3B3B3"));

                adContainer.addView(v);
            }
        }
    }

    private void initTaboolaApi() {
        TaboolaApi.getInstance().init(getContext().getApplicationContext(),
                "sdk-tester",
                "d39df1418f5a4819c9eae2ca02595d57de98c246");
    }
}
