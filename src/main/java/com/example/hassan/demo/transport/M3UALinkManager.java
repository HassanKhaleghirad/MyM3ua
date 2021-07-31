package com.example.hassan.demo.transport;


public interface M3UALinkManager {
    com.example.hassan.demo.transport.M3UALink getLink(com.example.hassan.demo.transport.M3UALinkConfiguration configuration);

    com.example.hassan.demo.transport.M3UALink getLink(long linkId);
}