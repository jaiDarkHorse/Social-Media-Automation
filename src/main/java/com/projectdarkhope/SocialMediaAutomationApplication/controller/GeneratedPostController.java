package com.projectdarkhope.SocialMediaAutomationApplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.projectdarkhope.SocialMediaAutomationApplication.entity.GeneratedPost;
import com.projectdarkhope.SocialMediaAutomationApplication.repository.GeneratedPostRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@RestController 
@RequestMapping("/api/posts") 
public class GeneratedPostController 
{
    @Autowired 
    private GeneratedPostRepository generatedPostRepository;

    @GetMapping 
    public List<GeneratedPost> getAllPosts()
    {
        return generatedPostRepository.findAll();
    }

    @PutMapping("/{id}")
    public GeneratedPost updatePost(@PathVariable Long id, @RequestBody GeneratedPost updatedPost) 
    {
        GeneratedPost post = generatedPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setHook(updatedPost.getHook());
        post.setPostText(updatedPost.getPostText());
        post.setHashtags(updatedPost.getHashtags());

        return generatedPostRepository.save(post);
    }

    @GetMapping("/generated")
    public List<GeneratedPost> getGeneratedPosts()

    {
        return generatedPostRepository.findByStatus("GENERATED");
    }

    @PutMapping("/{id}/approve")
    public String approvePost(@PathVariable Long id)
    {
        GeneratedPost post = generatedPostRepository.findById(id)
                            .orElseThrow(()->new RuntimeException("Post not found"));
        post.setStatus("APPROVED");
        generatedPostRepository.save(post);
        return "Post approved successfully";
    }

    @PutMapping("/{id}/reject")
    public String rejectPost(@PathVariable Long id)
    {
        GeneratedPost post = generatedPostRepository.findById(id)
                            .orElseThrow(()->new RuntimeException("Post not found"));
        post.setStatus("REJECTED");
        generatedPostRepository.save(post);
        return "Post rejected successfully";
    }
    
    
}
