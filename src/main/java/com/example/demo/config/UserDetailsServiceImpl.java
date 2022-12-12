package com.example.demo.config;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    //Here username is provided as email from login
    //username may not be unique
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user=userRepository.findUserByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("could not find user");
        }

        return new CustomUserDetails(user);
    }

}
