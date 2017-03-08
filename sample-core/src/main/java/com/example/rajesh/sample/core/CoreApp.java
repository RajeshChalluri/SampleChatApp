package com.example.rajesh.sample.core;

import android.app.Application;

import com.quickblox.core.QBSettings;

public class CoreApp extends Application {

    public static CoreApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized CoreApp getInstance() {
        if (instance == null) {
            instance = new CoreApp();
        }
        return instance;
    }

    public void initCredentials(String APP_ID, String AUTH_KEY, String AUTH_SECRET, String ACCOUNT_KEY) {
        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }
}