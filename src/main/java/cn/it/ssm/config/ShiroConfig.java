package cn.it.ssm.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import cn.it.ssm.common.shiro.cache.ShiroRedisCacheManager;
import cn.it.ssm.common.shiro.filter.ExtAuthenticationFilter;
import cn.it.ssm.common.shiro.filter.KickoutSessionControlFilter;
import cn.it.ssm.common.shiro.realm.RetryLimitHashedCredentialsMatcher;
import cn.it.ssm.common.shiro.realm.UserRealm;
import cn.it.ssm.common.shiro.session.RedisSessionDao;
import cn.it.ssm.common.shiro.session.ShiroSessionListener;
import cn.it.ssm.common.shiro.util.ShiroConst;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件过滤器
     * 1,配置shiro安全管理器接口securityManage;
     * 2,shiro 连接约束配置filterChainDefinitions;
     *
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter() {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        // filterChainDefinitions拦截器=map必须用：LinkedHashMap，因为它必须保证有序
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("kickOut", kickoutSessionControlFilter());
        filterMap.put("extAuthc", new ExtAuthenticationFilter());

        // 配置不会被拦截的链接 从上向下顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/health", "anon");
        filterChainDefinitionMap.put("/actuator", "anon");
        filterChainDefinitionMap.put("/dubbo", "anon");
        filterChainDefinitionMap.put("/assert/**", "anon");
        filterChainDefinitionMap.put("/assets/**", "anon");
        filterChainDefinitionMap.put("/dist/**", "anon");
        filterChainDefinitionMap.put("/includ/**", "anon");
        filterChainDefinitionMap.put("/plugins/**", "anon");
        filterChainDefinitionMap.put("/gifCode", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/sys/login", "anon");
        //filterChainDefinitionMap.put("/kickout.html", "anon");
        filterChainDefinitionMap.put("/job/**", "anon");
        filterChainDefinitionMap.put("/joblog/**", "anon");
        filterChainDefinitionMap.put("/act/**", "anon");
        filterChainDefinitionMap.put("/test/**", "anon");


        filterChainDefinitionMap.put("/logout", "logout");

        //过滤链定义，从上向下顺序执行，一般将/**放在最为下边 :这是一个坑呢，一不小心代码就不好使了;
        //authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/login/**", "anon");
        filterChainDefinitionMap.put("/**", "kickOut,extAuthc");

        // 登录url，如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/");

        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");

        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * ehcache缓存管理器；shiro整合ehcache：
     * 通过安全管理器：securityManager
     *
     * @return
     */
/*    @Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache.xml");
        return ehCacheManager;
    }*/
    @Bean
    @DependsOn("shiroRedisTemplate")
    public ShiroRedisCacheManager shiroRedisCacheManager() {
        RedisTemplate redisTemplate = RedisHolder.getShiroRedisTemplate();
        ShiroRedisCacheManager redisCacheManager = new ShiroRedisCacheManager(redisTemplate);
        redisCacheManager.setGlobalTimeout(36000);
        HashMap<String, Long> map = new HashMap<>();
        //设置各种缓存的有效时间,单位为秒
        map.put(ShiroConst.SESSION_CACHE, 36000L);
        map.put(ShiroConst.AUTHENTICATION_CACHE, 36000L);
        map.put(ShiroConst.AUTHENTIZATION_CACHE, 36000L);
        map.put(ShiroConst.KICKOUT_SESSION, 36000L);
        map.put(ShiroConst.PASSWORDRETRY_CACHE, 360L);
        redisCacheManager.setTimeMap(map);
        return redisCacheManager;
    }

    /**
     * 身份认证realm; (账号密码校验；权限等) 要将realm配置为 @lzay 否则会和dubbo冲突，导致@reference失效
     *
     * @return
     */
    @Bean
    @Lazy
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        //userRealm.setCacheManager(ehCacheManager());
        userRealm.setCredentialsMatcher(credentialsMatcher());
        userRealm.setCachingEnabled(true);
        userRealm.setAuthenticationCachingEnabled(false);
        userRealm.setAuthenticationCacheName(ShiroConst.AUTHENTICATION_CACHE);
        userRealm.setAuthorizationCachingEnabled(true);
        userRealm.setAuthorizationCacheName(ShiroConst.AUTHENTIZATION_CACHE);
        return userRealm;
    }


    /**
     * SecurityManager shiro安全管理器设置realm认证,ehcache缓存管理,sessionManager
     *
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        //securityManager.setCacheManager(ehCacheManager());
        securityManager.setCacheManager(shiroRedisCacheManager());
        securityManager.setSessionManager(sessionManager());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    /**
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码,更改密码生成规则和校验的逻辑一致即可; ）
     *
     * @return
     */
    @Bean
    public RetryLimitHashedCredentialsMatcher credentialsMatcher() {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher
            (shiroRedisCacheManager());
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    /**
     * 自定义cookie中session名称等配置
     *
     * @return sessionIdCookie
     */
    @Bean
    public Cookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(2592000);/*30天:2592000 单位：秒 */
        simpleCookie.setName("sid");
        return simpleCookie;
    }

    /**
     * EnterpriseCacheSessionDAO shiro sessionDao层的实现；
     * 提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * 若securityManager配置了cacheManager 则会替代默认的ConcurrentHashMap
     */
    @Bean
    public SessionDAO sessionDAO() {
        //EnterpriseCacheSessionDAO sessionDao = new EnterpriseCacheSessionDAO();
        //添加ehcache活跃缓存名称（必须和ehcache缓存名称一致）
        RedisSessionDao sessionDao = new RedisSessionDao();
        sessionDao.setActiveSessionsCacheName(ShiroConst.SESSION_CACHE);
        return sessionDao;
    }

    /**
     * sessionManager添加session缓存操作DAO
     *
     * @return
     */
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //ShiroSessionManager sessionManager = new ShiroSessionManager();
        ShiroSessionListener sessionListener = new ShiroSessionListener(shiroRedisCacheManager());
        sessionManager.setSessionListeners(Collections.singleton(sessionListener));
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);// 去掉 url中sessionId后缀
        sessionManager.setSessionDAO(sessionDAO());
        // 全局会话超时时间，单位：毫秒  20m=1200000, 30m=1800000, 60m=3600000 3天：3*24*60*60*1000
        sessionManager.setGlobalSessionTimeout(36000000); //20m
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setSessionValidationInterval(600000);
        return sessionManager;
    }

    /**
     * kickoutSessionFilter同一个用户多处登录限制，
     * 注意在springboot中，不要@bean，否则会将kickoutSessionFilter 注册为filter，导致shiro拦截规则失效
     *
     * @return
     */
    public KickoutSessionControlFilter kickoutSessionControlFilter() {
        KickoutSessionControlFilter filter = new KickoutSessionControlFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的ehcache实现的cacheManager()缓存管理
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        filter.setCacheManager(shiroRedisCacheManager());
        //用于根据会话ID，获取会话进行踢出操作的；
        filter.setSessionManager(sessionManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户顶替前者登录的用户；踢出顺序。
        filter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        filter.setKickoutUrl("/");
        //被踢出后重定向到的地址；
        filter.setSessionDAO(sessionDAO());
        filter.setMaxSession(1);
        return filter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor postProcessor = new LifecycleBeanPostProcessor();
        return postProcessor;
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * Enable Shiro Annotations for Spring-configured beans. Only run after the lifecycleBeanProcessor(保证实现了Shiro内部lifecycle函数的bean执行) has run
     * 不使用注解的话，可以注释掉这两个配置
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        //强制使用cglib代理，防止重复代理和可能引起的代理出错问题
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}
