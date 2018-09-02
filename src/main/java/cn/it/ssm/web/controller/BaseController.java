package cn.it.ssm.web.controller;

import cn.it.ssm.common.shiro.util.ShiroUtils;
import cn.it.ssm.domain.auto.SysUser;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class BaseController {


    public static Subject getSubject() {
        return ShiroUtils.getSubject();
    }

    protected SysUser getCurrentUser() {
        return ShiroUtils.getCurrentUser();
    }

    protected Session getSession() {
        return getSubject().getSession();
    }

    protected Session getSession(Boolean flag) {
        return getSubject().getSession(flag);
    }

    protected void login(AuthenticationToken token) {
        getSubject().login(token);
    }
}
