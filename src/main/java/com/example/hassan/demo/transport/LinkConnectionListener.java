package com.example.hassan.demo.transport;

import com.example.hassan.demo.transport.M3UALink;
import com.example.hassan.model.link.LinkConnectionStatus;

public interface LinkConnectionListener {
    void onStatusChange(M3UALink paramM3UALink, String paramString, LinkConnectionStatus paramLinkConnectionStatus);
}

