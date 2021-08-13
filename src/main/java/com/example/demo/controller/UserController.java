package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.ErrorService;
import com.example.demo.service.MyUserDetailsService;
import com.example.demo.service.RegistrationService;
import com.example.demo.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final MyUserDetailsService myUserDetailsService;
    private final ErrorService errorService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RegistrationService registrationService;

    //authenticate user and return a jwt token
    @PostMapping("/authenticate")
    public ResponseEntity createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {

        try{
            //authenticate username and password
            this.authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }


    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegistrationRequest registrationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = errorService.getErrorMessagesFromBindingResult(bindingResult);
            return ResponseEntity.badRequest().body(errorMessages);
        }

        MyUserDetails myUserDetails = new MyUserDetails(
                registrationRequest.getUsername(),
                registrationRequest.getEmail(),
                registrationRequest.getPassword()
        );

        //get roles from request and verify each role and set role in userdetails object
        Set<String> strRoles = registrationRequest.getRoles();
        Set<Role> roles = registrationService.generateRolesSet(strRoles);
        myUserDetails.setRoles(roles);

        Optional<MyUserDetails> userInsertedOptional = myUserDetailsService.insertUser(myUserDetails);

        if (userInsertedOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email or username already present");
        return ResponseEntity.ok().build();
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
