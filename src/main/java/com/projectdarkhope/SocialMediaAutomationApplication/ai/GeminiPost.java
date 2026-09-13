package com.projectdarkhope.SocialMediaAutomationApplication.ai;

import java.util.List;

public class GeminiPost
{
	private String hook;
	private String postText;
	private List<String> hashtags;
	
	
	public String getHook() {
		return hook;
	}
	public void setHook(String hook) {
		this.hook = hook;
	}
	public String getPostText() {
		return postText;
	}
	public void setPostText(String postText) {
		this.postText = postText;
	}
	public List<String> getHashtags() {
		return hashtags;
	}
	public void setHashtags(List<String> hashtags) {
		this.hashtags = hashtags;
	}
	
	
}