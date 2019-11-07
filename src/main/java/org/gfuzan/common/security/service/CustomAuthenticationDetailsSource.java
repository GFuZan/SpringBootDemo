package org.gfuzan.common.security.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationDetailsSource;

/**
 * 
 * 认证失败处理
 * 
 * @author gfuzan
 */
public class CustomAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, CustomWebAuthenticationDetails> {

    @Override
    public CustomWebAuthenticationDetails buildDetails(HttpServletRequest context) {
        

        
        return null;
    }

}
