package com.projectdarkhope.SocialMediaAutomationApplication.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "generated_posts")
public class GeneratedPost
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "article_id", nullable = false, unique = true)
    private Article article;

    private String hook;

    @Column(columnDefinition = "TEXT")
    private String postText;

    @Column(columnDefinition = "TEXT")
    private String hashtags;

    private String status;

    public GeneratedPost()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Article getArticle()
    {
        return article;
    }

    public void setArticle(Article article)
    {
        this.article = article;
    }

    public String getHook()
    {
        return hook;
    }

    public void setHook(String hook)
    {
        this.hook = hook;
    }

    public String getPostText()
    {
        return postText;
    }

    public void setPostText(String postText)
    {
        this.postText = postText;
    }

    public String getHashtags()
    {
        return hashtags;
    }

    public void setHashtags(String hashtags)
    {
        this.hashtags = hashtags;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}