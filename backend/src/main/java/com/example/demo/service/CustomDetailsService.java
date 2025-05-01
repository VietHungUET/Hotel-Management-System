package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserAccountRepository;
import com.example.demo.entity.UserAccount;

@Service
public class CustomDetailsService implements UserDetailsService {

    @Autowired
    UserAccountRepository useracRepo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<UserAccount> userac = useracRepo.findByUserName(s);
        userac.orElseThrow(() -> new UsernameNotFoundException("Not found: " + s));
        return userac.map(CustomDetails::new).get();
    }
}
