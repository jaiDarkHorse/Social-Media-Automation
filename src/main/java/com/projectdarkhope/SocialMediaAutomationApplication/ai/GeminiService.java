package com.projectdarkhope.SocialMediaAutomationApplication.ai;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.projectdarkhope.SocialMediaAutomationApplication.ai.GeminiPost;
import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    public String testGemini() {

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key="
                        + apiKey;

        Map<String, Object> request = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of(
                                                "text",
                                                "Say hello and explain what Minecraft is in one sentence."
                                        )
                                }
                        )
                }
        );

        return restClient
                .post()
                .uri(url)
                .body(request)
                .retrieve()
                .body(String.class);
    }
    
    
    public String classifyArticleRaw(String title, String description) {

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key="
                        + apiKey;

        String prompt = """
						You are a strict article classification system for a social media automation platform.
						
						Your task is to classify an article using ONLY its title and description.
						
						SUPPORTED CATEGORIES:
						
						1. minecraft
						2. anime
						3. tamil_nadu_politics
						4. ignore
						
						
						========================
						CATEGORY RULES
						========================
						
						MINECRAFT:
						
						Use "minecraft" when the article is specifically about:
						
						- Minecraft
						- Mojang
						- Minecraft Java Edition
						- Minecraft Bedrock Edition
						- Minecraft updates
						- Minecraft snapshots
						- Minecraft patches
						- Minecraft features
						- Minecraft mobs
						- Minecraft items
						- Minecraft biomes
						- Minecraft gameplay
						- Minecraft servers
						- Minecraft Realms
						- Minecraft Marketplace
						- Minecraft events
						- Minecraft mods
						- Minecraft-related official announcements
						- News specifically concerning the Minecraft community
						
						IMPORTANT:
						If the title or description explicitly mentions "Minecraft" or "Mojang", the category MUST be "minecraft" unless the article is clearly unrelated despite the mention.
						
						
						ANIME:
						
						Use "anime" when the article is specifically about:
						
						- Anime
						- Manga
						- Anime episodes
						- Anime movies
						- Anime seasons
						- Anime announcements
						- Anime releases
						- Anime characters
						- Anime studios
						- Anime adaptations
						- Manga chapters or releases
						- Anime games when the article is specifically focused on an anime franchise
						- Japanese animation industry news
						
						
						TAMIL_NADU_POLITICS:
						
						Use "tamil_nadu_politics" when the article is specifically about:
						
						- Tamil Nadu politics
						- Tamil Nadu political parties
						- Tamil Nadu politicians
						- Tamil Nadu elections
						- Tamil Nadu government political decisions
						- Political leaders in Tamil Nadu
						- Political campaigns in Tamil Nadu
						- Political controversies in Tamil Nadu
						- Tamil Nadu legislative or political developments
						
						
						IGNORE:
						
						Use "ignore" when:
						
						- The article does not meaningfully belong to any supported category.
						- The article is about an unrelated topic.
						- The connection to a supported category is extremely weak.
						- A keyword happens to appear but the article itself is not actually about that category.
						
						Do NOT use "ignore" for an article that is clearly about Minecraft, anime, or Tamil Nadu politics.
						
						
						========================
						SUBCATEGORY RULES
						========================
						
						For MINECRAFT, use an appropriate subcategory such as:
						
						- game_update
						- snapshot
						- feature
						- mob
						- item
						- gameplay
						- server
						- mod
						- marketplace
						- event
						- community
						- announcement
						- other
						
						For ANIME, use an appropriate subcategory such as:
						
						- new_release
						- episode
						- season
						- movie
						- manga
						- character
						- announcement
						- adaptation
						- event
						- other
						
						For TAMIL_NADU_POLITICS, use an appropriate subcategory such as:
						
						- election
						- political_party
						- politician
						- government
						- policy
						- controversy
						- announcement
						- campaign
						- other
						
						For IGNORE:
						
						subcategory MUST be "none".
						
						
						========================
						TARGET ACCOUNT
						========================
						
						If category is "minecraft":
						targetAccount MUST be "minecraft"
						
						If category is "anime":
						targetAccount MUST be "anime"
						
						If category is "tamil_nadu_politics":
						targetAccount MUST be "tamil_nadu_politics"
						
						If category is "ignore":
						targetAccount MUST be "none"
						
						
						========================
						RELEVANCE SCORE
						========================
						
						Give a relevance score from 0 to 100.
						
						90-100:
						The article is directly and strongly about the selected category.
						
						70-89:
						The article is strongly related to the selected category but is not completely focused on it.
						
						40-69:
						The article has a meaningful but indirect connection to the selected category.
						
						1-39:
						The article has only a weak connection to the selected category.
						
						0:
						The article has no meaningful connection to any supported category.
						
						IMPORTANT:
						A clearly category-specific article should normally receive a relevance score of 90-100.
						
						Examples:
						
						"Minecraft 1.21 Update Released"
						→ relevance should be approximately 95-100.
						
						"Mojang Announces New Minecraft Feature"
						→ relevance should be approximately 95-100.
						
						"New Gaming Console Released"
						→ relevance should be low or the category should be "ignore".
						
						
						========================
						CONFIDENCE SCORE
						========================
						
						Give a confidence score from 0 to 100.
						
						90-100:
						The classification is extremely clear from the title and description.
						
						70-89:
						The classification is reasonably clear but has some uncertainty.
						
						40-69:
						There is significant ambiguity.
						
						1-39:
						The classification is highly uncertain.
						
						0:
						There is insufficient information to make a reasonable classification.
						
						IMPORTANT:
						Confidence measures how certain YOU are about the classification.
						It is NOT the same thing as relevance.
						
						For example:
						
						Article:
						"Minecraft 1.21 Update Released"
						
						Classification:
						minecraft
						
						Relevance:
						100
						
						Confidence:
						99-100
						
						The article is both highly relevant and highly certain.
						
						
						========================
						IMPORTANT DECISION RULES
						========================
						
						1. Always consider the TITLE first.
						
						2. Use the DESCRIPTION to confirm or refine the classification.
						
						3. Do not classify an article based only on a single unrelated keyword.
						
						4. If "Minecraft" is explicitly the subject of the article, classify it as "minecraft".
						
						5. If "Mojang" is discussing Minecraft, classify it as "minecraft".
						
						6. If the article is clearly about anime or manga, classify it as "anime".
						
						7. If the article is clearly about politics in Tamil Nadu, classify it as "tamil_nadu_politics".
						
						8. If none of the categories apply, classify it as "ignore".
						
						9. Never invent facts that are not present in the title or description.
						
						10. Return exactly ONE category.
						
						
						========================
						EXAMPLES
						========================
						
						EXAMPLE 1:
						
						Title:
						Minecraft 1.21 Update Released With New Features
						
						Description:
						Mojang has released a new Minecraft update containing several new features and gameplay improvements.
						
						Correct output:
						
						{
						  "category": "minecraft",
						  "subcategory": "game_update",
						  "targetAccount": "minecraft",
						  "relevance": 100,
						  "confidence": 100
						}
						
						
						EXAMPLE 2:
						
						Title:
						Mojang Announces New Minecraft Mob
						
						Description:
						The developers have announced a new mob coming to Minecraft.
						
						Correct output:
						
						{
						  "category": "minecraft",
						  "subcategory": "mob",
						  "targetAccount": "minecraft",
						  "relevance": 100,
						  "confidence": 100
						}
						
						
						EXAMPLE 3:
						
						Title:
						New Anime Season Announced
						
						Description:
						The studio has announced a new season of a popular Japanese anime series.
						
						Correct output:
						
						{
						  "category": "anime",
						  "subcategory": "season",
						  "targetAccount": "anime",
						  "relevance": 95,
						  "confidence": 98
						}
						
						
						EXAMPLE 4:
						
						Title:
						Tamil Nadu Election Campaign Begins
						
						Description:
						Political parties have started their election campaigns across Tamil Nadu.
						
						Correct output:
						
						{
						  "category": "tamil_nadu_politics",
						  "subcategory": "campaign",
						  "targetAccount": "tamil_nadu_politics",
						  "relevance": 100,
						  "confidence": 100
						}
						
						
						EXAMPLE 5:
						
						Title:
						Apple Announces New iPhone
						
						Description:
						Apple has announced its latest iPhone with a faster processor and improved camera.
						
						Correct output:
						
						{
						  "category": "ignore",
						  "subcategory": "none",
						  "targetAccount": "none",
						  "relevance": 0,
						  "confidence": 100
						}
						
						
						EXAMPLE 6:
						
						Title:
						New Block Building Survival Game Announced
						
						Description:
						A new survival game lets players build structures and explore a procedurally generated world.
						
						Correct output:
						
						{
						  "category": "ignore",
						  "subcategory": "none",
						  "targetAccount": "none",
						  "relevance": 0,
						  "confidence": 90
						}
						
						
						========================
						NOW CLASSIFY THIS ARTICLE
						========================
						
						Title:
						%s
						
						Description:
						%s
						
						
						========================
						OUTPUT REQUIREMENTS
						========================
						
						Return ONLY valid JSON.
						
						Do NOT use markdown.
						
						Do NOT use code fences.
						
						Do NOT include explanations.
						
						Do NOT include additional fields.
						
						The JSON MUST contain exactly these five fields:
						
						{
						  "category": "...",
						  "subcategory": "...",
						  "targetAccount": "...",
						  "relevance": 0,
						  "confidence": 0
						}                
                """.formatted(title, description);
        
       /* System.out.println("===== GEMINI PROMPT =====");
        System.out.println(prompt);
        System.out.println("=========================");
        */
        Map<String, Object> request = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of(
                                                "text",
                                                prompt
                                        )
                                }
                        )
                }
        );

        return restClient
                .post()
                .uri(url)
                .body(request)
                .retrieve()
                .body(String.class);
    }
    
    public GeminiClassification classifyArticle(
            String title,
            String description) {

        String response = classifyArticleRaw(title, description);

        try {

            JsonNode root = objectMapper.readTree(response);

            String jsonText = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            return objectMapper.readValue(
                    jsonText,
                    GeminiClassification.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse Gemini classification response",
                    e
            );
        }
    }
    
    public GeminiPost generatePost(String title, String description, String category)
    {
        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key="
                        + apiKey;

        String prompt = """
                You are a social media content creator.

                Create an engaging social media post from the article below.

                CATEGORY:
                %s

                TITLE:
                %s

                DESCRIPTION:
                %s

                REQUIREMENTS:

                1. Write a short, engaging hook.
                2. Write a concise social media post.
                3. Keep the post factual and based ONLY on the provided article.
                4. Do not invent information.
                5. Make the writing natural and engaging.
                6. Do not use clickbait that misrepresents the article.
                7. Generate 3 to 6 relevant hashtags.
                8. Do not include hashtags inside the postText.
                9. Return ONLY valid JSON.
                10. Do not use markdown or code fences.

                REQUIRED JSON FORMAT:

                {
                  "hook": "...",
                  "postText": "...",
                  "hashtags": ["#...", "#...", "#..."]
                }
                """.formatted(category, title, description);

        Map<String, Object> request = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of(
                                                "text",
                                                prompt
                                        )
                                }
                        )
                }
        );

        String response = restClient
                .post()
                .uri(url)
                .body(request)
                .retrieve()
                .body(String.class);

        try
        {
            JsonNode root = objectMapper.readTree(response);

            String jsonText = root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            return objectMapper.readValue(
                    jsonText,
                    GeminiPost.class
            );
        }
        catch(Exception e)
        {
            throw new RuntimeException(
                    "Failed to parse Gemini post response",
                    e
            );
        }
    }
}