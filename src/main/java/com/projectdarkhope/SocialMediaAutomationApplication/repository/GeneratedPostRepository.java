package com.projectdarkhope.SocialMediaAutomationApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.GeneratedPost;
import java.util.List;


public interface GeneratedPostRepository extends JpaRepository<GeneratedPost, Long>
{
    boolean existsByArticleId(Long articleId);

    List<GeneratedPost> findByStatus(String status);
}