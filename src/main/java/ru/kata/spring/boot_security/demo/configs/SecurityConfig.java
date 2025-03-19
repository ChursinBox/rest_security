package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.service.MyUserDetailService;

@Configuration
@EnableWebSecurity

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SuccessHandler successHandler;
    private final MyUserDetailService myUserDetailService;

    @Autowired
    public SecurityConfig(SuccessHandler successHandler, MyUserDetailService myUserDetailService) {
        this.successHandler = successHandler;
        this.myUserDetailService = myUserDetailService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Отключаем CSRF для REST API
                .authorizeRequests()
                .antMatchers("/api/**").authenticated() // Защищаем все API endpoints
                .and()
                .httpBasic();

//        http
//                .authorizeHttpRequests()
//                .antMatchers("/auth/login", "/auth/registration",
//                        "/error").permitAll()
//                .antMatchers("/admin/**", "/api/**").hasRole("ADMIN")
//                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .successHandler(successHandler)
//                .loginPage("/auth/login")
//                .loginProcessingUrl("/process_login")
//                .failureUrl("/auth/login?error")
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/auth/login");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

