package com.example.hassan.demo.transport;



import com.example.hassan.demo.transport.M3UALink;
import com.example.hassan.demo.transport.M3UALinkConfiguration;

public interface M3UALinkManager {
    M3UALink getLink(M3UALinkConfiguration paramM3UALinkConfiguration);

    M3UALink getLink(long paramLong);
}
