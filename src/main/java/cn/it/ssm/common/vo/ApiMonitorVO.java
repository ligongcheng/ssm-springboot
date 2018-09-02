package cn.it.ssm.common.vo;

import java.io.Serializable;

public class ApiMonitorVO implements Serializable {

    private String apiTime;
    private String apiList;

    public String getApiTime() {
        return apiTime;
    }

    public void setApiTime(String apiTime) {
        this.apiTime = apiTime;
    }

    public String getApiList() {
        return apiList;
    }

    public void setApiList(String apiList) {
        this.apiList = apiList;
    }
}
