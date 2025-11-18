package fr.madu59;

import fr.madu59.emoji.EmojiReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

@EventBusSubscriber(
    modid = EmojiMod.MOD_ID,
    value = Dist.CLIENT
)
public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(ResourceLocation.tryParse("emojis:emojis"), new EmojiReloadListener());
    }
}