package com.taboola.android.sdksamples.sdk_via_native;

import android.os.Bundle;
import 	androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.taboola.android.TaboolaWidget;
import com.taboola.android.listeners.TaboolaEventListener;
import com.taboola.android.sdksamples.R;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.HashMap;

public class OCClickHandlerFragment extends Fragment implements TaboolaEventListener {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_oc_click, container, false);
        buildMiddleArticleWidget(view.findViewById(R.id.taboola_widget_middle));
        return view;
    }

    private void buildMiddleArticleWidget(TaboolaWidget taboolaWidget) {
        taboolaWidget
                .setPublisher("sdk-tester-demo")
                .setPageType("article")
                .setPageUrl("https://blog.taboola.com")
                .setPlacement("Feed without video")
                .setMode("thumbs-feed-01")
                .setTargetType("mix")
                .setTaboolaEventListener(this)
                .setInterceptScroll(true);

        HashMap<String, String> extraProperties = new HashMap<>();
        extraProperties.put("useOnlineTemplate", "true");
        taboolaWidget.setExtraProperties(extraProperties);

        final int height = SdkDetailsHelper.getDisplayHeight(taboolaWidget.getContext());
        ViewGroup.LayoutParams params = taboolaWidget.getLayoutParams();

        if (params == null) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            taboolaWidget.setLayoutParams(params);
        } else {
            params.height = height;
        }

        taboolaWidget.fetchContent();
    }

    @Override
    public boolean taboolaViewItemClickHandler(String url, boolean isOrganic) {
        if (isOrganic && getActivity() != null) {
            //in real app you should use this url to open organic url inside your app
            Toast.makeText(getContext(), "mock load url: " + url, Toast.LENGTH_LONG).show();
            //Returning false - the click's default behavior is aborted. The app should display the Taboola Recommendation content on its own (for example, using an in-app browser).
            return false;
        }

        //Returning true - the click is a standard one and is sent to the Android OS for default behavior.
        return true;
    }

    @Override
    public void taboolaViewResizeHandler(TaboolaWidget taboolaWidget, int height) {

    }
}
