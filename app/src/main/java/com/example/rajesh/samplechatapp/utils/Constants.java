package com.example.rajesh.samplechatapp.utils;

import com.example.rajesh.sample.core.utils.ResourceUtils;
import com.example.rajesh.samplechatapp.R;

/**
 * Created by rajesh on 26/9/16.
 */

public interface Constants {

    // In GCM, the Sender ID is a project ID that you acquire from the API console
    String GCM_SENDER_ID = "761750217637";

    String QB_APP_ID = "47468";
    String QB_AUTH_KEY = "AvZ2ze3pCnDytBE";
    String QB_AUTH_SECRET = "GdLS2s2gPGNwgYv";
    String QB_ACCOUNT_KEY = "fnG2xvaXY9yTN2BX5Aeu";

//    String GCM_SENDER_ID = "761750217637";
//
//    String QB_APP_ID = "92";
//    String QB_AUTH_KEY = "wJHdOcQSxXQGWx5";
//    String QB_AUTH_SECRET = "BTFsj7Rtt27DAmT";
//    String QB_ACCOUNT_KEY = "rz2sXxBt5xgSxGjALDW6";
//
//    String QB_USERS_TAG = "webrtcusers";
//    String QB_USERS_PASSWORD = "12345678";

    int PREFERRED_IMAGE_SIZE_PREVIEW = ResourceUtils.getDimen(R.dimen.chat_attachment_preview_size);
    int PREFERRED_IMAGE_SIZE_FULL = ResourceUtils.dpToPx(320);
}
