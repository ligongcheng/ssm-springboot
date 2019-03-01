package cn.it.ssm.service.manager.impl;

import cn.it.ssm.common.vo.OnlineUserVO;
import cn.it.ssm.domain.auto.SysUser;
import cn.it.ssm.service.manager.ISessionService;
import cn.it.ssm.web.ExceptionHandler.AppException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SessionService implements ISessionService {

    @Autowired
    SessionDAO sessionDAO;

    @Override
    public List<OnlineUserVO> getOnlineUser(){
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();

        ArrayList<OnlineUserVO> onlineUserVOS = new ArrayList<>();
        for (Session session : activeSessions) {
            SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
            if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                continue;
            }
            principalCollection = (SimplePrincipalCollection) session
                    .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            SysUser sysUser = (SysUser) principalCollection.getPrimaryPrincipal();
            OnlineUserVO onlineUserVO = new OnlineUserVO();
            onlineUserVO.setId(session.getId().toString());
            onlineUserVO.setIp(session.getHost());
            if (session.getAttribute("kickout") != null) {
                onlineUserVO.setState(0);
            } else {
                onlineUserVO.setState(1);
            }
            onlineUserVO.setPrincipal(sysUser.getUsername());
            onlineUserVO.setDate(session.getLastAccessTime());
            onlineUserVO.setLoginTime(session.getStartTimestamp());
            onlineUserVOS.add(onlineUserVO);
        }
        return onlineUserVOS;
    }

    @Override
    public void kickOut(String id){
        if (SecurityUtils.getSubject().getSession().getId().equals(id)) {
            throw new AppException("不能下线自己的账号");
        }
        Session session = null;
        try {
            session = sessionDAO.readSession(id);
            session.setAttribute("kickout", true);
            sessionDAO.update(session);
        } catch (Exception e) {
        }//ignore error
    }
}
