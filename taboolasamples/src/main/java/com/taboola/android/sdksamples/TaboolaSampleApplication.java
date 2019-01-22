package com.taboola.android.sdksamples;

import android.app.Application;

import com.taboola.android.api.TaboolaApi;
import com.taboola.android.js.TaboolaJs;

public class TaboolaSampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Required when using TaboolaJS integration
        TaboolaJs.getInstance().init(getApplicationContext());

        // Required when using TaboolaApi (Native Android) integration
        TaboolaApi.getInstance().init(getApplicationContext(),"sdk-tester","d39df1418f5a4819c9eae2ca02595d57de98c246");
    }

}