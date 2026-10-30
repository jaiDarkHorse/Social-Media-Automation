package com.projectdarkhope.SocialMediaAutomationApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.Article;
import com.projectdarkhope.SocialMediaAutomationApplication.entity.SocialAccount;
import com.projectdarkhope.SocialMediaAutomationApplication.service.SocialAccountService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController 
@RequestMapping("/api/social-accounts")
public class SocialAccountController 
{

    @Autowired
    private SocialAccountService socialAccountService;

    @PostMapping
    public SocialAccount addAccount(@RequestBody SocialAccount account) {
        return socialAccountService.addAccount(account);
    }

    @GetMapping
    public List<SocialAccount> getAllAccounts() {
        return socialAccountService.getAllAccounts();
    }   
    
    @GetMapping("/active")
    public List<SocialAccount> getActiveAccounts() {
        return socialAccountService.getActiveAccounts();
    }

    @GetMapping("/category/{category}")
    public List<SocialAccount> getAccountsByCategory(@PathVariable String category) {
        return socialAccountService.getAccountsByCategory(category);
    }
    
    @PutMapping("/{id}")
    public SocialAccount updateAccount(@PathVariable Long id, @RequestBody SocialAccount account) {
        return socialAccountService.updateAccount(id, account);
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id) {
        socialAccountService.deleteAccount(id);
        return "Social account deleted successfully";
    }

    @GetMapping("/name/{accountName}")
    public SocialAccount getAccountByName(@PathVariable String accountName) {
        return socialAccountService.getAccountByName(accountName);
    }

    @GetMapping("/{id}/articles")
    public List<Article> getArticlesForAccount(@PathVariable Long id)
    {
        return socialAccountService.getArticlesForAccount(id);
    }
    
}
