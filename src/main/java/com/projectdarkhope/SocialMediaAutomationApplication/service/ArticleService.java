package com.projectdarkhope.SocialMediaAutomationApplication.service;


import java.util.List;
import com.projectdarkhope.SocialMediaAutomationApplication.repository.GeneratedPostRepository;

import com.projectdarkhope.SocialMediaAutomationApplication.ai.GeminiClassification;
import com.projectdarkhope.SocialMediaAutomationApplication.ai.GeminiPost;
import com.projectdarkhope.SocialMediaAutomationApplication.ai.GeminiService;
import com.projectdarkhope.SocialMediaAutomationApplication.ai.GroqService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.Article;
import com.projectdarkhope.SocialMediaAutomationApplication.entity.GeneratedPost;
import com.projectdarkhope.SocialMediaAutomationApplication.repository.ArticleRepository;

@Service
public class ArticleService 
{
	@Value("${ai.provider}")
	private String aiProvider;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private GroqService groqService;
	
	@Autowired
	private GeminiService geminiService;
	
	@Autowired
	private GeneratedPostRepository generatedPostRepository;
	
	public Article saveArticle(Article article)
	{
		if(articleRepository.existsByUrl(article.getUrl()))
		{
			throw new RuntimeException("Article with this URL already exists");
		}
		return articleRepository.save(article);
	}
	
	public List<Article> getAllArticles()
	{
		return articleRepository.findAll();
	}
	
	public void processUnprocessedArticles()
	{
	    List<Article> articles = articleRepository.findByProcessedFalse();

	    int count = 0;

	    for(Article article : articles)
	    {
	        if(count >= 2)
	        {
	            break;
	        }

	        try
	        {
	            GeminiClassification classification;

				if (aiProvider.equalsIgnoreCase("groq"))
				{
				    classification =
				            groqService.classifyArticle(
				                    article.getTitle(),
				                    article.getDescription()
				            );
				}
				else if (aiProvider.equalsIgnoreCase("gemini"))
				{
				    classification =
				            geminiService.classifyArticle(
				                    article.getTitle(),
				                    article.getDescription()
				            );
				}
				else
				{
				    throw new RuntimeException(
				            "Unsupported AI provider: " + aiProvider
				    );
				}

	            article.setCategory(classification.getCategory());
	            article.setSubcategory(classification.getSubcategory());
	            article.setTargetAccount(classification.getTargetAccount());
	            article.setRelevance(classification.getRelevance());
	            article.setConfidence(classification.getConfidence());
	            
				if(classification.getRelevance() >= 80)
				{
				    article.setApproved(true);
				}
				else
				{
				    article.setApproved(false);
				}
	            article.setProcessed(true);

	            articleRepository.save(article);
	            
	            count++;
	            
	            if(article.isApproved())
	            {
	                generateAndSavePost(article);
	            }
	            
	        }
	        catch(Exception e)
	        {
	            System.out.println("Failed to process article: " + article.getTitle());
	            System.out.println(e.getMessage());
	        }
	    }
	}
	
	public String  generateAndSavePost(Article article)
	{
	    if(!article.isApproved())
	    {
	    	return "Article is not approved";
	    }

	    if(generatedPostRepository.existsByArticleId(article.getId()))
	    {
	    	return "Post already exists for this article";
	    }

	    GeminiPost geminiPost;
	    
		if (aiProvider.equalsIgnoreCase("groq"))
		{
		    geminiPost =
		            groqService.generatePost(
		                    article.getTitle(),
		                    article.getDescription(),
		                    article.getCategory()
		            );
		}
		else if (aiProvider.equalsIgnoreCase("gemini"))
		{
		    geminiPost =
		            geminiService.generatePost(
		                    article.getTitle(),
		                    article.getDescription(),
		                    article.getCategory()
		            );
		}
		else
		{
		    throw new RuntimeException(
		            "Unsupported AI provider: " + aiProvider
		    );
		}

	    GeneratedPost generatedPost = new GeneratedPost();

	    generatedPost.setArticle(article);
	    generatedPost.setHook(geminiPost.getHook());
	    generatedPost.setPostText(geminiPost.getPostText());

	    String hashtags = String.join(
	            ",",
	            geminiPost.getHashtags()
	    );

	    generatedPost.setHashtags(hashtags);
	    generatedPost.setStatus("GENERATED");

	    generatedPostRepository.save(generatedPost);
	    
	    return "Post generated successfully";
	}
	
	public Article getArticleById(Long id)
	{
		return articleRepository.findById(id)
				.orElseThrow(()->new RuntimeException("Article not found"));
	}
}