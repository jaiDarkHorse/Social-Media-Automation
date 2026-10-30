package com.projectdarkhope.SocialMediaAutomationApplication.scraper;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.Article;
import com.projectdarkhope.SocialMediaAutomationApplication.service.ArticleService;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

                article.setDescription(
                        entry.getDescription() != null
                                ? entry.getDescription().getValue()
                                : null
                );

                article.setSource(source);

                String imageUrl = extractThumbnail(feedUrl, article.getUrl());
                article.setImageUrl(imageUrl);

                System.out.println("IMAGE URL: " + imageUrl);

                if (entry.getPublishedDate() != null) {
                    LocalDateTime publishedAt = entry.getPublishedDate()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();

                    article.setPublishedAt(publishedAt);
                }

                article.setProcessed(false);

                try {
                    articleService.saveArticle(article);
                    System.out.println("Saved: " + article.getTitle());
                } catch (RuntimeException e) {
                    System.out.println("Skipped duplicate: " + article.getTitle());
                }
            }

        } catch (Exception e) {
            System.out.println("RSS scraping failed: " + e.getMessage());
        }
    }

    private String extractThumbnail(String feedUrl, String articleUrl) {
        try {
            URL url = new URL(feedUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept", "application/rss+xml, application/xml, text/xml");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            try (InputStream inputStream = connection.getInputStream()) {

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                // Important: required for media:thumbnail namespace
                factory.setNamespaceAware(true);

                Document document = factory.newDocumentBuilder().parse(inputStream);

                NodeList items = document.getElementsByTagName("item");

                for (int i = 0; i < items.getLength(); i++) {
                    Node itemNode = items.item(i);

                    if (itemNode.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    Element item = (Element) itemNode;

                    NodeList links = item.getElementsByTagName("link");

                    if (links.getLength() == 0) {
                        continue;
                    }

                    String link = links.item(0).getTextContent().trim();

                    if (!link.equals(articleUrl)) {
                        continue;
                    }

                //    System.out.println("Matching article found: " + articleUrl);

                    // First try media:thumbnail
                    NodeList thumbnails = item.getElementsByTagNameNS("*", "thumbnail");

                    if (thumbnails.getLength() > 0) {
                        Element thumbnail = (Element) thumbnails.item(0);

                        String imageUrl = thumbnail.getAttribute("url");

                        if (imageUrl != null && !imageUrl.isBlank()) {
                     //       System.out.println("FOUND THUMBNAIL: " + imageUrl);
                            return imageUrl;
                        }
                    }

                    // Fallback to media:content
                    NodeList mediaContents = item.getElementsByTagNameNS("*", "content");

                    if (mediaContents.getLength() > 0) {
                        for (int j = 0; j < mediaContents.getLength(); j++) {
                            Element mediaContent = (Element) mediaContents.item(j);

                            String imageUrl = mediaContent.getAttribute("url");
                            String mediaType = mediaContent.getAttribute("type");

                            if (imageUrl != null && !imageUrl.isBlank()) {
                                if (mediaType == null || mediaType.isBlank() || mediaType.startsWith("image/")) {
                        //            System.out.println("FOUND MEDIA CONTENT: " + imageUrl);
                                    return imageUrl;
                                }
                            }
                        }
                    }

                    System.out.println("No image found for: " + articleUrl);
                    return null;
                }
            }

        } catch (Exception e) {
            System.out.println("Thumbnail extraction failed: " + e.getMessage());
        }

        return null;
    }
}