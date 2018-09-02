package cn.it.ssm.common.monitor;

public enum ApiEnum {
    API_COUNT_PREFIX("api:count:"),
    API_URI("api:uri");

    private String apiValue;

    ApiEnum(String apiValue) {
        this.apiValue = apiValue;
    }

    public String getApiValue() {
        return apiValue;
    }

    public void setApiValue(String apiValue) {
        this.apiValue = apiValue;
    }
}
