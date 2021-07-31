package com.example.hassan.demo.transport;


public class USSDMessage {
    private java.lang.String msisdn;
    private java.lang.String message;
    private com.example.hassan.model.ussd.UssdMessageMethod method;
    private int invokeId;
    private final org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary mapDialog;
    private java.util.Date time;
    private com.example.hassan.model.ussd.UssdMessageSource source;

    USSDMessage(org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary mapDialog) { /* compiled code */ }

    public com.example.hassan.demo.transport.USSDMessage createReplay(java.lang.String message, boolean end) { /* compiled code */ }

    public java.lang.String getMsisdn() { /* compiled code */ }

    void setMsisdn(java.lang.String msisdn) { /* compiled code */ }

    public java.lang.String getMessage() { /* compiled code */ }

    public void setMessage(java.lang.String message) { /* compiled code */ }

    public com.example.hassan.model.ussd.UssdMessageMethod getMethod() { /* compiled code */ }

    void setMethod(com.example.hassan.model.ussd.UssdMessageMethod method) { /* compiled code */ }

    public int getInvokeId() { /* compiled code */ }

    void setInvokeId(int invokeId) { /* compiled code */ }

    org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary getMapDialog() { /* compiled code */ }

    public com.example.hassan.model.ussd.UssdMessageSource getSource() { /* compiled code */ }

    public void setSource(com.example.hassan.model.ussd.UssdMessageSource source) { /* compiled code */ }

    public java.util.Date getTime() { /* compiled code */ }

    public void setTime(java.util.Date time) { /* compiled code */ }

    public java.lang.String toString() { /* compiled code */ }
}