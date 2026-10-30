package com.projectdarkhope.SocialMediaAutomationApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projectdarkhope.SocialMediaAutomationApplication.ai.ImageGenerationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/api/images")
public class ImageGenerationController 
{
    @Autowired 
    private ImageGenerationService imageGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateImage(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String category) {

        String imagePath = imageGenerationService.generateImage(
                title,
                description,
                category
        );

        return ResponseEntity.ok(imagePath);
    }
    
}
