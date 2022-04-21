package com.weichuang.outsourcing.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weichuang.outsourcing.common.api.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义返回结果：没有权限访问时
 *
 * 登录后，访问缺失权限的资源会调用（注意：这里是登录成功之后，访问资源的时候没有权限）
 */
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        // 设置响应头
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        // 将Json字符串返回给前端 jackson springboot 中默认的 json 处理器
        response.getWriter().println(new ObjectMapper().writeValueAsString(CommonResult.forbidden(e.getMessage())));
        response.getWriter().flush();
    }
}
