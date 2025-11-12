package fr.madu59.utils;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class EmojiReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        EmojiManager.load(manager);
    }

    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation("emojis", "emoji_loader");
    }
}