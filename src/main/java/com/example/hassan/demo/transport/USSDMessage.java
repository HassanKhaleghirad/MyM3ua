package com.example.hassan.demo.transport;



import com.example.hassan.model.ussd.UssdMessageMethod;
import com.example.hassan.model.ussd.UssdMessageSource;
import java.util.Date;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;

public class USSDMessage {
    private String msisdn;
    private String message;
    private UssdMessageMethod method;
    private int invokeId;
    private final MAPDialogSupplementary mapDialog;
    private Date time = new Date();
    private UssdMessageSource source;

    USSDMessage(MAPDialogSupplementary mapDialog) {
        this.source = UssdMessageSource.server;
        this.mapDialog = mapDialog;
    }

    public USSDMessage createReplay(String message, boolean end) {
        USSDMessage replay = new USSDMessage(this.mapDialog);
        replay.setMessage(message);
        replay.setMsisdn(this.msisdn);
        replay.setInvokeId(this.invokeId);
        replay.setMethod(end ? UssdMessageMethod.end : UssdMessageMethod.continu);
        replay.setSource(UssdMessageSource.client);
        replay.setTime(new Date(System.currentTimeMillis()));
        return replay;
    }

    public String getMsisdn() {
        return this.msisdn;
    }

    void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UssdMessageMethod getMethod() {
        return this.method;
    }

    void setMethod(UssdMessageMethod method) {
        this.method = method;
    }

    public int getInvokeId() {
        return this.invokeId;
    }

    void setInvokeId(int invokeId) {
        this.invokeId = invokeId;
    }

    MAPDialogSupplementary getMapDialog() {
        return this.mapDialog;
    }

    public UssdMessageSource getSource() {
        return this.source;
    }

    public void setSource(UssdMessageSource source) {
        this.source = source;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String toString() {
        return "USSDMessage{msisdn='" + this.msisdn + '\'' + ", message='" + this.message + '\'' + ", method=" + this.method.getMethodName() + '}';
    }
}
