package com.yanbao.interceptor;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Created by summer on 2016-12-22:16:51;
 */
public class CrossFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // CORS "pre-flight" request
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        response.addHeader("Access-Control-Max-Age", "1728000");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", " Origin, No-Cache, X-Requested-With,Cache-Control, Expires, Content-Type,token,Content-Disposition,Pragma");
        response.setCharacterEncoding("UTF-8");
        filterChain.doFilter(request, response);
    }
}
