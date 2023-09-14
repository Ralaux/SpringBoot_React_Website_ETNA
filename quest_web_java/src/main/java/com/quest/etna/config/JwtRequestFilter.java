package com.quest.etna.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import static com.quest.etna.model.Constants.*;
import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.model.JwtUserDetails;

import netscape.javascript.JSObject;

import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    JwtUserDetailsService jwtUserDetailsService;
     
    @Autowired
    WebSecurityConfig webSecurityConfig;
     
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
    	// Get token = String "Bearer {token}"
        String header = request.getHeader(HEADER_STRING);
        
        // Check token
    	if (header != null) {
                
            	try {
            		
            	
	            	// Find user from token
	                String username = jwtTokenUtil.getUsernameFromToken(header.substring(7, header.length()));
	                JwtUserDetails jwtUser = jwtUserDetailsService.loadUserByUsername(username);
	 
	                // Create jwt token from user
	                UsernamePasswordAuthenticationToken token = new
	                        UsernamePasswordAuthenticationToken(
	                                jwtUser.getUsername(),
	                                webSecurityConfig.passwordEncoder().encode(jwtUser.getPassword()),
	                                jwtUser.getAuthorities());
	 
	                SecurityContextHolder.getContext().setAuthentication(token);
            	}
            	catch (Exception e) {
		            response.setStatus(HttpStatus.UNAUTHORIZED.value());
		            Map<String, Object> responseBody = new HashMap<>();
		            response.setContentType("application/json");
		            responseBody.put("error", "Unauthorized");
		            responseBody.put("message", e.getMessage());
		            ObjectMapper mapper = new ObjectMapper();
		            response.getWriter().write(mapper.writeValueAsString(responseBody));
		            return;
            	}
            	
            	
            
            }
    	
        // ceci doit rester en dehors du if    
    	filterChain.doFilter(request, response);
    
    }
}

