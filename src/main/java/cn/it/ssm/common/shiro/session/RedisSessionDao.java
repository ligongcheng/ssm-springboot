package cn.it.ssm.common.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class RedisSessionDao extends EnterpriseCacheSessionDAO {

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        Session reqSession = reqSession(sessionId);
        if (reqSession != null) {
            return reqSession;
        }

        Session session = super.readSession(sessionId);
        getRequest().setAttribute(sessionId.toString(), session);

        return session;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        super.update(session);
        Session reqSession = reqSession(session.getId());
        if (reqSession != null) {
            // 清除旧的session
            reqSession.removeAttribute(session.getId().toString());
        }
    }

    public Session reqSession(Serializable sessionId) {
        HttpServletRequest request = getRequest();
        return (Session) request.getAttribute(sessionId.toString());
    }

    @NotNull
    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
