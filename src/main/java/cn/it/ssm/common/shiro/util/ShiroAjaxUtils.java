package cn.it.ssm.common.shiro.util;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShiroAjaxUtils {

    final static Class<? extends ShiroAjaxUtils> CLAZZ = ShiroAjaxUtils.class;
    //登录页面
    static final String LOGIN_URL = "/login.html";
    //没有授权提醒
    static final String UNAUTHORIZED = "/login.html";

    /**
     * 是否是Ajax请求,如果是ajax请求响应头会有，x-requested-with
     *
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
    }

    /**
     * response 设置超时
     *
     * @param servletResponse
     */
    public static void out(ServletResponse servletResponse) {
        outAndRedirect(servletResponse, null);
    }

    /**
     * 设置超时以及redirectUrl
     *
     * @param servletResponse
     * @param redirectUrl
     */
    public static void outAndRedirect(ServletResponse servletResponse, String redirectUrl) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("UTF-8");
        //在响应头设置session状态
        response.setHeader("session-status", "timeout");
        //在响应头设置redirectUrl
        if (redirectUrl != null) {
            response.setHeader("redirectUrl", redirectUrl);
        }
    }
}
