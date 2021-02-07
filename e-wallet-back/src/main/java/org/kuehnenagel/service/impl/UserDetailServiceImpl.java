package org.kuehnenagel.service.impl;

import org.kuehnenagel.model.UserEntity;
import org.kuehnenagel.repository.UserRepository;
import org.kuehnenagel.service.UserDetailService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailServiceImpl implements UserDetailService {
    private final UserRepository userRepository;
    
    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByUsername(username);
        return new User(userEntity.getUsername(), userEntity.getPassword(), Collections.emptyList());
    }
}
