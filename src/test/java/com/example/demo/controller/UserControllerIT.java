package com.example.demo.controller;

import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.RegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    private static RegistrationRequest registrationRequestValid1;
    private static RegistrationRequest registrationRequestInvalid1;

    private static AuthenticationRequest authenticationRequestInvalid1;
    private static AuthenticationRequest authenticationRequestValid1;

    @BeforeAll
    public static void beforeAll(){
        registrationRequestValid1 = new RegistrationRequest("user1", "user1@gmail.com", "abcd1234", null);
        registrationRequestInvalid1 = new RegistrationRequest("user1", "user1", "abcdabcd", null);

        authenticationRequestValid1 = new AuthenticationRequest("user1","abcd1234");
        authenticationRequestInvalid1 = new AuthenticationRequest("user2","abcd1234");

    }

    @Test
    public void register_pass() throws JSONException {
        //getting a random string for username and email
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace('-', '0');  //removing '-' char from uuid

        registrationRequestValid1.setUsername(uuidStr.toString());
        registrationRequestValid1.setEmail(uuidStr.toString() + "@abc.com");

        //setting the username for log-in test later
        authenticationRequestValid1.setUsername(registrationRequestValid1.getUsername());

        ResponseEntity<List> registrationResponse = restTemplate.postForEntity("/user/register",
                registrationRequestValid1, List.class);

        assertThat(registrationResponse.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void register_fail() throws JSONException {
        ResponseEntity<List> registrationResponse = restTemplate.postForEntity("/user/register",
                registrationRequestInvalid1, List.class);

        assertThat(registrationResponse.getStatusCodeValue()).isBetween(400,404);
        assertThat(registrationResponse.getBody()).hasSizeGreaterThan(1);
    }

    @Test
    public void authentication_pass() throws JSONException {
        ResponseEntity<String> authenticationResponse = restTemplate.postForEntity("/user/authenticate",
                authenticationRequestValid1, String.class);

        assertThat(authenticationResponse.getStatusCodeValue()).isEqualTo(200);
        //checking received jwt size and pattern
        assertThat(authenticationResponse.getBody()).hasSizeGreaterThan(30)
                .containsPattern("[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+");
    }

    @Test
    public void authentication_fail() throws JSONException {
        ResponseEntity authenticationResponse = restTemplate.postForEntity("/user/authenticate",
                authenticationRequestInvalid1, String.class);

        assertThat(authenticationResponse.getStatusCodeValue()).isBetween(400,404);
    }
}
