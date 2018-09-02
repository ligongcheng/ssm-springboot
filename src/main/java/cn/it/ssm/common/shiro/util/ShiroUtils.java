package cn.it.ssm.common.shiro.util;

import cn.it.ssm.domain.auto.SysUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class ShiroUtils {

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static SysUser getCurrentUser() {
        return (SysUser) getSubject().getPrincipal();
    }

    public static Session getSession() {
        return getSubject().getSession();
    }

    public static Session getSession(Boolean flag) {
        return getSubject().getSession(flag);
    }
}
