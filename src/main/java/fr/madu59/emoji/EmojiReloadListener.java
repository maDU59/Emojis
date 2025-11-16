package fr.madu59.emoji;
import fr.madu59.EmojiMod;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class EmojiReloadListener implements ResourceManagerReloadListener {

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        EmojiMod.LOGGER.info("Trying to load");
        CustomEmojiManager.load();
        EmojiManager.load();
    }
}