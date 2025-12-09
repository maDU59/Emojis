package fr.madu59.emoji;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;

public class EmojiReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        CustomEmojiManager.load();
        EmojiManager.load(manager);
    }

    @Override
    public Identifier getFabricId() {
        return Identifier.tryParse("emojis");
    }
}