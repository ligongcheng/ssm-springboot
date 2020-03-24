package cn.it.ssm.sys.controller;

import cn.it.ssm.common.shiro.util.ShiroUtils;
import cn.it.ssm.sys.domain.auto.SysUser;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    public CacheManager cacheManager;

    public static Subject getSubject() {
        return ShiroUtils.getSubject();
    }


    public SysUser getCurrentUser() {
        return ShiroUtils.getCurrentUser();
    }

    public Session getSession() {
        return getSubject().getSession();
    }

    public Session getSession(Boolean flag) {
        return getSubject().getSession(flag);
    }

    protected void login(AuthenticationToken token) {
        getSubject().login(token);
    }

}
