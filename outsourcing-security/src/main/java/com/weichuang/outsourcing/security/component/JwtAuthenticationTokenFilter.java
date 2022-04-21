package com.weichuang.outsourcing.security.component;

import com.weichuang.outsourcing.security.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        logger.info("进入JwtAuthenticationTokenFilter -> doFilterInternal 函数");
        String authHeader = request.getHeader(jwtTokenUtil.getTokenHeader());
        logger.info("从HttpServletRequest中获取的tokenHeader为：{}", authHeader);
        if (authHeader != null && authHeader.startsWith(jwtTokenUtil.getTokenHead())) {
            logger.info("判断Header是我们要求的Bearer 开头");
            // 截取 "Bearer " 子串之后的字符串（这部分字符串为具体的token）
            String authToken = authHeader.substring(jwtTokenUtil.getTokenHead().length());// The part after "Bearer "
            logger.info("Bearer 字符串之后的真正token为：{}", authToken);
            // 使用JwtTokenUtil工具类从token中解析出用户名
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            logger.info("从token中解析出的username为{}", username);
            // 获取当前经过身份验证的主体或身份验证请求令牌。
            // 返回：
            // 如果没有可用的身份验证信息，则为Authentication或null
            /**
             * 下面代码首先判断前端请求携带的token中的用户名是否为空，并且安全认证框架中的主体（也就是要验证的用户）手否存在
             * SecurityContextHolder.getContext().getAuthentication() == null 说明当前请求主体中没有该用户
             * 该用户的信息需要去数据库中查询
             */
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 从数据库中加载用户信息
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                // 验证token
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    // 验证通过，构造UsernamePasswordAuthenticationToken对象
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    logger.info("authenticated user:{}", username);
                    // 将authentication存入安全框架的上下文中
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
