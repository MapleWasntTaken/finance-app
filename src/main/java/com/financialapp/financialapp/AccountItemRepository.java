package com.financialapp.financialapp;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountItemRepository extends JpaRepository<AccountItem, Long> {

    // 🔍 Find all accounts for a given PlaidItem
    List<AccountItem> findByPlaidItem(PlaidItem plaidItem);

    // 🔍 Find one account by its Plaid account ID
    Optional<AccountItem> findByAccountId(String accountId);

    Optional<AccountItem> findByPlaidItemAndAccountId(PlaidItem plaidItem, String accountId);

    // 🔍 Find all accounts for a given User (through the PlaidItem relationship)
    @Query("SELECT a FROM AccountItem a WHERE a.plaidItem.user = :user")
    List<AccountItem> findAllByUser(@Param("user") ApplicationUser user);

    // ❌ Delete all accounts under a specific PlaidItem (e.g. when unlinking)
    void deleteByPlaidItem(PlaidItem plaidItem);

    // ✅ Check if an account already exists (for sync deduping)
    boolean existsByAccountId(String accountId);
}

