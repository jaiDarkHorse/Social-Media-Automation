package com.projectdarkhope.SocialMediaAutomationApplication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.RssFeed;
import com.projectdarkhope.SocialMediaAutomationApplication.repository.RssFeedRepository;

@Service
public class RssFeedService {

    @Autowired
    private RssFeedRepository rssFeedRepository;

    public RssFeed addFeed(RssFeed rssFeed) {
        return rssFeedRepository.save(rssFeed);
    }

    public List<RssFeed> getAllFeeds() {
        return rssFeedRepository.findAll();
    }

    public List<RssFeed> getActiveFeeds() {
        return rssFeedRepository.findByActiveTrue();
    }
    
    public RssFeed updateFeed(Long id, RssFeed updatedFeed)
    {
        RssFeed existingFeed = rssFeedRepository.findById(id)
                        					.orElseThrow(() ->
                        							new RuntimeException("RSS feed not found"));

        existingFeed.setName(updatedFeed.getName());
        existingFeed.setUrl(updatedFeed.getUrl());
        existingFeed.setActive(updatedFeed.isActive());

        return rssFeedRepository.save(existingFeed);
    }
    
    public void deleteFeed(Long id)
    {
        if(!rssFeedRepository.existsById(id))
        {
            throw new RuntimeException("RSS feed not found");
        }

        rssFeedRepository.deleteById(id);
    }
}