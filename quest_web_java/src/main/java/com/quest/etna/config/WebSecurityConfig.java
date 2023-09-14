package com.quest.etna.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	@Qualifier("userDetailsService")
	private JwtUserDetailsService userDetailsService;
	
	@Autowired
	@Qualifier("jwtAuthenticationEntryPoint")
    private AuthenticationEntryPoint authEntryPoint;
	
	@Bean
	  public JwtRequestFilter jwtRequestFilter() {
	    return new JwtRequestFilter();
	  }
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		
	    httpSecurity
	            .csrf().disable()
	            // don't create session
	            .cors().and()
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	            .exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
	            .authorizeRequests()
	            .antMatchers("/register").permitAll()
	            .antMatchers("/authenticate").permitAll()
	            .antMatchers("/character").permitAll()
	            .antMatchers("/character/{id}").permitAll()
	            .antMatchers("/comment/{charID}").permitAll()
	            .anyRequest().authenticated();
	    	
	    httpSecurity.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

	    
	    // disable page caching
	    httpSecurity.headers().cacheControl();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
