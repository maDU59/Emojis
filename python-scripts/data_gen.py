import json

# Load your original JSON
with open("git-ignored-assets/data/data-by-emoji.json", "r", encoding="utf-8") as f:
    data = json.load(f)

# Build the new JSON
slug_to_emoji = {f":{v['slug']}:": k for k, v in data.items() if len(k) == 1}

# Load the custom JSON
with open("git-ignored-assets/data/custom-emojis.json", "r", encoding="utf-8") as f:
    data = json.load(f)

# Build the new JSON
slug_to_emoji.update({f":{v['slug']}:": k for k, v in data.items()})

# Save it
with open("git-ignored-assets/data/generated_data.json", "w", encoding="utf-8") as f:
    json.dump(slug_to_emoji, f, ensure_ascii=False, indent=2)