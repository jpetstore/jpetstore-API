package org.csu.jpetstoreapi.controller;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@Configuration
@WebFilter(filterName = "CORSFilter")
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response =(HttpServletResponse) servletResponse;
        HttpServletRequest request=(HttpServletRequest) servletRequest;

        //"Access-Control-Allow-Origin" : 允许跨域请求   "Origin" : 谁请求就允许谁  "*" : 允许任何
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        //允许携带cookie
        response.setHeader("Access-Control-Allow-Credentials","true");
        //哪些方法的请求是可以的
        response.setHeader("Access-Control-Allow-Methods","POST,GET,PATCH,DELETE,PUT");
        //缓存时间
        response.setHeader("Access-Control-Max-Age","3600");
        response.setHeader("Access-Control-Allow-Headers","Origin,X-Requested-With,Content-Type,Accept");

        filterChain.doFilter(request,response);
    }
}
