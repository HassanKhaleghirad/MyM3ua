package com.example.hassan.demo.transport;

public class M3UALinkManagerImp implements com.example.hassan.demo.transport.M3UALinkManager {
    private java.util.LinkedHashMap<java.lang.Long,com.example.hassan.demo.transport.M3UALink> links;

    public M3UALinkManagerImp() { /* compiled code */ }

    public com.example.hassan.demo.transport.M3UALink getLink(com.example.hassan.demo.transport.M3UALinkConfiguration configuration) { /* compiled code */ }

    public com.example.hassan.demo.transport.M3UALink getLink(long linkId) { /* compiled code */ }

    static long generateLinkId(com.example.hassan.demo.transport.M3UALinkConfiguration configuration) { /* compiled code */ }

    static long generateId(java.lang.String clientIp, java.lang.String clientPort, java.lang.String serverIp, java.lang.String serverPort) { /* compiled code */ }
}
