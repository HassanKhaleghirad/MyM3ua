package com.example.hassan.demo.transport;

import com.example.hassan.demo.transport.M3UALink;
import com.example.hassan.demo.transport.USSDMessage;

public interface M3UALinkMessageReceptionInterceptor {
    void onMessage(M3UALink paramM3UALink, USSDMessage paramUSSDMessage);
}

