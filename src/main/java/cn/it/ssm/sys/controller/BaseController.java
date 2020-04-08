package cn.it.ssm.sys.controller;

import cn.it.ssm.common.shiro.util.ShiroUtils;
import cn.it.ssm.sys.domain.auto.SysUser;
import org.activiti.engine.*;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public abstract class BaseController {

    @Autowired
    public CacheManager cacheManager;

    @Resource
    public ProcessEngine processEngine;

    @Resource
    public RuntimeService runtimeService;

    @Resource
    public HistoryService historyService;

    @Resource
    public IdentityService identityService;

    @Resource
    public TaskService taskService;

    @Resource
    public RepositoryService repositoryService;

    public Subject getSubject() {
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

    public void login(AuthenticationToken token) {
        getSubject().login(token);
    }

}
