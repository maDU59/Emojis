package fr.madu59;

import com.mojang.logging.LogUtils;

import fr.madu59.emoji.CustomEmojiManager;
import fr.madu59.emoji.EmojiManager;
import fr.madu59.emoji.EmojiReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.function.Consumer;

import org.slf4j.Logger;

@Mod(EmojiMod.MOD_ID)
public class EmojiMod{
	public static final String MOD_ID = "emojis";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogUtils.getLogger();

	public EmojiMod(IEventBus modEventBus) {

        // Register client setup only for client distribution
        modEventBus.addListener(this::onClientSetup);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Emojis loaded on client side.");
        CustomEmojiManager.load();
    }
}