package com.example.rajesh.samplechatapp.interfaces;

import com.quickblox.chat.model.QBChatMessage;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

/**
 * Created by rajesh on 26/9/16.
 */

public interface Chat {
    void sendMessage(QBChatMessage message) throws XMPPException, SmackException.NotConnectedException;

    void release() throws XMPPException;
}
