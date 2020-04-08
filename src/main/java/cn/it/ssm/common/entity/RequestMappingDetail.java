package cn.it.ssm.common.entity;

import java.io.Serializable;

public class RequestMappingDetail implements Serializable {

    private String requestUrl;
    private String requestType;
    private String controllerName;
    private String methodName;
    private String methodParmaTypes;

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodParmaTypes() {
        return methodParmaTypes;
    }

    public void setMethodParmaTypes(String methodParmaTypes) {
        this.methodParmaTypes = methodParmaTypes;
    }


}
