	package com.app.Ecommerce.config;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.security.authentication.AuthenticationManager;
	import org.springframework.security.config.Customizer;
	import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
	import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
	import org.springframework.security.config.annotation.web.builders.HttpSecurity;
	import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
	import org.springframework.security.config.http.SessionCreationPolicy;
	import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
	import org.springframework.security.crypto.password.PasswordEncoder;
	import org.springframework.security.web.SecurityFilterChain;
	import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
	
	import com.app.Ecommerce.security.JwtFilter;
	
	@Configuration
	@EnableMethodSecurity(prePostEnabled = true)
	public class SecurityConfig {
	
	    @Autowired
	    private JwtFilter jwtFilter;
	
	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        return http
	        		.cors(Customizer.withDefaults())
	                .csrf(AbstractHttpConfigurer::disable)
	                .authorizeHttpRequests(auth -> auth
	                	    .requestMatchers(
	                	    	"/v3/api-docs/**",
	                            "/swagger-ui/**",
	                            "/swagger-ui.html",
	                	        "/auth/register",
	                	        "/auth/login",
	                	        "/index.html",
	                	        "/main.html",
	                	        "/ws/**",          
	                	        "/ws/info/**" ,
	                	        "/ws-stock/**",
	                	        "/api/payment/**",
	                	        "/api/payment/success",
	                	        "/api/payment/cancel",
	                	        "/api/webhook",
	                	        "/api/webhook/**",  
	                	        "/success",
	                	        "/cancel",
	                	        "/"
	                	    ).permitAll()
	                	    .requestMatchers("/buyer/**").hasRole("BUYER")
	                	    .requestMatchers("/seller/**").hasRole("SELLER")
	                	    .requestMatchers("/admin/**").hasRole("ADMIN")
	                	    .anyRequest().authenticated()
	                	)
	                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	                .build();
	    }
	
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	
	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }
	}
