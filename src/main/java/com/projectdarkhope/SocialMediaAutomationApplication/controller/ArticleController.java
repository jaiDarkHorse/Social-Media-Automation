package com.projectdarkhope.SocialMediaAutomationApplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.Article;
import com.projectdarkhope.SocialMediaAutomationApplication.service.ArticleService;

@RestController
@RequestMapping("/api/articles")
public class ArticleController
{
	@Autowired
	private ArticleService articleService;
	
	@PostMapping
	public Article createArticle(@RequestBody Article article)
	{
		return articleService.saveArticle(article);
	}
	
	@GetMapping
	public List<Article> getAllArticles()
	{
		return articleService.getAllArticles();
	}
	
	@PostMapping("/process")
	public String processArticles()
	{
		articleService.processUnprocessedArticles();
	    return "Articles processed successfully";
	}
	
	@PostMapping("/{id}/generate-post")
	public String generatePost(@PathVariable Long id)
	{
	    Article article = articleService.getArticleById(id);

	    return articleService.generateAndSavePost(article);

	}
	
}