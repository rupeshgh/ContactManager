package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

public class CustomAuthenticator implements AuthenticationProvider {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String name= authentication.getName();
        String password=authentication.getCredentials().toString();

        System.out.println(name);
        System.out.println(password);


        String encode=bCryptPasswordEncoder.encode(password);
            return new UsernamePasswordAuthenticationToken(name,encode,new ArrayList<>());

//        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
