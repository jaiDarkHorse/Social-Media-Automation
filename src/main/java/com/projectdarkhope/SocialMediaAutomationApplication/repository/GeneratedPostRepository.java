package com.projectdarkhope.SocialMediaAutomationApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.GeneratedPost;

public interface GeneratedPostRepository extends JpaRepository<GeneratedPost, Long>
{
    boolean existsByArticleId(Long articleId);
}