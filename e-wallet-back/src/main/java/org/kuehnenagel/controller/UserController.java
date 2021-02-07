package org.kuehnenagel.controller;

import org.kuehnenagel.model.dto.UserDto;
import org.kuehnenagel.model.dto.UserOutputDto;
import org.kuehnenagel.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

// CrossOrigin only for dev purposes
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1/users")
@Validated
public class UserController {
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    
    public UserController(JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }
    
    /**
     * Generate valid token, otherwise unauthorized
     * @param userDto input
     * @return response with valid token
     */
    @PostMapping("/authenticate")
    public ResponseEntity<UserOutputDto> generateToken(@Valid @RequestBody final UserDto userDto) {
        try {
            logger.info("Generating token for {}", userDto.getUsername());
            
            UsernamePasswordAuthenticationToken token
                    = new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
            authenticationManager.authenticate(token);
            final String jwsToken = jwtUtil.generateToken(userDto.getUsername());
            UserOutputDto userOutputDto = new UserOutputDto(jwsToken, userDto.getUsername());
            
            logger.info("Generated token successfully for {}", userDto.getUsername());
            return ResponseEntity.ok(userOutputDto);
        } catch (AuthenticationException authEx) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
