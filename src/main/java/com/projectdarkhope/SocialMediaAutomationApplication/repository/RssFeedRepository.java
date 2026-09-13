package com.projectdarkhope.SocialMediaAutomationApplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectdarkhope.SocialMediaAutomationApplication.entity.RssFeed;

@Repository
public interface RssFeedRepository extends JpaRepository<RssFeed, Long> {

    List<RssFeed> findByActiveTrue();
}