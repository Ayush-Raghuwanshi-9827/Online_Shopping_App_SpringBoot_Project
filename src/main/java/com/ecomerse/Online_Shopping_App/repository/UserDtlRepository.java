package com.ecomerse.Online_Shopping_App.repository;

import com.ecomerse.Online_Shopping_App.model.UserDetails;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDtlRepository extends JpaRepository<UserDetails, Integer> {
    public UserDetails findByEmail(String email);

    public UserDetails findByResetToken(String token);
}
