package com.projectdarkhope.SocialMediaAutomationApplication.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.RssFeed;
import com.projectdarkhope.SocialMediaAutomationApplication.scraper.RssScraperService;
import com.projectdarkhope.SocialMediaAutomationApplication.service.ArticleService;
import com.projectdarkhope.SocialMediaAutomationApplication.service.RssFeedService;

@Component
public class AutomationScheduler 
{
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private RssScraperService rssScraperService;
	
	@Autowired
    private RssFeedService rssFeedService;
	
	// RSS fetching - every 30 minutes
    @Scheduled(fixedRate = 1800000)
    public void fetchRssFeeds() {

        System.out.println("=================================");
        System.out.println("RSS scheduler is running...");
        System.out.println("=================================");

        List<RssFeed> feeds = rssFeedService.getActiveFeeds();

        for(RssFeed feed : feeds)
        {
            System.out.println("Fetching RSS: " + feed.getName());

            rssScraperService.fetchRssFeed(
                    feed.getUrl(),
                    feed.getName()
            );
        }

        System.out.println("RSS fetching finished.");
    }
    
    @Scheduled(fixedRate = 300000)
    public void processArticles() {

        System.out.println("=================================");
        System.out.println("AI processing scheduler is running...");
        System.out.println("=================================");

        articleService.processUnprocessedArticles();

        System.out.println("AI processing finished.");
    } 
}