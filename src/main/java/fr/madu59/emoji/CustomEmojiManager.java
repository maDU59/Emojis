package fr.madu59.emoji;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

import net.minecraftforge.fml.loading.FMLPaths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class CustomEmojiManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("emojis-custom.json");
    public static Map<String, String> customEmojiMap = new LinkedHashMap<String, String>();

    public static boolean saveData(Map<String, String> map) {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(map, writer);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Map<String, String> loadData() {
        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
			return GSON.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
			return Collections.emptyMap();
        }
    }

    public static void load(){
        customEmojiMap = new LinkedHashMap<>(loadData());
    }

    public static boolean addEmoji(String id, String value){
        if(id.contains(":")) return false;
        id = ":" + id + ":";
        customEmojiMap.put(id, value);
        EmojiManager.addEmoji(id, value);
        return saveData(customEmojiMap);
    }

    public static boolean removeEmoji(String id){
        if(id.contains(":")) return false;
        id = ":" + id + ":";
        if(!customEmojiMap.containsKey(id)) return false;
        customEmojiMap.remove(id);
        if(!EmojiManager.removeEmoji(id)) return false;
        return saveData(customEmojiMap);
    }
}
