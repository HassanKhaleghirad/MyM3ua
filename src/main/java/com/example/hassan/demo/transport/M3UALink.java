package com.example.hassan.demo.transport;

import com.example.hassan.model.link.LinkConnectionStatus;
import com.example.hassan.model.ussd.UssdMessageMethod;
import java.nio.charset.Charset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.mobicents.protocols.api.Association;
import org.mobicents.protocols.api.IpChannelType;
import org.mobicents.protocols.api.ManagementEventListener;
import org.mobicents.protocols.api.Server;
import org.mobicents.protocols.sctp.netty.NettySctpManagementImpl;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.m3ua.As;
import org.restcomm.protocols.ss7.m3ua.Asp;
import org.restcomm.protocols.ss7.m3ua.AspFactory;
import org.restcomm.protocols.ss7.m3ua.ExchangeType;
import org.restcomm.protocols.ss7.m3ua.Functionality;
import org.restcomm.protocols.ss7.m3ua.IPSPType;
import org.restcomm.protocols.ss7.m3ua.M3UAManagementEventListener;
import org.restcomm.protocols.ss7.m3ua.State;
import org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl;
import org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.restcomm.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;
import org.restcomm.protocols.ss7.m3ua.parameter.TrafficModeType;
import org.restcomm.protocols.ss7.map.MAPStackImpl;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPDialogListener;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPProvider;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource;
import org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic;
import org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason;
import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.sccp.LoadSharingAlgorithm;
import org.restcomm.protocols.ss7.sccp.OriginationType;
import org.restcomm.protocols.ss7.sccp.RuleType;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.BCDOddEncodingScheme;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.EncodingScheme;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.ss7ext.Ss7ExtInterface;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.comp.Problem;
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
    private static Logger logger = LoggerFactory.getLogger(M3UALink.class);
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
        if (this.connectionStatus != LinkConnectionStatus.connected && this.connectionStatus != LinkConnectionStatus.connecting) {
            this.setConnectionStatus("trying to connect", LinkConnectionStatus.connecting);

            try {
                this.initializeStack(this.ipChannelType);
            } catch (Exception var2) {
                logger.error("Error", var2);
                this.disconnect();
                this.setConnectionStatus("Error Connecting: " + var2.getMessage(), LinkConnectionStatus.disconnected);
            }

        }
    }

    protected void initializeStack(IpChannelType ipChannelType) throws Exception {
        this.initSCTP(ipChannelType);
        this.initM3UA();
        this.initSCCP();
        this.initTCAP();
        this.initMAP();
        this.setConnectionStatus("Starting M3UA");
        this.clientM3UAMgmt.startAsp(this.id + "");
        this.setConnectionStatus("M3UA Started");
    }

    private void initSCTP(IpChannelType ipChannelType) throws Exception {
        this.setConnectionStatus("Init SCTP");
        this.sctpManagement = new NettySctpManagementImpl(this.id + "");
        this.sctpManagement.start();
        this.sctpManagement.addManagementEventListener(new ManagementEventListener() {
            public void onServiceStarted() {
                M3UALink.logger.debug("SCTP onServiceStarted");
            }

            public void onServiceStopped() {
                M3UALink.logger.debug("SCTP onServiceStopped");
            }

            public void onRemoveAllResources() {
                M3UALink.logger.debug("SCTP onRemoveAllResources");
            }

            public void onServerAdded(Server server) {
                M3UALink.logger.debug("SCTP onServerAdded {}", server);
            }

            public void onServerRemoved(Server server) {
                M3UALink.logger.debug("SCTP onServerRemoved {}", server);
            }

            public void onAssociationAdded(Association association) {
                M3UALink.logger.debug("SCTP onAssociationAdded {}", association);
            }

            public void onAssociationRemoved(Association association) {
                M3UALink.logger.debug("SCTP onAssociationRemoved {}", association);
            }

            public void onAssociationStarted(Association association) {
                M3UALink.logger.debug("SCTP onAssociationStarted {}", association);
            }

            public void onAssociationStopped(Association association) {
                M3UALink.logger.debug("SCTP onAssociationStopped {}", association);
            }

            public void onAssociationUp(Association association) {
                M3UALink.logger.debug("SCTP onAssociationUp {}", association);
            }

            public void onAssociationDown(Association association) {
                M3UALink.logger.debug("SCTP onAssociationDown {}", association);
            }

            public void onServerModified(Server server) {
                M3UALink.logger.debug("SCTP onServerModified {}", server);
            }

            public void onAssociationModified(Association association) {
                M3UALink.logger.debug("SCTP onAssociationModified {}", association);
            }
        });
        this.sctpManagement.removeAllResourses();
        this.sctpManagement.addAssociation(this.configuration.getClientAddress(), Integer.parseInt(this.configuration.getClientPort()), this.configuration.getServerAddress(), Integer.parseInt(this.configuration.getServerPort()), this.CLIENT_ASSOCIATION_NAME, ipChannelType, (String[])null);
        this.setConnectionStatus("SCTP init done - " + this.sctpManagement.getAssociation(this.CLIENT_ASSOCIATION_NAME).isConnected());
    }

    private void initM3UA() throws Exception {
        this.setConnectionStatus("Init M3UA");
        this.clientM3UAMgmt = new M3UAManagementImpl(this.id + "", (String)null, (Ss7ExtInterface)null);
        this.clientM3UAMgmt.setTransportManagement(this.sctpManagement);
        this.clientM3UAMgmt.setDeliveryMessageThreadCount(this.DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT);
        this.clientM3UAMgmt.start();
        this.clientM3UAMgmt.addM3UAManagementEventListener(new M3UAManagementEventListener() {
            public void onServiceStarted() {
                M3UALink.logger.debug("M3UA onServiceStarted");
            }

            public void onServiceStopped() {
                M3UALink.logger.debug("M3UA onServiceStopped");
            }

            public void onRemoveAllResources() {
                M3UALink.logger.debug("M3UA onRemoveAllResources");
            }

            public void onAsCreated(As as) {
                M3UALink.logger.debug("M3UA onAsCreated {}", as);
            }

            public void onAsDestroyed(As as) {
                M3UALink.logger.debug("M3UA onAsDestroyed {}", as);
            }

            public void onAspFactoryCreated(AspFactory aspFactory) {
                M3UALink.logger.debug("M3UA onAspFactoryCreated {}", aspFactory);
            }

            public void onAspFactoryDestroyed(AspFactory aspFactory) {
                M3UALink.logger.debug("M3UA onAspFactoryDestroyed {}", aspFactory);
            }

            public void onAspAssignedToAs(As as, Asp asp) {
                M3UALink.logger.debug("M3UA onAspAssignedToAs {} {}", as, asp);
            }

            public void onAspUnassignedFromAs(As as, Asp asp) {
                M3UALink.logger.debug("M3UA onAspUnassignedFromAs {} {}", as, asp);
            }

            public void onAspFactoryStarted(AspFactory aspFactory) {
                M3UALink.logger.debug("M3UA onAspFactoryStarted {}", aspFactory);
            }

            public void onAspFactoryStopped(AspFactory aspFactory) {
                M3UALink.logger.debug("M3UA onAspFactoryStopped {}", aspFactory);
            }

            public void onAspActive(Asp asp, State state) {
                M3UALink.logger.debug("M3UA onAspActive {} {}", asp, state);
            }

            public void onAspInactive(Asp asp, State state) {
                M3UALink.logger.debug("M3UA onAspInactive {} {}", asp, state);
            }

            public void onAspDown(Asp asp, State state) {
                M3UALink.logger.debug("M3UA onAspDown {} {}", asp, state);
            }

            public void onAsActive(As as, State state) {
                M3UALink.logger.debug("M3UA onAsActive {} {}", as, state);
                M3UALink.this.setConnectionStatus("M3UA Connected", LinkConnectionStatus.connected);
            }

            public void onAsPending(As as, State state) {
                M3UALink.logger.debug("M3UA onAsPending {} {}", as, state);
                M3UALink.this.setConnectionStatus("M3UA AS Pending", LinkConnectionStatus.connecting);
            }

            public void onAsInactive(As as, State state) {
                M3UALink.logger.debug("M3UA onAsInactive {} {}", as, state);
            }

            public void onAsDown(As as, State state) {
                M3UALink.logger.debug("M3UA onAsDown {} {}", as, state);
            }
        });
        this.clientM3UAMgmt.removeAllResourses();
        RoutingContext rc = this.factory.createRoutingContext(new long[]{101L});
        TrafficModeType trafficModeType = this.factory.createTrafficModeType(2);
        NetworkAppearance na = this.factory.createNetworkAppearance(102L);
        this.clientM3UAMgmt.createAs(this.id + "", Functionality.IPSP, ExchangeType.SE, IPSPType.CLIENT, rc, trafficModeType, 1, na);
        this.clientM3UAMgmt.createAspFactory(this.id + "", this.CLIENT_ASSOCIATION_NAME);
        Asp asp = this.clientM3UAMgmt.assignAspToAs(this.id + "", this.id + "");
        this.clientM3UAMgmt.addRoute(this.SERVER_SPC, this.CLIENT_SPC, -1, this.id + "");
        this.setConnectionStatus("M3UA init done");
    }

    private void initSCCP() throws Exception {
        this.setConnectionStatus("Init SCCP");
        this.sccpStack = new SccpStackImpl(this.id + "");
        this.sccpStack.setMtp3UserPart(1, this.clientM3UAMgmt);
        this.sccpStack.start();
        this.sccpStack.removeAllResourses();
        this.sccpStack.getSccpResource().addRemoteSpc(0, this.SERVER_SPC, 0, 0);
        this.sccpStack.getSccpResource().addRemoteSsn(0, this.SERVER_SPC, this.SSN, 0, false);
        this.sccpStack.getRouter().addMtp3ServiceAccessPoint(1, 1, this.CLIENT_SPC, this.NETWORK_INDICATOR, 0, (String)null);
        this.sccpStack.getRouter().addMtp3Destination(1, 1, this.SERVER_SPC, this.SERVER_SPC, 0, 255, 255);
        org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl fact = new org.restcomm.protocols.ss7.sccp.impl.parameter.ParameterFactoryImpl();
        EncodingScheme ec = new BCDOddEncodingScheme();
        GlobalTitle gt1 = fact.createGlobalTitle("_", 0, NumberingPlan.ISDN_TELEPHONY, ec, NatureOfAddress.INTERNATIONAL);
        GlobalTitle gt2 = fact.createGlobalTitle("_", 0, NumberingPlan.ISDN_TELEPHONY, ec, NatureOfAddress.INTERNATIONAL);
        SccpAddress localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, this.CLIENT_SPC, 0);
        this.sccpStack.getRouter().addRoutingAddress(1, localAddress);
        SccpAddress remoteAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, this.SERVER_SPC, 0);
        this.sccpStack.getRouter().addRoutingAddress(2, remoteAddress);
        GlobalTitle gt = fact.createGlobalTitle("*", 0, NumberingPlan.ISDN_TELEPHONY, ec, NatureOfAddress.INTERNATIONAL);
        SccpAddress pattern = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, this.SERVER_SPC, 0);
        this.sccpStack.getRouter().addRule(1, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.REMOTE, pattern, "K", 1, -1, (Integer)null, 0, (SccpAddress)null);
        this.sccpStack.getRouter().addRule(2, RuleType.SOLITARY, LoadSharingAlgorithm.Bit0, OriginationType.LOCAL, pattern, "K", 2, -1, (Integer)null, 0, (SccpAddress)null);
        this.setConnectionStatus("SCCP init Done");
    }

    private void initTCAP() throws Exception {
        this.setConnectionStatus("Init TCAP");
        this.tcapStack = new TCAPStackImpl(this.id + "", this.sccpStack.getSccpProvider(), this.SSN);
        this.tcapStack.start();
        this.tcapStack.setDialogIdleTimeout(60000L);
        this.tcapStack.setInvokeTimeout(30000L);
        this.setConnectionStatus("TCAP init Done");
    }

    private void initMAP() throws Exception {
        this.setConnectionStatus("Init MAP");
        this.mapStack = new MAPStackImpl(this.id + "", this.tcapStack.getProvider());
        this.mapProvider = this.mapStack.getMAPProvider();
        this.mapProvider.addMAPDialogListener(new M3UALink.DialogListener());
        this.mapProvider.getMAPServiceSupplementary().addMAPServiceListener(new M3UALink.SupplementryListener());
        this.mapProvider.getMAPServiceSupplementary().acivate();
        this.mapStack.start();
        this.setConnectionStatus("MAP init done");
    }

    public void reply(USSDMessage message) {
        try {
            logger.debug("sending replay {}", message);
            USSDString ussdStrObj = this.mapProvider.getMAPParameterFactory().createUSSDString(message.getMessage());
            CBSDataCodingScheme ussdDataCodingScheme = new CBSDataCodingSchemeImpl(15);
            MAPDialogSupplementary dialog = message.getMapDialog();
            ISDNAddressString msisdn = this.mapProvider.getMAPParameterFactory().createISDNAddressString(AddressNature.international_number, org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan.ISDN, message.getMsisdn());
            if (message.getMethod() == UssdMessageMethod.end) {
                dialog.addProcessUnstructuredSSRequest(ussdDataCodingScheme, ussdStrObj, (AlertingPattern)null, msisdn);
                dialog.close(false);
            } else {
                dialog.addUnstructuredSSRequest(ussdDataCodingScheme, ussdStrObj, (AlertingPattern)null, msisdn);
                dialog.send();
            }
        } catch (MAPException var6) {
            var6.printStackTrace();
            logger.warn("error sending replay " + message, var6);
        }

    }

    public void terminate() {
    }

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
        if (this.connectionStatus == LinkConnectionStatus.connected || this.connectionStatus == LinkConnectionStatus.connecting) {
            if (this.mapStack != null) {
                this.mapStack.stop();
            }

            if (this.tcapStack != null) {
                this.tcapStack.stop();
            }

            if (this.sccpStack != null) {
                this.sccpStack.removeAllResourses();
                this.sccpStack.stop();
            }

            if (this.clientM3UAMgmt != null) {
                try {
                    this.clientM3UAMgmt.removeAllResourses();
                } catch (Exception var5) {
                    logger.error("Error", var5);
                }

                try {
                    this.clientM3UAMgmt.stop();
                } catch (Exception var4) {
                    logger.error("Error", var4);
                }
            }

            if (this.sctpManagement != null) {
                try {
                    this.sctpManagement.removeAllResourses();
                } catch (Exception var3) {
                    logger.error("Error", var3);
                }

                try {
                    this.sctpManagement.stop();
                } catch (Exception var2) {
                    logger.error("Error", var2);
                }
            }

            this.setConnectionStatus(LinkConnectionStatus.disconnected);
        }
    }

    public long getId() {
        return this.id;
    }

    private void setConnectionStatus(String info, LinkConnectionStatus status) {
        this.connectionStatus = status;
        if (this.connectionListener != null) {
            this.connectionListenerExecutor.execute(() -> {
                this.connectionListener.onStatusChange(this, info, status);
            });
        }

    }

    private void setConnectionStatus(LinkConnectionStatus status) {
        this.setConnectionStatus((String)null, status);
    }

    private synchronized void setConnectionStatus(String info) {
        this.setConnectionStatus(info, this.connectionStatus);
    }

    private void deliverMessage(USSDMessage message) {
        if (this.messageReceptionInterceptor != null) {
            this.messageInterceptorExecutor.execute(() -> {
                this.messageReceptionInterceptor.onMessage(this, message);
            });
        }

    }

    private class SupplementryListener implements MAPServiceSupplementaryListener {
        private SupplementryListener() {
        }

        public void onErrorComponent(MAPDialog mapDialog, Long invokeId, MAPErrorMessage mapErrorMessage) {
            M3UALink.logger.error(String.format("onErrorComponent for Dialog=%d and invokeId=%d MAPErrorMessage=%s", mapDialog.getLocalDialogId(), invokeId, mapErrorMessage));
        }

        public void onRejectComponent(MAPDialog mapDialog, Long invokeId, Problem problem, boolean isLocalOriginated) {
            M3UALink.logger.error(String.format("onRejectComponent for Dialog=%d and invokeId=%d Problem=%s isLocalOriginated=%s", mapDialog.getLocalDialogId(), invokeId, problem, isLocalOriginated));
        }

        public void onInvokeTimeout(MAPDialog mapDialog, Long invokeId) {
            M3UALink.logger.error(String.format("onInvokeTimeout for Dialog=%d and invokeId=%d", mapDialog.getLocalDialogId(), invokeId));
        }

        public void onProcessUnstructuredSSRequest(ProcessUnstructuredSSRequest procUnstrReqInd) {
            M3UALink.logger.debug("onProcessUnstructuredSSRequest: {}", procUnstrReqInd);
            USSDMessage message = new USSDMessage(procUnstrReqInd.getMAPDialog());
            message.setMethod(UssdMessageMethod.begin);
            message.setMsisdn(procUnstrReqInd.getMSISDNAddressString().getAddress());

            try {
                message.setMessage(procUnstrReqInd.getUSSDString().getString((Charset)null));
                M3UALink.this.deliverMessage(message);
            } catch (MAPException var4) {
                M3UALink.logger.error("Error", var4);
            }

        }

        public void onProcessUnstructuredSSResponse(ProcessUnstructuredSSResponse procUnstrResInd) {
            if (M3UALink.logger.isDebugEnabled()) {
                M3UALink.logger.debug(String.format("Rx ProcessUnstructuredSSResponseIndication.  USSD String=%s", procUnstrResInd.getUSSDString()));
            }

        }

        public void onUnstructuredSSRequest(UnstructuredSSRequest unstrReqInd) {
            M3UALink.logger.debug("onUnstructuredSSRequest: {}", unstrReqInd);
            USSDMessage message = new USSDMessage(unstrReqInd.getMAPDialog());
            message.setMethod(UssdMessageMethod.continu);
            message.setMsisdn(unstrReqInd.getMSISDNAddressString().getAddress());

            try {
                message.setMessage(unstrReqInd.getUSSDString().getString((Charset)null));
                M3UALink.this.deliverMessage(message);
            } catch (MAPException var4) {
                M3UALink.logger.error("Error", var4);
            }

        }

        public void onUnstructuredSSResponse(UnstructuredSSResponse unstrResInd) {
            M3UALink.logger.error(String.format("onUnstructuredSSResponseIndication for Dialog=%d and invokeId=%d", unstrResInd.getMAPDialog().getLocalDialogId(), unstrResInd.getInvokeId()));
        }

        public void onUnstructuredSSNotifyRequest(UnstructuredSSNotifyRequest unstrNotifyInd) {
            M3UALink.logger.error(String.format("onUnstructuredSSNotifyRequestIndication for Dialog=%d and invokeId=%d", unstrNotifyInd.getMAPDialog().getLocalDialogId(), unstrNotifyInd.getInvokeId()));
        }

        public void onUnstructuredSSNotifyResponseIndication(UnstructuredSSNotifyResponse unstrNotifyInd) {
            M3UALink.logger.error(String.format("onUnstructuredSSNotifyResponseIndication for Dialog=%d and invokeId=%d", unstrNotifyInd.getMAPDialog().getLocalDialogId(), unstrNotifyInd.getInvokeId()));
        }

        public void onUnstructuredSSNotifyResponse(UnstructuredSSNotifyResponse unstrNotifyInd) {
        }

        public void onMAPMessage(MAPMessage mapMessage) {
        }

        public void onRegisterSSRequest(RegisterSSRequest request) {
        }

        public void onRegisterSSResponse(RegisterSSResponse response) {
        }

        public void onEraseSSRequest(EraseSSRequest request) {
        }

        public void onEraseSSResponse(EraseSSResponse response) {
        }

        public void onActivateSSRequest(ActivateSSRequest request) {
        }

        public void onActivateSSResponse(ActivateSSResponse response) {
        }

        public void onDeactivateSSRequest(DeactivateSSRequest request) {
        }

        public void onDeactivateSSResponse(DeactivateSSResponse response) {
        }

        public void onInterrogateSSRequest(InterrogateSSRequest request) {
        }

        public void onInterrogateSSResponse(InterrogateSSResponse response) {
        }

        public void onGetPasswordRequest(GetPasswordRequest request) {
        }

        public void onGetPasswordResponse(GetPasswordResponse response) {
        }

        public void onRegisterPasswordRequest(RegisterPasswordRequest request) {
        }

        public void onRegisterPasswordResponse(RegisterPasswordResponse response) {
        }
    }

    private class DialogListener implements MAPDialogListener {
        private DialogListener() {
        }

        public void onDialogDelimiter(MAPDialog mapDialog) {
            if (M3UALink.logger.isDebugEnabled()) {
                M3UALink.logger.debug(String.format("onDialogDelimiter for DialogId=%d", mapDialog.getLocalDialogId()));
            }

        }

        public void onDialogRequest(MAPDialog mapDialog, AddressString destReference, AddressString origReference, MAPExtensionContainer extensionContainer) {
            if (M3UALink.logger.isDebugEnabled()) {
                M3UALink.logger.debug(String.format("onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s MAPExtensionContainer=%s", mapDialog.getLocalDialogId(), destReference, origReference, extensionContainer));
            }

        }

        public void onDialogRequestEricsson(MAPDialog mapDialog, AddressString destReference, AddressString origReference, AddressString arg3, AddressString arg4) {
            if (M3UALink.logger.isDebugEnabled()) {
                M3UALink.logger.debug(String.format("onDialogRequest for DialogId=%d DestinationReference=%s OriginReference=%s ", mapDialog.getLocalDialogId(), destReference, origReference));
            }

        }

        public void onDialogAccept(MAPDialog mapDialog, MAPExtensionContainer extensionContainer) {
            if (M3UALink.logger.isDebugEnabled()) {
                M3UALink.logger.debug(String.format("onDialogAccept for DialogId=%d MAPExtensionContainer=%s", mapDialog.getLocalDialogId(), extensionContainer));
            }

        }

        public void onDialogReject(MAPDialog mapDialog, MAPRefuseReason refuseReason, ApplicationContextName alternativeApplicationContext, MAPExtensionContainer extensionContainer) {
            M3UALink.logger.error(String.format("onDialogReject for DialogId=%d MAPRefuseReason=%s ApplicationContextName=%s MAPExtensionContainer=%s", mapDialog.getLocalDialogId(), refuseReason, alternativeApplicationContext, extensionContainer));
        }

        public void onDialogUserAbort(MAPDialog mapDialog, MAPUserAbortChoice userReason, MAPExtensionContainer extensionContainer) {
            M3UALink.logger.error(String.format("onDialogUserAbort for DialogId=%d MAPUserAbortChoice=%s MAPExtensionContainer=%s", mapDialog.getLocalDialogId(), userReason, extensionContainer));
        }

        public void onDialogProviderAbort(MAPDialog mapDialog, MAPAbortProviderReason abortProviderReason, MAPAbortSource abortSource, MAPExtensionContainer extensionContainer) {
            M3UALink.logger.error(String.format("onDialogProviderAbort for DialogId=%d MAPAbortProviderReason=%s MAPAbortSource=%s MAPExtensionContainer=%s", mapDialog.getLocalDialogId(), abortProviderReason, abortSource, extensionContainer));
        }

        public void onDialogClose(MAPDialog mapDialog) {
            if (M3UALink.logger.isDebugEnabled()) {
                M3UALink.logger.debug(String.format("DialogClose for Dialog=%d", mapDialog.getLocalDialogId()));
            }

        }

        public void onDialogNotice(MAPDialog mapDialog, MAPNoticeProblemDiagnostic noticeProblemDiagnostic) {
            M3UALink.logger.error(String.format("onDialogNotice for DialogId=%d MAPNoticeProblemDiagnostic=%s ", mapDialog.getLocalDialogId(), noticeProblemDiagnostic));
        }

        public void onDialogRelease(MAPDialog mapDialog) {
            if (M3UALink.logger.isDebugEnabled()) {
                M3UALink.logger.debug(String.format("onDialogResease for DialogId=%d", mapDialog.getLocalDialogId()));
            }

        }

        public void onDialogTimeout(MAPDialog mapDialog) {
            M3UALink.logger.error(String.format("onDialogTimeout for DialogId=%d", mapDialog.getLocalDialogId()));
        }
    }
}
