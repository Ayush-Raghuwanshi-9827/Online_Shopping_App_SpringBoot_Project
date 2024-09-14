package com.ecomerse.Online_Shopping_App.Config;

import com.ecomerse.Online_Shopping_App.repository.UserDtlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImple implements UserDetailsService {

    @Autowired
    private UserDtlRepository userDtlRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userDtlRepository.findByEmail(username);

        if (user == null)
            throw new UsernameNotFoundException("user not found");
        return new CustomUser(user);
    }
}
