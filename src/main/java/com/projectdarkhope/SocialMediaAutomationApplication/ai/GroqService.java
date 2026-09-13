package com.projectdarkhope.SocialMediaAutomationApplication.ai;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GroqService {

    private final RestClient restClient = RestClient.create();

    private final ObjectMapper objectMapper = new ObjectMapper();


    public String testGroq() {

        String apiKey = System.getenv("GROQ_API_KEY");

        String requestBody = """
                {
                  "model": "openai/gpt-oss-120b",
                  "messages": [
                    {
                      "role": "user",
                      "content": "Say hello and tell me that you are working."
                    }
                  ]
                }
                """;

        return restClient.post()
                .uri("https://api.groq.com/openai/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(String.class);
    }


    public GeminiClassification classifyArticle(
            String title,
            String description) {

        String apiKey = System.getenv("GROQ_API_KEY");

        String prompt = """
                You are an article classification system for a social media automation platform.

                Classify the article into exactly one of these categories:

                1. minecraft
                2. anime
                3. tamil_nadu_politics
                4. ignore

                Rules:

                - Use "minecraft" only when the article is specifically about Minecraft,
                  Mojang, Minecraft Java Edition, Minecraft Bedrock Edition, Minecraft updates,
                  snapshots, features, mobs, items, servers, or other directly related Minecraft content.

                - Use "anime" when the article is specifically about anime or manga.

                - Use "tamil_nadu_politics" when the article is specifically about politics
                  in Tamil Nadu.

                - Use "ignore" when the article is unrelated to these topics or only
                  mentions one of them incidentally.
                
                - Return ONLY valid JSON. Do not include markdown or explanations.

                Important:
                An article should NOT be classified as Minecraft merely because Minecraft
                is mentioned in the title or description.

                Example:
                "New Gaming Laptop Can Run Minecraft at 4K"
                should be classified as "ignore" because the article is about a laptop,
                not Minecraft.

                Relevance:
                Give a score from 0 to 100 representing how relevant the article is
                to one of the supported categories.

                Confidence:
                Give a score from 0 to 100 representing how confident you are in
                the classification.

                Subcategory:

                For Minecraft, use:
                game_update, snapshot, hotfix, feature, mob, item, event, news, guide, other

                For anime, use:
                anime_news, manga, release, episode, movie, character, event, other

                For tamil_nadu_politics, use:
                election, government, politician, policy, party, controversy, other

                For ignore:
                subcategory = "none"

                Target account:

                minecraft → "minecraft"
                anime → "anime"
                tamil_nadu_politics → "tamil_nadu_politics"
                ignore → "none"

                Return exactly these fields:

                {
                  "category": "...",
                  "subcategory": "...",
                  "targetAccount": "...",
                  "relevance": 0,
                  "confidence": 0
                }

                Article title:
                %s

                Article description:
                %s
                """.formatted(
                        title,
                        description == null ? "" : description
                );


        String requestBody;

        try {

            var requestJson = objectMapper.createObjectNode();

            requestJson.put("model", "openai/gpt-oss-120b");

            var messages = requestJson.putArray("messages");

            var message = messages.addObject();

            message.put("role", "user");
            message.put("content", prompt);

            requestJson.put("temperature", 0);

            var responseFormat =
                    requestJson.putObject("response_format");

            responseFormat.put("type", "json_object");

            requestBody = objectMapper.writeValueAsString(requestJson);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to create Groq request",
                    e
            );
        }


        String response = restClient.post()
                .uri("https://api.groq.com/openai/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(String.class);


        try {

            JsonNode root =
                    objectMapper.readTree(response);

            String jsonText =
                    root.path("choices")
                            .get(0)
                            .path("message")
                            .path("content")
                            .asText();

            return objectMapper.readValue(
                    jsonText,
                    GeminiClassification.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse Groq classification response: "
                    + response,
                    e
            );
        }
    }
    
    public GeminiPost generatePost(
            String title,
            String description,
            String category) {

        String apiKey = System.getenv("GROQ_API_KEY");

        String prompt = """
                 You are a professional social media content writer for a gaming news account.

        Transform the article below into a catchy, natural, informative social media post.

        Category:
        %s

        Article title:
        %s

        Article description:
        %s


        HOOK:

        Create a short, catchy hook that makes the reader want to continue reading.

        Hook requirements:
        - Around 8 to 15 words.
        - Natural and human-sounding.
        - Create curiosity or excitement without exaggerating.
        - Do NOT use emojis in the hook.
        - Do NOT use words such as "BREAKING", "URGENT", or excessive clickbait.
        - Do not simply repeat the article title.


        POST:

        Write the main post in a concise gaming-news style.

        Post requirements:
        - Approximately 60 to 100 words.
        - Usually 1 to 2 paragraphs.
        - Give the reader the important details from the article.
        - Make the content interesting and easy to read.
        - Sound like a gaming community/news account, not a formal newspaper.
        - Use natural and moderate excitement.
        - You may use emojis inside the post when they fit naturally,
          but do not overuse them.
        - Do not force an engagement question at the end.
        - Do not repeat the same sentence or information.
        - Do not simply copy the article description.
        - Preserve important names, numbers, dates, updates, features,
          and other factual details from the source.


        ACCURACY:

        - Use ONLY information supported by the article.
        - Do not invent facts, features, statistics, quotes, dates,
          characters, gameplay mechanics, or events.
        - You may rewrite and restructure information to make it more engaging.
        - Do not exaggerate facts beyond what the article supports.


        HASHTAGS:

        Generate exactly 4 to 5 relevant hashtags.

        Use a mixture of:
        - the main topic/category
        - the specific subject of the article
        - relevant gaming/community tags

        For Minecraft content, use specific hashtags when appropriate,
        such as:

        #Minecraft
        #MinecraftJava
        #MinecraftBedrock
        #MinecraftUpdate
        #MinecraftNews
        #MinecraftSnapshot
        #MinecraftCommunity
        #Gaming

        Do not use irrelevant hashtags simply to increase the count.


        JSON:

        Return ONLY valid JSON.

        Return exactly these fields:

        {
          "hook": "...",
          "postText": "...",
          "hashtags": ["...", "...", "...", "..."]
        }

        The "hook" must contain only the hook.

        The "postText" must contain only the main social media post.

        The "hashtags" array must contain exactly 4 to 5 hashtags.

        Do not include markdown.
        Do not include explanations outside the JSON.
                """.formatted(
                        category,
                        title,
                        description == null ? "" : description
                );

        String requestBody;

        try {

            var requestJson = objectMapper.createObjectNode();

            requestJson.put(
                    "model",
                    "openai/gpt-oss-120b"
            );

            var messages = requestJson.putArray("messages");

            var message = messages.addObject();

            message.put("role", "user");
            message.put("content", prompt);

            requestJson.put("temperature", 0.7);

            var responseFormat =
                    requestJson.putObject("response_format");

            responseFormat.put(
                    "type",
                    "json_object"
            );

            requestBody =
                    objectMapper.writeValueAsString(requestJson);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to create Groq post request",
                    e
            );
        }

        String response = restClient.post()
                .uri("https://api.groq.com/openai/v1/chat/completions")
                .header(
                        "Authorization",
                        "Bearer " + apiKey
                )
                .header(
                        "Content-Type",
                        "application/json"
                )
                .body(requestBody)
                .retrieve()
                .body(String.class);

        try {

            JsonNode root =
                    objectMapper.readTree(response);

            String jsonText =
                    root.path("choices")
                            .get(0)
                            .path("message")
                            .path("content")
                            .asText();

            return objectMapper.readValue(
                    jsonText,
                    GeminiPost.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse Groq post response: "
                            + response,
                    e
            );
        }
    }
}