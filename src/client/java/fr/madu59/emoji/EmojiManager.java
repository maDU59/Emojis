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

public class EmojiManager{

    private static final Map<String, String> EMOJIS = new HashMap<>();
    public static final Map<String, Emoji> suggestionMap = new HashMap<>();
    public static final Map<String, Emoji> idMap = new HashMap<>();

    public static void load() {
        EMOJIS.clear();
        try (InputStream is = Minecraft.getInstance().getResourceManager().getResource(Identifier.tryParse("emojis:emojis.json")).get().open()) {
            JsonObject obj = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
            for (var entry : obj.entrySet()) {
                addEmoji(entry.getKey(), entry.getValue().getAsString());
            }

            for (Map.Entry<String, String> entry : CustomEmojiManager.customEmojiMap.entrySet()) {
                addEmoji(entry.getKey(), entry.getValue());
            }
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
        Emoji emoji = new Emoji(id, value, suggestion);
        suggestionMap.put(suggestion, emoji);
        idMap.put(id, emoji);
    }

    public static boolean removeEmoji(String id){
        Iterator<Emoji> it = idMap.values().iterator();

        while (it.hasNext()) {
            Emoji emoji = it.next();
            if (emoji.getId().equals(id)){
                EMOJIS.remove(id);
                suggestionMap.remove(emoji.getSuggestion());
                idMap.remove(id, emoji);
                return true;
            }
        }
        return false;
    }
}