import json, math, os, shutil
from PIL import Image

# === CONFIG ===
EMOJI_JSON_PATH = "../src/main/resources/assets/emojis/emojis.json"
IMAGES_DIR = "git-ignored-assets/72x72"
OUTPUT_DIR = "../run/resourcepacks/twemojipack"
FONT_DIR = os.path.join(OUTPUT_DIR, "assets/minecraft/font")
TEXTURE_FONT_DIR = os.path.join(OUTPUT_DIR, "assets/minecraft/textures/font")
TEXTURE_DIR = os.path.join(OUTPUT_DIR, "assets/emojis/textures/emoji")
PACK_NAME = "Twemoji Font Pack"
PACK_FORMAT = 34
SHEET_SIZE = 1008          # size of texture sheet (e.g. 1024x1024)
CELL_SIZE = 72             # each emoji is 72x72
JSON_HEIGHT = 8          # Minecraft font "height"
JSON_ASCENT = 8            # Minecraft font "ascent"

def emoji_to_codepoints(emoji: str) -> str:
    """Convert emoji characters into Twemoji-style codepoint filenames."""
    if len(emoji) > 1: return emoji
    return "-".join(f"{ord(c):x}" for c in emoji if not c.isspace())

def extract_emojis(emojis: list[str]) -> list[str]:
    """Get rid of all the ASCII art emojis"""
    return [e for e in emojis if len(e)==1]

# === PREP ===
os.makedirs(FONT_DIR, exist_ok=True)
os.makedirs(TEXTURE_FONT_DIR, exist_ok=True)

with open(EMOJI_JSON_PATH, "r", encoding="utf-8") as f:
    emojis = json.load(f)

# emojis = {":smile:": "😄", ":sad:": "😢", ...}
emoji_list = extract_emojis(list(emojis.values()))
num_per_row = SHEET_SIZE // CELL_SIZE
max_per_sheet = num_per_row ** 2

sheets = math.ceil(len(emoji_list) / max_per_sheet)
print(f"Generating {sheets} sheet(s)...")

font_providers = []
sheet_index = -1
x = y = 0

for emoji in emoji_list:

    if y == 0 and x == 0:
        sheet_index += 1
        sheet = Image.new("RGBA", (SHEET_SIZE, SHEET_SIZE), (0, 0, 0, 0))
        chars = ""
        x = y = 0
    
    name = emoji.encode("unicode_escape").decode("utf-8")
    # Try to find image matching name (by shortcode or codepoint)
    # Simplest: use shortcode key (first match)
    for key, val in emojis.items():
        if val == emoji:
            img_path = os.path.join(IMAGES_DIR, emoji_to_codepoints(val) + ".png")
            break
    else:
        print(f"[WARN] No image for {emoji}, skipping.")
        continue

    if not os.path.exists(img_path):
        print(f"[WARN] Missing PNG: {img_path} for key {key}")
        continue

    img = Image.open(img_path).convert("RGBA").resize((CELL_SIZE, CELL_SIZE))
    sheet.paste(img, (x * CELL_SIZE, y * CELL_SIZE), img)
    chars += emoji
    x += 1
    if x >= num_per_row:
        x = 0
        y += 1
        if y >= num_per_row:
            y = 0
            file_name = f"twemoji_sheet_{sheet_index}.png"
            sheet.save(os.path.join(TEXTURE_FONT_DIR, file_name))

            chars_list = [chars[i:i+num_per_row] for i in range(0, len(chars), num_per_row)]

            font_providers.append({
                "type": "bitmap",
                "file": f"minecraft:font/{file_name}",
                "ascent": JSON_ASCENT,
                "height": JSON_HEIGHT,
                "chars": chars_list
            })

file_name = f"twemoji_sheet_{sheet_index}.png"
sheet.save(os.path.join(TEXTURE_FONT_DIR, file_name))

chars_list = [chars[i:i+num_per_row] for i in range(0, len(chars), num_per_row)]
chars_list[-1] += "\u0000" * (num_per_row - len(chars_list[-1]))

while len(chars_list) < num_per_row:
    chars_list.append("\u0000" * num_per_row)

font_providers.append({
    "type": "bitmap",
    "file": f"minecraft:font/{file_name}",
    "ascent": JSON_ASCENT,
    "height": JSON_HEIGHT,
    "chars": chars_list
})

# === Write font JSON ===
font_json_path = os.path.join(FONT_DIR, "default.json")
with open(font_json_path, "w", encoding="utf-8") as f:
    json.dump({"providers": font_providers}, f, ensure_ascii=False, indent=2)

# === Write pack.mcmeta ===
pack_meta = {
    "pack": {
        "pack_format": PACK_FORMAT,
        "description": "Adds colored emojis to Minecraft! The emojis are from Twemojis and are licensed under CC BY 4.0"
    }
}
with open(os.path.join(OUTPUT_DIR, "pack.mcmeta"), "w", encoding="utf-8") as f:
    json.dump(pack_meta, f, ensure_ascii=False, indent=2)

print("✅ Done! Resource pack built at:", OUTPUT_DIR)