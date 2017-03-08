package com.example.ilya.gb_android2_les3_contentproviders;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Ilya on 08.03.2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(
                new FlowConfig.Builder(this)
                        .openDatabasesOnInit(true)
                        .build());
    }


}
