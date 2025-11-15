package fr.madu59;

import fr.madu59.emoji.EmojiCommand;
import fr.madu59.emoji.EmojiReloadListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class EmojiModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new EmojiReloadListener());
		EmojiCommand.register();
	}
}