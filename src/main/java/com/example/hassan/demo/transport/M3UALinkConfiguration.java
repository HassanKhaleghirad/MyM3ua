package com.example.hassan.demo.transport;


import java.io.Serializable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class M3UALinkConfiguration implements Serializable {
    private String clientAddress;
    private String clientPort;
    private String serverPort;
    private String serverAddress;
    private int serviceSpc;
    private int clientSpc;
    private int ssn;
    private int networkIndicator;

    public M3UALinkConfiguration(@Value("${demo.ussd.client-address}") String clientAddress, @Value("${demo.ussd.client-port}") String clientPort, @Value("${demo.ussd.server-address}") String serverAddress, @Value("${demo.ussd.server-port}") String serverPort, @Value("${demo.ussd.server-spc}") int serviceSpc, @Value("${demo.ussd.client-spc}") int clientSpc, @Value("${demo.ussd.ssn}") int ssn, @Value("${demo.ussd.network-indicator}") int networkIndicator) {
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
        this.serverPort = serverPort;
        this.serverAddress = serverAddress;
        this.serviceSpc = serviceSpc;
        this.clientSpc = clientSpc;
        this.ssn = ssn;
        this.networkIndicator = networkIndicator;
    }

    public String getClientAddress() {
        return this.clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getClientPort() {
        return this.clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }

    public String getServerPort() {
        return this.serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerAddress() {
        return this.serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServiceSpc() {
        return this.serviceSpc;
    }

    public int getClientSpc() {
        return this.clientSpc;
    }

    public int getSsn() {
        return this.ssn;
    }

    public int getNetworkIndicator() {
        return this.networkIndicator;
    }
}
