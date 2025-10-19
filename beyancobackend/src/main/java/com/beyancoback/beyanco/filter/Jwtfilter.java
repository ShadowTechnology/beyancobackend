package com.beyancoback.beyanco.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.beyancoback.beyanco.security.services.UserDetailsServiceImpl;
import com.beyancoback.beyanco.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class Jwtfilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    
	    String authHeader = request.getHeader("Authorization");
	    String token = null;
	    String username = null;
	    
//	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
//	        token = authHeader.substring(7);
//	        try {
//	            username = jwtService.extractUsername(token);
//	        } catch (Exception e) {
//	            // Token invalid or expired — just continue without setting authentication
//	        }
//	    }

	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        token = authHeader.substring(7);
	        try {
	            username = jwtService.extractUsername(token);
	            System.out.println("✅ Extracted username from token: " + username);
	        } catch (Exception e) {
	            System.out.println("❌ Failed to extract username: " + e.getMessage());
	        }
	    }
	    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        UserDetails userdetails = applicationContext.getBean(UserDetailsServiceImpl.class).loadUserByUsername(username);
	        
	        if (jwtService.validateToken(token, userdetails)) {
	            UsernamePasswordAuthenticationToken authToken =
	                new UsernamePasswordAuthenticationToken(userdetails, null, userdetails.getAuthorities()); // ✅ pass userdetails
	            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	            SecurityContextHolder.getContext().setAuthentication(authToken);
	        }
	    }

	    filterChain.doFilter(request, response);
	}


//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		String authHeader = request.getHeader("Authorization");
//		String token = null;
//		String username = null;
//		if(authHeader != null && authHeader.startsWith("Bearer ")) {
//			token = authHeader.substring(7);
//			username = jwtService.extractUsername(token);
//		}
//		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//			UserDetails userdetails = applicationContext.getBean(UserDetailsServiceImpl.class).loadUserByUsername(username);
//			if(jwtService.validateToken(token,userdetails)) {
//				UsernamePasswordAuthenticationToken authToken =
//						new UsernamePasswordAuthenticationToken(username,null, userdetails.getAuthorities());
//				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//				SecurityContextHolder.getContext().setAuthentication(authToken);
//			}
//		}
//		filterChain.doFilter(request, response);
//	}

}
