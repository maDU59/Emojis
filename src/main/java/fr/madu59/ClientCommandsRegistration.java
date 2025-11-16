package fr.madu59;

import fr.madu59.emoji.EmojiCommand;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber(modid = EmojiMod.MOD_ID)
public class ClientCommandsRegistration {
    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
		EmojiCommand.register(event);
	}
}