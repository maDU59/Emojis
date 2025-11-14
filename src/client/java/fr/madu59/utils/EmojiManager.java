package fr.madu59.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
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
        try (InputStream is = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("emojis", "emojis.json")).get().open()) {
            JsonObject obj = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
            for (var entry : obj.entrySet()) {
                EMOJIS.put(entry.getKey(), entry.getValue().getAsString());
                String suggestion = entry.getKey() + " " + entry.getValue().getAsString();
                suggestions.add(suggestion);
                emojiList.add(new Emoji(entry.getKey(), entry.getValue().getAsString(), suggestion));
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
}