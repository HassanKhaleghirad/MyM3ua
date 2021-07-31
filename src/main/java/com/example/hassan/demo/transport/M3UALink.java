package com.example.hassan.demo.transport;

import com.example.hassan.demo.transport.LinkConnectionListener;
import com.example.hassan.demo.transport.M3UALinkConfiguration;
import com.example.hassan.demo.transport.M3UALinkMessageReceptionInterceptor;
import com.example.hassan.demo.transport.USSDMessage;
import com.example.hassan.model.link.LinkConnectionStatus;
import com.example.hassan.model.ussd.UssdMessageMethod;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.Management;
import org.mobicents.protocols.api.ManagementEventListener;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.M3UAManagementEventListener;
import org.restcomm.protocols.ss7.m3ua.impl.AspImpl;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialogListener;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.mtp.Mtp3UserPart;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.OriginationType;
import org.restcomm.protocols.ss7.sccp.RuleType;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.BCDOddEncodingScheme;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.EncodingScheme;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle0100;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class M3UALink {
    private final String CLIENT_ASSOCIATION_NAME;

    private int DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;

    private final ParameterFactoryImpl factory = new ParameterFactoryImpl();

    private int SERVER_SPC = 2;

    private int CLIENT_SPC = 1;

    private int NETWORK_INDICATOR = 2;

    private int SSN = 8;

    private int MAX_DIALOGS = 500000;

    private IpChannelType ipChannelType;

    private static Logger logger = LoggerFactory.getLogger(com.example.hassan.demo.transport.M3UALink.class);

    private M3UALinkConfiguration configuration;

    private TCAPStack tcapStack;

    private MAPStackImpl mapStack;

    private MAPProvider mapProvider;

    private SccpStackImpl sccpStack;

    private M3UAManagementImpl clientM3UAMgmt;

    private NettySctpManagementImpl sctpManagement;

    private final long id;

    private volatile LinkConnectionStatus connectionStatus;

    private M3UALinkMessageReceptionInterceptor messageReceptionInterceptor;

    private LinkConnectionListener connectionListener;

    private final Executor connectionListenerExecutor;

    private final Executor messageInterceptorExecutor;

    M3UALink(long id, M3UALinkConfiguration configuration) {
        this.id = id;
        this.configuration = configuration;
        this.CLIENT_ASSOCIATION_NAME = id + "";
        this.ipChannelType = IpChannelType.SCTP;
        this.CLIENT_SPC = configuration.getClientSpc();
        this.SERVER_SPC = configuration.getServiceSpc();
        this.NETWORK_INDICATOR = configuration.getNetworkIndicator();
        this.SSN = configuration.getSsn();
        logger.debug("IpChannelType=" + this.ipChannelType);
        logger.debug("CLIENT_IP=" + configuration.getClientAddress());
        logger.debug("CLIENT_PORT=" + configuration.getClientPort());
        logger.debug("SERVER_IP=" + configuration.getServerAddress());
        logger.debug("SERVER_PORT=" + configuration.getServerPort());
        logger.debug("CLIENT_SPC=" + this.CLIENT_SPC);
        logger.debug("SERVET_SPC=" + this.SERVER_SPC);
        logger.debug("NETWORK_INDICATOR=" + this.NETWORK_INDICATOR);
        logger.debug("SSN=" + this.SSN);
        logger.debug("DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT=" + this.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);
        this.connectionStatus = LinkConnectionStatus.idle;
        this.connectionListenerExecutor = Executors.newSingleThreadExecutor();
        this.messageInterceptorExecutor = Executors.newCachedThreadPool();
    }

    public synchronized void connect() {
        if (this.connectionStatus == LinkConnectionStatus.connected || this.connectionStatus == LinkConnectionStatus.connecting)
            return;
        setConnectionStatus("trying to connect", LinkConnectionStatus.connecting);
        try {
            initializeStack(this.ipChannelType);
        } catch (Exception e) {
            logger.error("Error", e);
            disconnect();
            setConnectionStatus("Error Connecting: " + e.getMessage(), LinkConnectionStatus.disconnected);
        }
    }

    protected void initializeStack(IpChannelType ipChannelType) throws Exception {
        initSCTP(ipChannelType);
        initM3UA();
        initSCCP();
        initTCAP();
        initMAP();
        setConnectionStatus("Starting M3UA");
        this.clientM3UAMgmt.startAsp(this.id + "");
        setConnectionStatus("M3UA Started");
    }

    private void initSCTP(IpChannelType ipChannelType) throws Exception {
        setConnectionStatus("Init SCTP");
        this.sctpManagement = new NettySctpManagementImpl(this.id + "");
        this.sctpManagement.start();
        this.sctpManagement.addManagementEventListener((ManagementEventListener)new Object(this));
        this.sctpManagement.removeAllResourses();
        Association association = this.sctpManagement.addAssociation(this.configuration.getClientAddress(), Integer.parseInt(this.configuration.getClientPort()), this.configuration.getServerAddress(), Integer.parseInt(this.configuration.getServerPort()), this.CLIENT_ASSOCIATION_NAME, ipChannelType, null);
        setConnectionStatus("SCTP init done - " + this.sctpManagement.getAssociation(this.CLIENT_ASSOCIATION_NAME).isConnected());
    }

    private void initM3UA() throws Exception {
        setConnectionStatus("Init M3UA");
        this.clientM3UAMgmt = new M3UAManagementImpl(this.id + "", null, null);
        this.clientM3UAMgmt.setTransportManagement((Management)this.sctpManagement);
        this.clientM3UAMgmt.setDeliveryMessageThreadCount(this.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);
        this.clientM3UAMgmt.start();
        this.clientM3UAMgmt.addM3UAManagementEventListener((M3UAManagementEventListener)new Object(this));
        this.clientM3UAMgmt.removeAllResourses();
        RoutingContext rc = this.factory.createRoutingContext(new long[] { 101L });
        TrafficModeType trafficModeType = this.factory.createTrafficModeType(2);
        NetworkAppearance na = this.factory.createNetworkAppearance(102L);
        this.clientM3UAMgmt.createAs(this.id + "", Functionality.IPSP, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, na);
        this.clientM3UAMgmt.createAspFactory(this.id + "", this.CLIENT_ASSOCIATION_NAME);
        AspImpl aspImpl = this.clientM3UAMgmt.assignAspToAs(this.id + "", this.id + "");
        this.clientM3UAMgmt.addRoute(this.SERVER_SPC, this.CLIENT_SPC, -1, this.id + "");
        setConnectionStatus("M3UA init done");
    }

    private void initSCCP() throws Exception {
        setConnectionStatus("Init SCCP");
        this.sccpStack = new SccpStackImpl(this.id + "");
        this.sccpStack.setMtp3UserPart(1, (Mtp3UserPart)this.clientM3UAMgmt);
        this.sccpStack.start();
        this.sccpStack.removeAllResourses();
        this.sccpStack.getSccpResource().addRemoteSpc(0, this.SERVER_SPC, 0, 0);
        this.sccpStack.getSccpResource().addRemoteSsn(0, this.SERVER_SPC, this.SSN, 0, false);
        this.sccpStack.getRouter().addMtp3ServiceAccessPoint(1, 1, this.CLIENT_SPC, this.NETWORK_INDICATOR, 0, null);
        this.sccpStack.getRouter().addMtp3Destination(1, 1, this.SERVER_SPC, this.SERVER_SPC, 0, 255, 255);
        ParameterFactoryImpl fact = new ParameterFactoryImpl();
        BCDOddEncodingScheme bCDOddEncodingScheme = new BCDOddEncodingScheme();
        GlobalTitle0100 globalTitle01001 = fact.createGlobalTitle("_", 0, NumberingPlan.ISDN_TELEPHONY, (EncodingScheme)bCDOddEncodingScheme, NatureOfAddress.INTERNATIONAL);
        GlobalTitle0100 globalTitle01002 = fact.createGlobalTitle("_", 0, NumberingPlan.ISDN_TELEPHONY, (EncodingScheme)bCDOddEncodingScheme, NatureOfAddress.INTERNATIONAL);
        SccpAddressImpl sccpAddressImpl1 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, (GlobalTitle)globalTitle01001, this.CLIENT_SPC, 0);
        this.sccpStack.getRouter().addRoutingAddress(1, (SccpAddress)sccpAddressImpl1);
        SccpAddressImpl sccpAddressImpl2 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, (GlobalTitle)globalTitle01002, this.SERVER_SPC, 0);
        this.sccpStack.getRouter().addRoutingAddress(2, (SccpAddress)sccpAddressImpl2);
        GlobalTitle0100 globalTitle01003 = fact.createGlobalTitle("*", 0, NumberingPlan.ISDN_TELEPHONY, (EncodingScheme)bCDOddEncodingScheme, NatureOfAddress.INTERNATIONAL);
        SccpAddressImpl sccpAddressImpl3 = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, (GlobalTitle)globalTitle01003, this.SERVER_SPC, 0);
        this.sccpStack.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, (SccpAddress)sccpAddressImpl3, "K", 1, -1, null, 0, null);
        this.sccpStack.getRouter().addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, (SccpAddress)sccpAddressImpl3, "K", 2, -1, null, 0, null);
        setConnectionStatus("SCCP init Done");
    }

    private void initTCAP() throws Exception {
        setConnectionStatus("Init TCAP");
        this.tcapStack = (TCAPStack)new TCAPStackImpl(this.id + "", this.sccpStack.getSccpProvider(), this.SSN);
        this.tcapStack.start();
        this.tcapStack.setDialogIdleTimeout(60000L);
        this.tcapStack.setInvokeTimeout(30000L);
        setConnectionStatus("TCAP init Done");
    }

    private void initMAP() throws Exception {
        setConnectionStatus("Init MAP");
        this.mapStack = new MAPStackImpl(this.id + "", this.tcapStack.getProvider());
        this.mapProvider = this.mapStack.getMAPProvider();
        this.mapProvider.addMAPDialogListener((MAPDialogListener)new DialogListener(this, null));
        this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener((MAPServiceSupplementaryListener)new SupplementryListener(this, null));
        this.mapProvider.getMAPServiceSupplementary().acivate();
        this.mapStack.start();
        setConnectionStatus("MAP init done");
    }

    public void reply(USSDMessage message) {
        try {
            logger.debug("sending replay {}", message);
            USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString(message
                    .getMessage());
            CBSDataCodingSchemeImpl cBSDataCodingSchemeImpl = new CBSDataCodingSchemeImpl(15);
            MAPDialogSupplementary dialog = message.getMapDialog();
            ISDNAddressString msisdn = this.mapProvider.getMAPParameterFactory().createISDNAddressString(AddressNature.international_number, NumberingPlan.ISDN, message
                    .getMsisdn());
            if (message.getMethod() == UssdMessageMethod.end) {
                dialog.addProcessUnstructuredSSRequest((CBSDataCodingScheme)cBSDataCodingSchemeImpl, ussdStrObj, null, msisdn);
                dialog.close(false);
            } else {
                dialog.addUnstructuredSSRequest((CBSDataCodingScheme)cBSDataCodingSchemeImpl, ussdStrObj, null, msisdn);
                dialog.send();
            }
        } catch (MAPException e) {
            e.printStackTrace();
            logger.warn("error sending replay " + message, (Throwable)e);
        }
    }

    public void terminate() {}

    public M3UALinkMessageReceptionInterceptor getMessageReceptionInterceptor() {
        return this.messageReceptionInterceptor;
    }

    public void setMessageReceptionInterceptor(M3UALinkMessageReceptionInterceptor messageReceptionInterceptor) {
        this.messageReceptionInterceptor = messageReceptionInterceptor;
    }

    public LinkConnectionListener getConnectionListener() {
        return this.connectionListener;
    }

    public void setConnectionListener(LinkConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public LinkConnectionStatus getConnectionStatus() {
        return this.connectionStatus;
    }

    public synchronized void disconnect() {
        if (this.connectionStatus != LinkConnectionStatus.connected && this.connectionStatus != LinkConnectionStatus.connecting)
            return;
        if (this.mapStack != null)
            this.mapStack.stop();
        if (this.tcapStack != null)
            this.tcapStack.stop();
        if (this.sccpStack != null) {
            this.sccpStack.removeAllResourses();
            this.sccpStack.stop();
        }
        if (this.clientM3UAMgmt != null) {
            try {
                this.clientM3UAMgmt.removeAllResourses();
            } catch (Exception e) {
                logger.error("Error", e);
            }
            try {
                this.clientM3UAMgmt.stop();
            } catch (Exception e) {
                logger.error("Error", e);
            }
        }
        if (this.sctpManagement != null) {
            try {
                this.sctpManagement.removeAllResourses();
            } catch (Exception e) {
                logger.error("Error", e);
            }
            try {
                this.sctpManagement.stop();
            } catch (Exception e) {
                logger.error("Error", e);
            }
        }
        setConnectionStatus(LinkConnectionStatus.disconnected);
    }

    public long getId() {
        return this.id;
    }

    private void setConnectionStatus(String info, LinkConnectionStatus status) {
        this.connectionStatus = status;
        if (this.connectionListener != null)
            this.connectionListenerExecutor.execute(() -> this.connectionListener.onStatusChange(this, info, status));
    }

    private void setConnectionStatus(LinkConnectionStatus status) {
        setConnectionStatus(null, status);
    }

    private synchronized void setConnectionStatus(String info) {
        setConnectionStatus(info, this.connectionStatus);
    }

    private void deliverMessage(USSDMessage message) {
        if (this.messageReceptionInterceptor != null)
            this.messageInterceptorExecutor.execute(() -> this.messageReceptionInterceptor.onMessage(this, message));
    }
}
}