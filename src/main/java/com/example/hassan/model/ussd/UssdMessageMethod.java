package com.example.hassan.model.ussd;

public enum UssdMessageMethod {
    begin("ProcessUnstructuredSS-Request"),
    continu("UnstructuresSS-Request"),
    end("ProcessUnstructuredSS-Request");

    private String methodName;

    public String getMethodName(){return this.methodName;}

    private UssdMessageMethod(String methodName){this.methodName = methodName;}

    public String toMenuMakerCommandType(){
        switch (this) {
            case begin:
                return "111";
            case continu:
                return "112";
            case end:
                return "113";
            default:
                return null;
        }
    }


    public static UssdMessageMethod fromMenuMakerCommand(String command){
        byte var2 = -1;
        switch (command.hashCode()){
            case 48657:
                if (command.equals("111")){
                    var2 = 0;
                }
                break;
            case 48658:
                if (command.equals("112")){
                    var2 = 0;
                }
                break;
            case 48659:
                if (command.equals("113")){
                    var2 = 2;
                }
        }

        switch (var2){
            case 0:
                return begin;
            case 1:
                return continu;
            case 2:
                return end;
            default:
                return null;
        }

    }

}