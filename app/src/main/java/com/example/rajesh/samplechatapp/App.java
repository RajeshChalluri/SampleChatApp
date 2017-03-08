package com.example.rajesh.samplechatapp;

import com.example.rajesh.sample.core.CoreApp;
import com.example.rajesh.sample.core.utils.ActivityLifecycle;
import com.example.rajesh.samplechatapp.utils.Constants;


/**
 * Created by rajesh on 26/9/16.
 */

public class App extends CoreApp {
    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityLifecycle.init(this);
        initCredentials(Constants.QB_APP_ID, Constants.QB_AUTH_KEY, Constants.QB_AUTH_SECRET, Constants.QB_ACCOUNT_KEY);
    }
}