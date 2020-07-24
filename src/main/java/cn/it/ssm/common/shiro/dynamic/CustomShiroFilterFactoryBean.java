package cn.it.ssm.common.shiro.dynamic;

import cn.it.ssm.sys.service.UserService;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * 自定义shiro过滤器工厂bean
 *
 * @author chengtao
 * @date 2020/07/11 13:06:03
 */
public class CustomShiroFilterFactoryBean extends ShiroFilterFactoryBean {

    private static final Logger log = LoggerFactory.getLogger(CustomShiroFilterFactoryBean.class);

    @Autowired
    private UserService userService;

    public Map<String, String> updateFilter() {

        //filterMap.put("/user/login**", "anon");
        //filterMap.put("/admin/**", "anon");//把 admin 设置成不需要拦截
        //filterMap.put(" /super/**", "authc,roles[super],perms[super:info]");

        Map<String, String> filterMap = userService.findUserFilterMap();

        this.updatePermission(filterMap);

        return super.getFilterChainDefinitionMap();
    }

    /**
     * 动态更新权限拦截规则
     *
     * @param filterMap
     */
    public synchronized void updatePermission(Map<String, String> filterMap) {

        AbstractShiroFilter shiroFilter;
        try {
            shiroFilter = (AbstractShiroFilter) super.getObject();

            // 获取过滤管理器
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                .getFilterChainResolver();
            DefaultFilterChainManager filterManager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            //清空拦截管理器中的存储
            filterManager.getFilterChains().clear();
            /*
            清空拦截工厂中的存储,如果不清空这里,还会把之前的带进去
            ps:如果仅仅是更新的话,可以根据这里的 map 遍历数据修改,重新整理好权限再一起添加
             */
            super.getFilterChainDefinitionMap().clear();

            // 相当于新建的 map, 因为已经清空了
            Map<String, String> chains = super.getFilterChainDefinitionMap();
            //把修改后的 map 放进去
            chains.putAll(filterMap);

            //这个相当于是全量添加
            for (Map.Entry<String, String> entry : filterMap.entrySet()) {
                //要拦截的地址
                String url = entry.getKey().trim().replace(" ", "");
                //地址持有的权限
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                //生成拦截
                filterManager.createChain(url, chainDefinition);
            }
        } catch (Exception e) {
            log.error("updatePermission error,filterMap=" + filterMap, e);
        }
    }

    /**
     * 重写此方法可以自定义 PathMatchingFilterChainResolver
     *
     * @return {@link AbstractShiroFilter}* @throws Exception 异常
     */
    @Override
    protected AbstractShiroFilter createInstance() throws Exception {
        return super.createInstance();
    }

    /**
     * @param filterChainDefinitionMap
     */
    @Override
    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        //再细致的拼装可以自己改
        /*filterChainDefinitionMap.put("/user/login**","anon");
        filterChainDefinitionMap.put("/admin/**","authc,roles[admin],perms[admin:info]");
        filterChainDefinitionMap.put(" /super/**","authc,roles[super],perms[super:info]");*/
        super.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }
}
