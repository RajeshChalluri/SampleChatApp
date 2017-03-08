package com.example.rajesh.samplechatapp.activities;

import android.os.Bundle;
import android.view.View;

import com.example.rajesh.sample.core.ui.activity.CoreSplashActivity;
import com.example.rajesh.samplechatapp.R;
import com.example.rajesh.samplechatapp.utils.SharedPreferencesUtil;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

/**
 * Created by rajesh on 26/9/16.
 */
public class SplashActivity extends CoreSplashActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            if (SharedPreferencesUtil.hasQbUser()) {
                proceedToTheNextActivityWithDelay();
                return;
            }
        createSession();
    }

    private void createSession() {
            QBAuth.createSession(new QBEntityCallback<QBSession>() {
                @Override
                public void onSuccess(QBSession result, Bundle params) {
                    proceedToTheNextActivity();
                }

                @Override
                public void onError(QBResponseException e) {
                    showSnackbarError(null, R.string.splash_create_session_error, e, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createSession();
                        }
                    });
                }
            });
    }

    @Override
    protected String getAppName() {
        return getString(R.string.splash_app_title);
    }

    @Override
    protected void proceedToTheNextActivity() {
        if (SharedPreferencesUtil.hasQbUser()) {
            DialogsActivity.start(this);
        } else {
            LoginActivity.start(this);
        }
        finish();
    }
}
