package cn.it.ssm.common.shiro.filter;


import cn.it.ssm.common.shiro.util.ShiroAjaxUtils;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 拓展AuthenticationFilter，使其支持ajax请求
 */
public class ExtAuthenticationFilter extends AuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(ExtAuthenticationFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * isAccessAllowed为false时会调用的方法
     *
     * @return true 放行，flase 拦截
     **/
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        if (isLoginRequest(request, response)) {
            return true;
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                    "Authentication url [" + getLoginUrl() + "]");
            }
            if (!ShiroAjaxUtils.isAjax(request)) {
                saveRequestAndRedirectToLogin(request, response);
            } else {
                ShiroAjaxUtils.out(response);
            }
            return false;
        }
    }
}
