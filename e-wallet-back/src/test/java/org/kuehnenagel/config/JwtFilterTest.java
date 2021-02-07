package org.kuehnenagel.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kuehnenagel.service.UserDetailService;
import org.kuehnenagel.util.JwtUtil;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {
    @Mock
    private JwtUtil jwtUtil;
    
    @Mock
    private UserDetailService userDetailService;
    
    @Mock
    private HttpServletRequest httpServletRequest;
    
    @Mock
    private HttpServletResponse httpServletResponse;
    
    @Mock
    private FilterChain filterChain;
    
    @Mock
    private UserDetails userDetails;
    
    private JwtFilter jwtFilter;
    
    @BeforeEach
    public void setUp() {
        jwtFilter = new JwtFilter(jwtUtil, userDetailService);
    }
    
    @Test
    public void doFilterInternal_LacksHeader_NoAuth() throws Exception {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("invalid");
        jwtFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
        
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
        
        verify(jwtUtil, never()).getUsername(any());
        verify(userDetailService, never()).loadUserByUsername(any());
    }
    
    @Test
    public void doFilterInternal_ProperHeader_Authenticated() throws Exception {
        final String validToken = "valid_token";
        final String username = "test";
        
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(userDetailService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtUtil.getUsername(validToken)).thenReturn(username);
        jwtFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
    
        verify(userDetailService).loadUserByUsername(username);
        verify(jwtUtil).getUsername(validToken);
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
    }
}
