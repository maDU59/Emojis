package fr.madu59;

import fr.madu59.emoji.EmojiCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.fml.common.EventBusSubscriber;



@EventBusSubscriber(modid = EmojiMod.MOD_ID)
public class ClientCommandsRegistration {
    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
		EmojiCommand.register(event);
	}
}