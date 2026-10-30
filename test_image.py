import os
from huggingface_hub import InferenceClient

client = InferenceClient(
    provider="wavespeed",
    api_key=os.environ["HF_TOKEN"]
)

image = client.text_to_image(
    "A cinematic Minecraft-inspired fantasy landscape with blocky mountains, green forests, a bright square sun, and dramatic lighting",
    model="krea/Krea-2-Turbo"
)

image.save("test-image.png")

print("Image generated successfully.")
print("Saved as test-image.png")