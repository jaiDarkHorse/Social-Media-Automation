package com.projectdarkhope.SocialMediaAutomationApplication.scraper;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.Article;
import com.projectdarkhope.SocialMediaAutomationApplication.service.ArticleService;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class RssScraperService {

    @Autowired
    private ArticleService articleService;

    public void fetchRssFeed(String feedUrl, String source) {

        try {

            URL url = new URL(feedUrl);

            SyndFeedInput input = new SyndFeedInput();

            SyndFeed feed = input.build(new XmlReader(url));

            for (SyndEntry entry : feed.getEntries()) {

                Article article = new Article();

                article.setTitle(entry.getTitle());

                article.setUrl(entry.getLink());

                article.setDescription(entry.getDescription() != null
                        ? entry.getDescription().getValue()
                        : null);

                article.setSource(source);

                if (entry.getPublishedDate() != null) {

                    LocalDateTime publishedAt =
                            entry.getPublishedDate()
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime();

                    article.setPublishedAt(publishedAt);
                }

                article.setProcessed(false);

                try {

                    articleService.saveArticle(article);

                    System.out.println(
                            "Saved: " + article.getTitle()
                    );

                } catch (RuntimeException e) {

                    System.out.println(
                            "Skipped duplicate: " + article.getTitle()
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "RSS scraping failed: " + e.getMessage()
            );
        }
    }
}