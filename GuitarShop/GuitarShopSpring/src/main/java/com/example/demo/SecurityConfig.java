package com.example.demo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import jakarta.servlet.DispatcherType;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public HttpSessionSecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }
   

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            
            

            .authorizeHttpRequests(authz -> authz
//            		.anyRequest().permitAll()
    			.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE).permitAll()
    			
    			 .requestMatchers(
    				        "/order/redirectToOrders",
    				        "/order/redirectToModifyOrderPage",
    				        "/order/filterOrdersByStatus",
    				        "/order/modifyOrderData",
    				        "/order/deleteOrderData",
    				        "/product/redirectToStorage",
    				        "/product/redirectToModifyProductPage",
    				        "/product/modifyProductData",
    				        "/product/deleteProductData",
    				        "/product/addProduct",
    				        "/admin/**"
    				    ).hasRole("ADMIN")
                
                
                .requestMatchers(
                			"/",
                			"/pages/**",
                			"/prod_img/**",
                			"/img/**",
                			"/login",
                			"/register",
                			"/cart/redirectToCart",
                			"/chat/**",
                			"/cart/**",
                			"/product/**",
                			"/user/**",
                			"/access-denied",
                			"/error",
                			"/css/**", "/js/**", "/public/**").permitAll()

                
                
                

                .anyRequest().authenticated()
            )
            
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/acess/access-denied")
            )
            
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
}
