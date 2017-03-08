package com.example.rajesh.samplechatapp.gcm;

import com.example.rajesh.sample.core.gcm.CoreGcmPushInstanceIDService;
import com.example.rajesh.samplechatapp.utils.Constants;

public class GcmPushInstanceIDService extends CoreGcmPushInstanceIDService {
    @Override
    protected String getSenderId() {
        return Constants.GCM_SENDER_ID;
    }
}
