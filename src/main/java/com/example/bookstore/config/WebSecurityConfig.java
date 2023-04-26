package com.example.bookstore.config;

import com.example.bookstore.service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomerService customerService;

    public WebSecurityConfig(CustomerService customerService) {
        this.customerService = customerService;
    }
    @Bean
    public CustomerDetailsService customerDetailsService() {
        return new CustomerDetailsService(customerService);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customerDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/**",
                        "/webjars/**", "/css/**", "/js/**", "/images/**" , "/resources/**").permitAll()
                .antMatchers("/user/**").hasAuthority("CustomerRole")
                .antMatchers("/homepage/**", "/index/**","/author/**", "/authors/**", "/book/**", "/books/**", "/customer/**",
                        "/customers/**", "/genre/**", "/genres/**", "/publisher/**", "/publishers/**")
                    .hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.GET, "/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .usernameParameter("userName")
                .defaultSuccessUrl("/findPath")
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll();
    }

    protected Long getId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return customerService.findByUserName(name).getId();
    }
}