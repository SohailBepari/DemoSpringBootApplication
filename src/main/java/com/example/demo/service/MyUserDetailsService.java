package com.example.demo.service;

import com.example.demo.model.MyUserDetails;
import com.example.demo.repository.MyUserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private MyUserDetailsRepository myUserDetailsRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MyUserDetailsService(MyUserDetailsRepository myUserDetailsRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.myUserDetailsRepository = myUserDetailsRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUserDetails myUserDetails =  myUserDetailsRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " username not found"));

        myUserDetails.buildAuthorities();

        return myUserDetails;
    }

    public Optional<MyUserDetails> insertUser(MyUserDetails user){
        boolean sameEmailExists = myUserDetailsRepository.findByEmail(user.getEmail()).isPresent();
        if(sameEmailExists)
            return Optional.empty();

        boolean sameUsernameExists = myUserDetailsRepository.findByUsername(user.getUsername()).isPresent();
        if(sameUsernameExists)
            return Optional.empty();
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return Optional.of(myUserDetailsRepository.insert(user));
    }

}
