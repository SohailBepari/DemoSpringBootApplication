package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {

    private final RoleRepository roleRepository;

    @Autowired
    public RegistrationService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    //verify each role that is passed in registration request and returns set of verified roles
    public Set<Role> generateRolesSet(Set<String> strRoles){

        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRole("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: User Role not found."));
            roles.add(userRole);
        }
        else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRole("ROLE_ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error:Admin Role not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByRole("ROLE_MOD")
                                .orElseThrow(() -> new RuntimeException("Error: Moderator Role is not found."));
                        roles.add(modRole);
                        break;
                    case "user":
                        Role userRole = roleRepository.findByRole("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException("Error: User Role is not found."));
                        roles.add(userRole);
                        break;
                    default:
                        throw new RuntimeException(role + " role not found");
                }
            });
        }
        return roles;
    }
}
