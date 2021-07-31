package com.example.hassan.demo.transport;



public interface M3UALinkManager {
    M3UALink getLink(M3UALinkConfiguration configuration);

    M3UALink getLink(long linkId);
}
