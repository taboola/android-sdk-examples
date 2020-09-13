package com.taboola.android.sdksamples.tb_api;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import 	androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

/**
 * The initialization of TaboolaAPI is done in the Application class
 * If your project does not have an Application extending class, create one and then init TaboolaApi
 */

public class FourItemsApiFragment extends Fragment {

    private static final String TAG = FourItemsApiFragment.class.getSimpleName();
    private LinearLayout adContainer;
    private View attributionView;

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

        adContainer = view.findViewById(R.id.ad_container);

        /* Dark Mode support - change the background color of the adContainer to BLACK:

        adContainer.setBackgroundColor(Color.BLACK); */

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

                /*
           If we want to prevent 'available' event from being sent upon request
           (using setAvailable(false) will send the event upon render):

           TBPlacementRequest placementRequest = new TBPlacementRequest(placementName, 4)
                .setThumbnailSize(screenSize.x / 2, (screenSize.y / 6))
                .setAvailable(false);

           *** PLEASE DO NOT CHANGE THE VALUE OF  "setAvailable" TO FALSE WITHOUT GETTING YOUR ACCOUNT MANAGER'S APPROVAL ***
         */

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

                /* Dark Mode support - change text color to WHITE:

                TBTextView title = item.getTitleView(getContext());
                title.setTextColor(Color.WHITE); */

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
}
