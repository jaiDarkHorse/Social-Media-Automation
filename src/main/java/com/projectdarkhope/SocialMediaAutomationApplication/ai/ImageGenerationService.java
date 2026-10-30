package com.projectdarkhope.SocialMediaAutomationApplication.ai;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageGenerationService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private static final String CLOUDFLARE_WORKER_URL =
            "https://cloudflare-image-generator.jeyasurya2005.workers.dev";

    public String generateImage(
            String title,
            String description,
            String category
    ) {

        String prompt = buildPrompt(title, description, category);

        try {

            String json = """
                    {
                        "prompt": "%s"
                    }
                    """.formatted(
                    escapeJson(prompt)
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CLOUDFLARE_WORKER_URL))
                    .header("Content-Type", "application/json")
                    .header("Accept", "image/jpeg")
                    .POST(
                            HttpRequest.BodyPublishers.ofString(json)
                    )
                    .build();

            System.out.println("Generating image...");
            System.out.println("Prompt: " + prompt);

            HttpResponse<byte[]> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofByteArray()
            );

            if (response.statusCode() < 200
                    || response.statusCode() >= 300) {

                String error = new String(response.body());

                throw new RuntimeException(
                        "Image generation failed. Status: "
                                + response.statusCode()
                                + " Response: "
                                + error
                );
            }

            Path imageDirectory = Path.of("generated-images");

            Files.createDirectories(imageDirectory);

            String fileName =
                    "image-" + System.currentTimeMillis() + ".jpg";

            Path imagePath =
                    imageDirectory.resolve(fileName);

            Files.write(imagePath, response.body());

            System.out.println(
                    "Image generated successfully: "
                            + imagePath
            );

            return imagePath.toString();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to generate image",
                    e
            );
        }
    }

    private String buildPrompt(
            String title,
            String description,
            String category
    ) {

        return """
                Create a visually striking professional social media news image
            based strictly on the news information below.

            Category:
            %s

            News title:
            %s

            News description:
            %s

            Visual direction:
            - Create a strong, cinematic hero composition.
            - Make the main subject immediately recognizable.
            - Use dramatic but natural lighting.
            - Use depth, atmosphere and visual contrast.
            - Create a polished professional editorial/gaming-news aesthetic.
            - Use a clear foreground, subject and background.
            - Make the image visually interesting even without text.
            - Use dynamic perspective where appropriate.
            - High detail and clean composition.
            - Square 1:1 composition suitable for Instagram and other social platforms.

            Content rules:
            - Represent only information supported by the title and description.
            - Do not invent characters, events, locations or objects that contradict the news.
            - Do not create screenshots of websites.
            - Do not add long text.
            - Avoid generic stock-photo appearance.
            - Avoid bland or empty compositions.
            - Do not put the news title as large text across the image.
            """.formatted(
            category,
            title,
            description
        );
    }

    private String escapeJson(String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}