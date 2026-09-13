package com.projectdarkhope.SocialMediaAutomationApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.RssFeed;
import com.projectdarkhope.SocialMediaAutomationApplication.service.RssFeedService;

@RestController
@RequestMapping("/api/rss-feeds")
public class RssFeedController
{
	@Autowired
	private RssFeedService rssFeedService;
	
	@PostMapping
	public RssFeed addFeed(@RequestBody RssFeed rssFeed) {
        return rssFeedService.addFeed(rssFeed);
    }

    @GetMapping
    public List<RssFeed> getAllFeeds() {
        return rssFeedService.getAllFeeds();
    }
    
    @PutMapping("/{id}")
    public RssFeed updateFeed(@PathVariable Long id,@RequestBody RssFeed rssFeed)
    {
        return rssFeedService.updateFeed(id, rssFeed);
    }
    
    @DeleteMapping("/{id}")
    public String deleteFeed(@PathVariable Long id)
    {
        rssFeedService.deleteFeed(id);

        return "RSS feed deleted successfully";
    }
}