package com.example.hassan.demo.transport;

@org.springframework.stereotype.Component
public class M3UALinkConfiguration implements java.io.Serializable {
    private java.lang.String clientAddress;
    private java.lang.String clientPort;
    private java.lang.String serverPort;
    private java.lang.String serverAddress;
    private int serviceSpc;
    private int clientSpc;
    private int ssn;
    private int networkIndicator;

    public M3UALinkConfiguration(@org.springframework.beans.factory.annotation.Value("${demo.ussd.client-address}") java.lang.String clientAddress, @org.springframework.beans.factory.annotation.Value("${demo.ussd.client-port}") java.lang.String clientPort, @org.springframework.beans.factory.annotation.Value("${demo.ussd.server-address}") java.lang.String serverAddress, @org.springframework.beans.factory.annotation.Value("${demo.ussd.server-port}") java.lang.String serverPort, @org.springframework.beans.factory.annotation.Value("${demo.ussd.server-spc}") int serviceSpc, @org.springframework.beans.factory.annotation.Value("${demo.ussd.client-spc}") int clientSpc, @org.springframework.beans.factory.annotation.Value("${demo.ussd.ssn}") int ssn, @org.springframework.beans.factory.annotation.Value("${demo.ussd.network-indicator}") int networkIndicator) { /* compiled code */ }

    public java.lang.String getClientAddress() { /* compiled code */ }

    public void setClientAddress(java.lang.String clientAddress) { /* compiled code */ }

    public java.lang.String getClientPort() { /* compiled code */ }

    public void setClientPort(java.lang.String clientPort) { /* compiled code */ }

    public java.lang.String getServerPort() { /* compiled code */ }

    public void setServerPort(java.lang.String serverPort) { /* compiled code */ }

    public java.lang.String getServerAddress() { /* compiled code */ }

    public void setServerAddress(java.lang.String serverAddress) { /* compiled code */ }

    public int getServiceSpc() { /* compiled code */ }

    public int getClientSpc() { /* compiled code */ }

    public int getSsn() { /* compiled code */ }

    public int getNetworkIndicator() { /* compiled code */ }
}
