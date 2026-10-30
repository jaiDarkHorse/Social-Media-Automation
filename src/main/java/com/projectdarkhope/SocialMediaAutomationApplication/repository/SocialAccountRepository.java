package com.projectdarkhope.SocialMediaAutomationApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.SocialAccount;
import java.util.List;
import java.util.Optional;


@Repository 
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long>
{
    List<SocialAccount> findByActiveTrue();

    List<SocialAccount> findByCategory(String category);

    Optional<SocialAccount> findByAccountName(String accountName);
}
