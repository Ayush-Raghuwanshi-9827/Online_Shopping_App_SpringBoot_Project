package com.ecomerse.Online_Shopping_App.service;

import com.ecomerse.Online_Shopping_App.model.UserDetails;

public interface UserDetailsService {

    public UserDetails SaveUser(UserDetails userDetails);

    public UserDetails getUserByEmail(String email);

    public void updateUserResetToken(UserDetails userEmail, String resetToken);

    public UserDetails getUserByToken(String token);

    public UserDetails updateUser(UserDetails user);

}
