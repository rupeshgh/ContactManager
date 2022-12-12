package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class myconfig {



    @Bean
    public UserDetailsService getUserDetailService(){
        return new UserDetailsServiceImpl();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
            DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
            daoAuthenticationProvider.setUserDetailsService(getUserDetailService());
            daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }



    //hasRole() adds ROLE before roles  Resulting = ROLE_role ..
    //hasAnyAuthority doesnt..
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{


        http.authenticationProvider(daoAuthenticationProvider())
          .authorizeHttpRequests()
                  .antMatchers("/admin/**").hasAnyAuthority("admin")
                .antMatchers("/user/**").hasAnyAuthority("user")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/checkUser")
                .defaultSuccessUrl("/user/index")
                .and()
                .csrf().disable();

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer  webSecurityCustomizer(){

        return (web -> web.ignoring().antMatchers("/images/**","/js/**"));
    }


}
