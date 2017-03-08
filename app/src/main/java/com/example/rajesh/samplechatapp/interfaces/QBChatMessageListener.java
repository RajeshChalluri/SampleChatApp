package com.example.rajesh.samplechatapp.interfaces;

import com.quickblox.chat.QBChat;
import com.quickblox.chat.model.QBChatMessage;

/**
 * Created by rajesh on 26/9/16.
 */

public interface QBChatMessageListener {
    void onQBChatMessageReceived(QBChat chat, QBChatMessage message);
}
