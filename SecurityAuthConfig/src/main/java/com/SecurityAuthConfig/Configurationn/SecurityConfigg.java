package com.SecurityAuthConfig.Configurationn;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import com.SecurityAuthConfig.Service.CustomerUserDetailsService;

@Configuration // is used to tell Spring to treat the class as a configuration class 
@EnableWebSecurity //Security feature that is applicable for http request for the app 
// The http request should under go this class so we are enabling WebSecurity.

public class SecurityConfigg{
	 String[] openUrls= {"/api/v1/auth/register", 
     		"/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/api/v1/auth/login",
			 "/api/v1/auth/login/mobile",
            "/api/v1/auth/add-mobile",
	 };	
	 
	 @Autowired
		private CustomerUserDetailsService customerUserDetailsService;
	 
	 @Autowired
		private JwtFilter filter;
	 
	// Creating a bean for PasswordEncoder
	 @Bean
		public PasswordEncoder getEncoder() {
			return new BCryptPasswordEncoder();
		}
	 
	//Here we will configure the permission access of the URL
	@Bean
	public SecurityFilterChain securityConfig(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(
				
				req -> {
				    req.requestMatchers(openUrls).permitAll()
				       .requestMatchers("/api/v1/auth/getWelcome").hasRole("ADMIN")
				       .anyRequest().permitAll();
				}	
				).authenticationProvider(authProvider())
        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.csrf().disable().build();
	}
	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
    
    @Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customerUserDetailsService);
		authProvider.setPasswordEncoder(getEncoder());

		return authProvider;
	}
}


