package cn.it.ssm.common.shiro.session;

import cn.it.ssm.common.shiro.util.ShiroEnum;
import cn.it.ssm.common.shiro.util.ShiroUtils;
import cn.it.ssm.domain.auto.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.io.Serializable;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ShiroSessionListener implements SessionListener {

    private final AtomicInteger sessionCount = new AtomicInteger(0);

    private CacheManager cacheManager;

    public ShiroSessionListener(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
        log.warn("session:{} start", session.getId());
    }

    @Override
    public void onStop(Session session) {
        sessionCount.decrementAndGet();
        log.warn("session:{} stop", session.getId());
        clearKickoutCache(session);
    }

    @Override
    public void onExpiration(Session session) {
        sessionCount.decrementAndGet();
        clearKickoutCache(session);
        log.warn("session:{} expiration", session.getId());
    }

    private void clearKickoutCache(Session session) {
        try {
            SysUser sysUser = (SysUser) session.getAttribute("user");
            Cache<String, Deque<Serializable>> cache = cacheManager.getCache(ShiroEnum.KICKOUT_SESSION.getCacheName());
            Deque<Serializable> deque = null;
            if (cache != null) {
                deque = cache.get(sysUser.getUsername());
                if (deque != null && deque.size() > 0) {
                    deque.remove(session.getId());
                    if (deque.size() == 0) {
                        cache.remove(sysUser.getUsername());
                    } else {
                        cache.put(sysUser.getUsername(), deque);
                    }
                }
            }
        } catch (CacheException e) {
            log.error("clearKickoutCache error:{}", e.getCause());
        }
    }
}
