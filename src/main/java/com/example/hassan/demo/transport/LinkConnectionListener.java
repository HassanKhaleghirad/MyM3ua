package com.example.hassan.demo.transport;

public interface LinkConnectionListener {
    void onStatusChange(com.example.hassan.demo.transport.M3UALink link, java.lang.String info, com.example.hassan.model.link.LinkConnectionStatus status);
}
