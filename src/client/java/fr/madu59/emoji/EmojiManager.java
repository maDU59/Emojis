package fr.madu59.emoji;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

public class EmojiManager{

    public static boolean loaded = false;
    private static final Map<String, String> EMOJIS = new HashMap<>();
    public static final ArrayList<String> suggestions = new ArrayList<String>();
    public static final ArrayList<Emoji> emojiList = new ArrayList<Emoji>();

    public static void load(ResourceManager manager) {
        EMOJIS.clear();
        suggestions.clear();
        emojiList.clear();
        try (InputStream is = Minecraft.getInstance().getResourceManager().getResource(Identifier.tryParse("emojis:emojis.json")).get().open()) {
            JsonObject obj = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
            for (var entry : obj.entrySet()) {
                addEmoji(entry.getKey(), entry.getValue().getAsString());
            }

            for (Map.Entry<String, String> entry : CustomEmojiManager.customEmojiMap.entrySet()) {
                addEmoji(entry.getKey(), entry.getValue());
            }

            loaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String name) {
        return EMOJIS.get(name);
    }

    public static Set<String> getNames() {
        return EMOJIS.keySet();
    }

    public static void addEmoji(String id, String value){
        EMOJIS.put(id, value);
        String suggestion = id + " " + value;
        suggestions.add(suggestion);
        emojiList.add(new Emoji(id, value, suggestion));
    }

    public static boolean removeEmoji(String id){
        Iterator<Emoji> it = emojiList.iterator();

        while (it.hasNext()) {
            Emoji emoji = it.next();
            if (emoji.getId().equals(id)){
                EMOJIS.remove(id);
                suggestions.remove(emoji.getSuggestion());
                emojiList.remove(emoji);
                return true;
            }
        }
        return false;
    }
}