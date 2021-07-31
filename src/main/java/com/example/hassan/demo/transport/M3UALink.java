package com.example.hassan.demo.transport;



public class M3UALink {
    private final java.lang.String CLIENT_ASSOCIATION_NAME;
    private int DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT;
    private final org.restcomm.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl factory;
    private int SERVER_SPC;
    private int CLIENT_SPC;
    private int NETWORK_INDICATOR;
    private int SSN;
    private int MAX_DIALOGS;
    private org.mobicents.protocols.api.IpChannelType ipChannelType;
    private static org.slf4j.Logger logger;
    private com.example.hassan.demo.transport.M3UALinkConfiguration configuration;
    private org.restcomm.protocols.ss7.tcap.api.TCAPStack tcapStack;
    private org.restcomm.protocols.ss7.map.MAPStackImpl mapStack;
    private org.restcomm.protocols.ss7.map.api.MAPProvider mapProvider;
    private org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl sccpStack;
    private org.restcomm.protocols.ss7.m3ua.impl.M3UAManagementImpl clientM3UAMgmt;
    private org.mobicents.protocols.sctp.netty.NettySctpManagementImpl sctpManagement;
    private final long id;
    private volatile com.example.hassan.model.link.LinkConnectionStatus connectionStatus;
    private com.example.hassan.demo.transport.M3UALinkMessageReceptionInterceptor messageReceptionInterceptor;
    private com.example.hassan.demo.transport.LinkConnectionListener connectionListener;
    private final java.util.concurrent.Executor connectionListenerExecutor;
    private final java.util.concurrent.Executor messageInterceptorExecutor;

    M3UALink(long id, com.example.hassan.demo.transport.M3UALinkConfiguration configuration) { /* compiled code */ }

    public synchronized void connect() { /* compiled code */ }

    protected void initializeStack(org.mobicents.protocols.api.IpChannelType ipChannelType) throws java.lang.Exception { /* compiled code */ }

    private void initSCTP(org.mobicents.protocols.api.IpChannelType ipChannelType) throws java.lang.Exception { /* compiled code */ }

    private void initM3UA() throws java.lang.Exception { /* compiled code */ }

    private void initSCCP() throws java.lang.Exception { /* compiled code */ }

    private void initTCAP() throws java.lang.Exception { /* compiled code */ }

    private void initMAP() throws java.lang.Exception { /* compiled code */ }

    public void reply(com.example.hassan.demo.transport.USSDMessage message) { /* compiled code */ }

    public void terminate() { /* compiled code */ }

    public com.example.hassan.demo.transport.M3UALinkMessageReceptionInterceptor getMessageReceptionInterceptor() { /* compiled code */ }

    public void setMessageReceptionInterceptor(com.example.hassan.demo.transport.M3UALinkMessageReceptionInterceptor messageReceptionInterceptor) { /* compiled code */ }

    public com.example.hassan.demo.transport.LinkConnectionListener getConnectionListener() { /* compiled code */ }

    public void setConnectionListener(com.example.hassan.demo.transport.LinkConnectionListener connectionListener) { /* compiled code */ }

    public com.example.hassan.model.link.LinkConnectionStatus getConnectionStatus() { /* compiled code */ }

    public synchronized void disconnect() { /* compiled code */ }

    public long getId() { /* compiled code */ }

    private void setConnectionStatus(java.lang.String info, com.example.hassan.model.link.LinkConnectionStatus status) { /* compiled code */ }

    private void setConnectionStatus(com.example.hassan.model.link.LinkConnectionStatus status) { /* compiled code */ }

    private synchronized void setConnectionStatus(java.lang.String info) { /* compiled code */ }

    private void deliverMessage(com.example.hassan.demo.transport.USSDMessage message) { /* compiled code */ }

    private class SupplementryListener implements org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener {
        private SupplementryListener() { /* compiled code */ }

        public void onErrorComponent(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, java.lang.Long invokeId, org.restcomm.protocols.ss7.map.api.errors.MAPErrorMessage mapErrorMessage) { /* compiled code */ }

        public void onRejectComponent(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, java.lang.Long invokeId, org.restcomm.protocols.ss7.tcap.asn.comp.Problem problem, boolean isLocalOriginated) { /* compiled code */ }

        public void onInvokeTimeout(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, java.lang.Long invokeId) { /* compiled code */ }

        public void onProcessUnstructuredSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest procUnstrReqInd) { /* compiled code */ }

        public void onProcessUnstructuredSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse procUnstrResInd) { /* compiled code */ }

        public void onUnstructuredSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest unstrReqInd) { /* compiled code */ }

        public void onUnstructuredSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse unstrResInd) { /* compiled code */ }

        public void onUnstructuredSSNotifyRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest unstrNotifyInd) { /* compiled code */ }

        public void onUnstructuredSSNotifyResponseIndication(org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse unstrNotifyInd) { /* compiled code */ }

        public void onUnstructuredSSNotifyResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse unstrNotifyInd) { /* compiled code */ }

        public void onMAPMessage(org.restcomm.protocols.ss7.map.api.MAPMessage mapMessage) { /* compiled code */ }

        public void onRegisterSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSRequest request) { /* compiled code */ }

        public void onRegisterSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSResponse response) { /* compiled code */ }

        public void onEraseSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSRequest request) { /* compiled code */ }

        public void onEraseSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSResponse response) { /* compiled code */ }

        public void onActivateSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSRequest request) { /* compiled code */ }

        public void onActivateSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSResponse response) { /* compiled code */ }

        public void onDeactivateSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSRequest request) { /* compiled code */ }

        public void onDeactivateSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSResponse response) { /* compiled code */ }

        public void onInterrogateSSRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSRequest request) { /* compiled code */ }

        public void onInterrogateSSResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse response) { /* compiled code */ }

        public void onGetPasswordRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordRequest request) { /* compiled code */ }

        public void onGetPasswordResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordResponse response) { /* compiled code */ }

        public void onRegisterPasswordRequest(org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest request) { /* compiled code */ }

        public void onRegisterPasswordResponse(org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordResponse response) { /* compiled code */ }
    }

    private class DialogListener implements org.restcomm.protocols.ss7.map.api.MAPDialogListener {
        private DialogListener() { /* compiled code */ }

        public void onDialogDelimiter(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog) { /* compiled code */ }

        public void onDialogRequest(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.primitives.AddressString destReference, org.restcomm.protocols.ss7.map.api.primitives.AddressString origReference, org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer extensionContainer) { /* compiled code */ }

        public void onDialogRequestEricsson(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.primitives.AddressString destReference, org.restcomm.protocols.ss7.map.api.primitives.AddressString origReference, org.restcomm.protocols.ss7.map.api.primitives.AddressString arg3, org.restcomm.protocols.ss7.map.api.primitives.AddressString arg4) { /* compiled code */ }

        public void onDialogAccept(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer extensionContainer) { /* compiled code */ }

        public void onDialogReject(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPRefuseReason refuseReason, org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName alternativeApplicationContext, org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer extensionContainer) { /* compiled code */ }

        public void onDialogUserAbort(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice userReason, org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer extensionContainer) { /* compiled code */ }

        public void onDialogProviderAbort(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPAbortProviderReason abortProviderReason, org.restcomm.protocols.ss7.map.api.dialog.MAPAbortSource abortSource, org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer extensionContainer) { /* compiled code */ }

        public void onDialogClose(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog) { /* compiled code */ }

        public void onDialogNotice(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog, org.restcomm.protocols.ss7.map.api.dialog.MAPNoticeProblemDiagnostic noticeProblemDiagnostic) { /* compiled code */ }

        public void onDialogRelease(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog) { /* compiled code */ }

        public void onDialogTimeout(org.restcomm.protocols.ss7.map.api.MAPDialog mapDialog) { /* compiled code */ }
    }
}