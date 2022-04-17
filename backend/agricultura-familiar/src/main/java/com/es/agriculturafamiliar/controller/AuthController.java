package com.es.agriculturafamiliar.controller;

import java.net.URI;
import java.util.Set;

import com.es.agriculturafamiliar.constants.RoleType;
import com.es.agriculturafamiliar.dto.SignUpConfirmationDTO;
import com.es.agriculturafamiliar.dto.UserCredentialsDTO;
import com.es.agriculturafamiliar.entity.JwtToken;
import com.es.agriculturafamiliar.entity.Role;
import com.es.agriculturafamiliar.entity.User;
import com.es.agriculturafamiliar.service.TokenAuthenticationService;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class AuthController {
    private final ModelMapper modelMapper;
    private final TokenAuthenticationService tokenAuthenticationService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserCredentialsDTO userCredentialsDTO) {
        User user = modelMapper.map(userCredentialsDTO, User.class);        
        JwtToken jwtToken = tokenAuthenticationService.authenticate(user);
        return ResponseEntity.ok(jwtToken);        
    }

    @PostMapping(value="/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserCredentialsDTO userCredentialsDTO) {        
        User user = modelMapper.map(userCredentialsDTO, User.class);
        user.setRoles(Set.of(Role.builder().role(RoleType.ADMIN).build()));
        tokenAuthenticationService.signUp(user);
        
        return ResponseEntity.created(URI.create("/id")).build();
    }
    
    @PostMapping("/enable-account")
    public ResponseEntity<?> confirmSignUp(@RequestBody SignUpConfirmationDTO signUpConfirmation) {
    	return ResponseEntity.noContent().build();
    }
    
}
