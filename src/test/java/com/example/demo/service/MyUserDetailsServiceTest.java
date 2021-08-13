package com.example.demo.service;

import com.example.demo.model.MyUserDetails;
import com.example.demo.repository.MyUserDetailsRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    @Mock
    MyUserDetailsRepository myUserDetailsRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    MyUserDetailsService myUserDetailsService;

    private static MyUserDetails user1;
    private static MyUserDetails user2;
    @BeforeAll
    public static void beforeAll(){
         user1 = new MyUserDetails("user1","user1@gmail.com", "1234");
         user2 = new MyUserDetails("user2","user2@gmail.com", "1234");
    }

    @Test
    public void loadUserByUsername_Pass(){
        //insert roles inside user
        user1.setRoles(new HashSet<>());

        when(myUserDetailsRepository.findByUsername("user1")).thenReturn(Optional.ofNullable(user1));

        UserDetails userDetails = myUserDetailsService.loadUserByUsername("user1");

        assertThat(userDetails).isNotNull().isEqualTo(user1);
    }

    @Test
    public void loadUserByUsername_Fail(){
        when(myUserDetailsRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        //checking if unregistered username throws an exception
        Throwable thrown = catchThrowable(()-> {
            UserDetails userDetails = myUserDetailsService.loadUserByUsername("anyStringXYZ");
        });
        assertThat(thrown).isInstanceOf(UsernameNotFoundException.class).hasMessageContaining("anyStringXYZ");
    }

    @Test
    public void insertUser_Pass(){
        when(myUserDetailsRepository.findByUsername("user1")).thenReturn(Optional.empty());
        when(myUserDetailsRepository.findByEmail("user1@gmail.com")).thenReturn(Optional.empty());
        when(myUserDetailsRepository.insert((MyUserDetails) any())).then(returnsFirstArg());

        when(bCryptPasswordEncoder.encode("1234")).thenReturn("abcd");

        Optional<MyUserDetails> myUserDetailsOptional = myUserDetailsService.insertUser(user1);

        assertTrue(myUserDetailsOptional.isPresent());

        MyUserDetails myUserDetails = myUserDetailsOptional.get();

        assertThat(myUserDetails).isEqualTo(user1);
        assertThat(myUserDetails.getPassword()).isEqualTo("abcd");
    }

}
