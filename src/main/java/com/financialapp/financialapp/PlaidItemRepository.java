package com.financialapp.financialapp;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlaidItemRepository extends JpaRepository<PlaidItem, Long> {
    List<PlaidItem> findByUser(ApplicationUser user);

    Optional<PlaidItem> findByUserAndAccessToken(ApplicationUser applicationUser, String accessToken);
}