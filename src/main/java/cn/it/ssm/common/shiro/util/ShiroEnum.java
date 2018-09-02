package cn.it.ssm.common.shiro.util;

public enum ShiroEnum {

    SESSION_CACHE("shiro:activeSessionCache:"),
    AUTHENTICATION_CACHE("shiro:authenticationCache:"),
    AUTHENTIZATION_CACHE("shiro:authorizationCache:"),
    KICKOUT_SESSION("shiro:kickout-session:"),
    PASSWORDRETRY_CACHE("shiro:passwordRetryCache:");

    private String cacheName;

    ShiroEnum(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public String toString() {
        return this.cacheName;
    }

}
