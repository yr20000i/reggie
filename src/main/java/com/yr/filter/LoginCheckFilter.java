package com.yr.filter;

import com.alibaba.fastjson.JSON;
import com.yr.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 检查用户是否已经完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();//路径比较
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;//向下强转为HttpServletRequest
        HttpServletResponse response1 = (HttpServletResponse) response;

        /**
         * 获取本次请求的URI
         * 判断本次请求是需要放行
         * 如果不需要处理，则直接放行
         * 判断登录状态，如果已登录，则直接放行
         * 如果未登录则返回未登录结果
         */
        String requestURI=request1.getRequestURI();
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",//静态资源都可以看，因为拦截的是请求
                "/front/**"
        };//不需要处理的请求路径

        boolean check = check(urls, requestURI);

        if (check){
            log.info("本次请求{}不需要验证",requestURI);
            chain.doFilter(request1,response1);//放行
            return;//既然放行所以后面就不需要进行了，直接让方法结束
        }

        //怎么判断登录状态？？？从session中获取
        if(request1.getSession().getAttribute("employee")!=null){
            log.info("本次请求{}已登录",requestURI);
            chain.doFilter(request1,response1);//放行
            return;//既然放行所以后面就不需要进行了，直接让方法结束
        }

        //结合前端的JS代码，通过输出流方式向客户端页面响应数据
        log.info("本次请求{}需要拦截",requestURI);
        response1.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，判断是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
