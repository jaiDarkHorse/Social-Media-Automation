package com.projectdarkhope.SocialMediaAutomationApplication.controller;

import com.projectdarkhope.SocialMediaAutomationApplication.scraper.RssScraperService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rss")
public class RssController
{
	@Autowired
	private RssScraperService rssScraperService;
	
	@GetMapping("/fetch")
	public String fetchFeed(@RequestParam String url,
							@RequestParam String source)
	{
		rssScraperService.fetchRssFeed(url, source);
		return "RSS feed processed";
	}
	
	
}