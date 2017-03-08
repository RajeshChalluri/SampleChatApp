package com.example.rajesh.samplechatapp.gcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.rajesh.sample.core.gcm.CoreGcmPushListenerService;
import com.example.rajesh.sample.core.utils.NotificationUtils;
import com.example.rajesh.sample.core.utils.ResourceUtils;
import com.example.rajesh.sample.core.utils.constant.GcmConsts;
import com.example.rajesh.samplechatapp.R;
import com.example.rajesh.samplechatapp.activities.SplashActivity;

public class GcmPushListenerService extends CoreGcmPushListenerService {
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void showNotification(String message) {
        NotificationUtils.showNotification(this, SplashActivity.class,
                ResourceUtils.getString(R.string.notification_title), message,
                R.mipmap.ic_launcher, NOTIFICATION_ID);
    }

    @Override
    protected void sendPushMessageBroadcast(String message) {
        Intent gcmBroadcastIntent = new Intent(GcmConsts.ACTION_NEW_GCM_EVENT);
        gcmBroadcastIntent.putExtra(GcmConsts.EXTRA_GCM_MESSAGE, message);

        LocalBroadcastManager.getInstance(this).sendBroadcast(gcmBroadcastIntent);
    }
}