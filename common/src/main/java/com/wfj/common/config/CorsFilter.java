package com.wfj.common.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wfj
 * @description TODO
 * @date 2020/9/22
 */
public class CorsFilter implements Filter {
    private final static String OPTIONS = "OPTIONS";
    private final static String ORIGIN = "*";

    private final static String ALLOW_HEADERS = "content-type,x-requested-with,Authorization, x-ui-request,lang";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", ORIGIN);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 允许请求方式
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        // 用来指定本次预检请求的有效期，单位为秒，在此期间不用发出另一条预检请求
        response.setHeader("Access-Control-Max-Age", "3600");
        // 请求包含的字段内容，如有多个可用哪个逗号分隔如下
        response.setHeader("Access-Control-Allow-Headers", ALLOW_HEADERS);
        // 浏览器是会先发一次options请求，如果请求通过，则继续发送正式的post请求
        // 配置options的请求返回
        if (OPTIONS.equals(request.getMethod())) {
            response.setStatus(200);
            response.getWriter().write("OPTIONS returns OK");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
