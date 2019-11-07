package org.gfuzan.common.security.service;

import org.gfuzan.common.security.entity.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 
 * 用户详情服务
 * 
 * @author gfuzan
 */
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

}
