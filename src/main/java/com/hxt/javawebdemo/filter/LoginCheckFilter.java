package com.hxt.javawebdemo.filter;

import com.alibaba.fastjson.JSON;
import com.hxt.javawebdemo.common.BaseContext;
import com.hxt.javawebdemo.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheck", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common"
        };

        //2、判断本次请求是否需要处理,3、如果不需要处理，则直接放行
        if(check(requestURI, urls)){
            filterChain.doFilter(request, response);
            return;
        }

        //4、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            Long id = (Long)request.getSession().getAttribute("employee");
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            BaseContext.setCurrentId(id); // 存为本线程的变量，可以在一个线程中（同一个http请求）传递下去
            log.info("此时的线程id: {}", Thread.currentThread());
            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN"))); // 联动前端代码。向respond输出R错误对象即可。前端跳转request.js

    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String requestURI, String[] urls){
        for(String url : urls){
            if(PATH_MATCHER.match(url, requestURI))
                return true;
        }
        return false;
    }
}
