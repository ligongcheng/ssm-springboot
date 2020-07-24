package cn.it.ssm.common.shiro.filter;

import cn.it.ssm.common.shiro.util.ShiroAjaxUtils;
import cn.it.ssm.common.shiro.util.ShiroConst;
import cn.it.ssm.sys.domain.auto.SysUser;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-2-18
 * <p>Version: 1.0
 */
public class KickoutSessionControlFilter extends AccessControlFilter {

    private String kickoutUrl; //踢出后到的地址
    private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession = 1; //同一个帐号最大会话数 默认1
    private CacheManager cacheManager;
    private SessionDAO sessionDAO;
    private SessionManager sessionManager;
    private Cache<String, LinkedList<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        this.cache = cacheManager.getCache(ShiroConst.KICKOUT_SESSION);
    }

    public void setSessionDAO(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        //如果没有登录也不是记住我的话，直接进行之后的流程
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return true;
        }

        Session session = subject.getSession();
        SysUser sysUser = (SysUser) subject.getPrincipal();
        Serializable sessionId = session.getId();

        //TODO 同步控制
        LinkedList<Serializable> currentSessions = cache.get(sysUser.getUsername());
        if (currentSessions == null) {
            synchronized (this) {
                currentSessions = new LinkedList<>();
            }
        }

        //如果队列里没有此sessionId,放入队列
        if (!currentSessions.contains(sessionId)) {
            currentSessions.push(sessionId);
        }
        AtomicBoolean kick = new AtomicBoolean(false);
        //如果队列里的sessionId数超出最大会话数，开始踢人
        while (currentSessions.size() > maxSession) {
            kick.set(true);
            Serializable kickoutSessionId;
            //如果踢出后登录
            if (kickoutAfter) {
                kickoutSessionId = currentSessions.removeFirst();
                subject.logout();
                //否则踢出前者
            } else {
                kickoutSessionId = currentSessions.removeLast();
            }
            try {
                Session kickoutSession = sessionDAO.readSession(kickoutSessionId);
                sessionDAO.delete(kickoutSession);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        // 保存用户登录session信息
        cache.put(sysUser.getUsername(), currentSessions);
        // 已经踢出用户
        if (kick.get()) {
            if (kickoutAfter) {
                // 不是ajax请求
                if (!ShiroAjaxUtils.isAjax(request)) {
                    saveRequest(request);
                    WebUtils.issueRedirect(request, response, kickoutUrl);
                } else {
                    ShiroAjaxUtils.outAndRedirect(response, kickoutUrl);
                }
            }
            return false;
        }
        return true;
    }
}
