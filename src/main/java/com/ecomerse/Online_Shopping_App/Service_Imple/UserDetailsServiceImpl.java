package com.ecomerse.Online_Shopping_App.Service_Imple;

import com.ecomerse.Online_Shopping_App.model.UserDetails;
import com.ecomerse.Online_Shopping_App.repository.UserDtlRepository;
import com.ecomerse.Online_Shopping_App.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDtlRepository userDtlRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails SaveUser(UserDetails userDetails) {
        userDetails.setRole("ROLE_USER");
        String encodePassword = passwordEncoder.encode(userDetails.getPassword());
        userDetails.setPassword(encodePassword);
        return userDtlRepository.save(userDetails);
    }

    @Override
    public UserDetails getUserByEmail(String email) {
        return userDtlRepository.findByEmail(email);
    }

    @Override
    public void updateUserResetToken(UserDetails userEmail, String resetToken) {
        var userObj =  userDtlRepository.findByEmail(userEmail.getEmail());
        userObj.setResetToken(resetToken);
        userDtlRepository.save(userObj);
    }

    @Override
    public UserDetails getUserByToken(String token) {
        return userDtlRepository.findByResetToken(token);
    }

    @Override
    public UserDetails updateUser(UserDetails user) {
        return userDtlRepository.save(user);
    }

}
