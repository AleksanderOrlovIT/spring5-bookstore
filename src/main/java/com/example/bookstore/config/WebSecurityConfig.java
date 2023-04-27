package com.example.bookstore.config;

import com.example.bookstore.service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
                .antMatchers("/homepage/**", "/index/**", "/authors/**","/author/**/new", "/author/**/update",
                        "/author/**/delete","/author/**/book/new","/author/**/book/**/delete",
                        "/author/**/genre/new","/author/**/genre/**/delete",
                        "/books/**", "/book/new", "/book/**/update", "/book/**/delete", "/book/**/author/**",
                        "/book/**/customer/**", "/book/**/customers","/book/**/genre/**","/book/**/publisher/**",
                        "/customer/**/books", "/customer/**/book/**","/customer/**/genres",
                        "/customer/**/genre/**", "/customer/**/show","/customer/new", "/customer/**/update",
                        "/customer/**/delete", "/customers/**",
                        "/genres/**","/genre/**/delete", "/genre/new", "/genre/**/update",
                        "/genre/**/author/new", "/genre/**/author/**/delete", "/genre/**/book/**/delete",
                        "/genre/**/book/**/new",
                        "/publishers/**", "/publisher/new", "/publisher/**/update", "/publisher/**/delete",
                        "/publisher/**/book/**", "/book/**/newImage","/author/**/newImage",
                        "/publisher/**/newImage")
                    .hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                .antMatchers(HttpMethod.GET, "/register").permitAll()
                .antMatchers(HttpMethod.POST, "/customer/**/newImage").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                        .permitAll()
                .usernameParameter("userName")
                .defaultSuccessUrl("/findPath")
                .permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/h2-console/**");
    }
}