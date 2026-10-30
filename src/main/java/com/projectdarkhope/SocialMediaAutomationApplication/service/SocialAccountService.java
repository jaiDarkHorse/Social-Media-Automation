package com.projectdarkhope.SocialMediaAutomationApplication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.Article;
import com.projectdarkhope.SocialMediaAutomationApplication.entity.SocialAccount;
import com.projectdarkhope.SocialMediaAutomationApplication.repository.ArticleRepository;
import com.projectdarkhope.SocialMediaAutomationApplication.repository.SocialAccountRepository;

@Service 
public class SocialAccountService 
{
    @Autowired 
    private SocialAccountRepository socialAccountRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public SocialAccount addAccount(SocialAccount account)
    {
        return socialAccountRepository.save(account);
    }

    public List<SocialAccount> getAllAccounts() {
        return socialAccountRepository.findAll();
    }

    public List<SocialAccount> getActiveAccounts() {
        return socialAccountRepository.findByActiveTrue();
    }

    public List<SocialAccount> getAccountsByCategory(String category) {
        return socialAccountRepository.findByCategory(category);
    }

    public SocialAccount updateAccount(Long id, SocialAccount updatedAccount) {
        SocialAccount account = socialAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Social account not found"));

        account.setAccountName(updatedAccount.getAccountName());
        account.setPlatform(updatedAccount.getPlatform());
        account.setCategory(updatedAccount.getCategory());
        account.setActive(updatedAccount.isActive());

        return socialAccountRepository.save(account);
    }

    public void deleteAccount(Long id) 
    {
        if (!socialAccountRepository.existsById(id)) 
        {
            throw new RuntimeException("Social account not found");
        }

        socialAccountRepository.deleteById(id);
    }

    public SocialAccount getAccountByName(String accountName) {
        return socialAccountRepository.findByAccountName(accountName)
                .orElseThrow(() -> new RuntimeException("Social account not found"));
    }

    public List<Article> getArticlesForAccount(Long id)
    {
        SocialAccount account = socialAccountRepository.findById(id)
                                .orElseThrow(()->new RuntimeException("Social account not found"));
        return articleRepository.findByCategory(account.getCategory());
    }
}
