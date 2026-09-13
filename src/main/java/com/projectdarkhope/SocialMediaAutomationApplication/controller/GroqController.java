package com.projectdarkhope.SocialMediaAutomationApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectdarkhope.SocialMediaAutomationApplication.ai.GeminiClassification;
import com.projectdarkhope.SocialMediaAutomationApplication.ai.GroqService;


@RestController
@RequestMapping("/api/groq")
public class GroqController
{
	@Autowired
	private GroqService groqService;

	@GetMapping("/test")
	public String testGroq() {
	    return groqService.testGroq();
	}
	
	 @GetMapping("/test-classification")
	    public GeminiClassification testClassification() {

	        String title = "New Gaming Laptop Can Run Minecraft at 4K";

	        String description =
	                "A new gaming laptop has been announced with powerful hardware "
	                + "capable of running Minecraft at 4K resolution.";

	        return groqService.classifyArticle(title, description);
	    }
}