package cn.it.ssm.common.monitor;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * 监控uri调用
 */

public class MonitorFilter implements Filter {

    private ServletContext sc;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        sc = filterConfig.getServletContext();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sc);
        //RequestMappingHandlerMapping bean = wac.getBean(RequestMappingHandlerMapping.class);
        //System.out.println(bean);
        MonitorHelper monitorHelper = wac.getBean(MonitorHelper.class);
        monitorHelper.count(((HttpServletRequest) request).getRequestURI());
        chain.doFilter(request, response);
    }


    @Override
    public void destroy() {

    }
}
