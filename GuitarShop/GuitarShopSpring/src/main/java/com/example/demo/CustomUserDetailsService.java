package com.example.demo;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.user.UserRepo;

import jakarta.transaction.Transactional;
import model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepo userRepo;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));


        Set<GrantedAuthority> authorities = new HashSet<>();
        
        System.out.println(">>> loadUserByUsername: username=" + username + ", role from DB=" + user.getType());
        
        if (user.getType() != null && !user.getType().isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getType().toUpperCase()));
            System.out.println(user.getType());
        }
        
        System.out.println(">>> Authorities: " + authorities);
        
        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                authorities
        );
    }
}
