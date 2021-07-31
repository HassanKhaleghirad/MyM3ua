package com.example.hassan.model.ussd;

public enum UssdMessageMethod {
    begin, continu, end;

    private java.lang.String methodName;

    public java.lang.String getMethodName() { /* compiled code */ }

    private UssdMessageMethod(java.lang.String methodName) { /* compiled code */ }

    public java.lang.String toMenuMakerCommandType() { /* compiled code */ }

    public static com.example.hassan.model.ussd.UssdMessageMethod fromMenuMakerCommand(java.lang.String command) { /* compiled code */ }
}
