package com.financialapp.financialapp;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser,Integer>{
    
    Optional<ApplicationUser> findByUserId(Integer user_id);

    Optional<ApplicationUser> findByEmail(String email);


}
