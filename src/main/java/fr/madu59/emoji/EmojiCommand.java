package fr.madu59.emoji;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraft.commands.Commands;

import java.util.Map;

import com.mojang.brigadier.arguments.StringArgumentType;

public class EmojiCommand {

    public static void register(RegisterClientCommandsEvent event){
        event.getDispatcher().register(
            Commands.literal("emojis")
            .then(Commands.literal("add")
                .then(Commands.argument("slug", StringArgumentType.word())
                    .then(Commands.argument("value", StringArgumentType.greedyString())
                        .executes(context -> {
                            boolean success = CustomEmojiManager.addEmoji(StringArgumentType.getString(context, "slug"), StringArgumentType.getString(context, "value"));
                            if(success) feedbackMessage(Component.translatable("emoji-added-success"));
                            else feedbackMessage(Component.translatable("emoji-added-fail"));
                            return 1;
                        })
                    )
                )
            )
        );

        event.getDispatcher().register(
            Commands.literal("emojis")
            .then(Commands.literal("remove")
                .then(Commands.argument("slug", StringArgumentType.word())
                    .executes(context -> {
                        boolean success = CustomEmojiManager.removeEmoji(StringArgumentType.getString(context, "slug"));
                        if(success) feedbackMessage(Component.translatable("emoji-removed-success"));
                        else feedbackMessage(Component.translatable("emoji-removed-fail"));
                        return 1;
                    })
                )
            )
        );

        event.getDispatcher().register(
            Commands.literal("emojis")
            .then(Commands.literal("customlist")
                .executes(context -> {
                    if(CustomEmojiManager.customEmojiMap.size() > 0){
                        feedbackMessage(Component.translatable("display-custom-emoji-list"));
                        for (Map.Entry<String, String> entry : CustomEmojiManager.customEmojiMap.entrySet()){
                            feedbackMessage(Component.literal(entry.getKey() + " " + entry.getValue()));
                        }
                    }
                    else feedbackMessage(Component.translatable("empty-custom-emoji-list"));
                    return 1;
                })
            )
        );
    }

    public static void feedbackMessage(Component message){
        Minecraft.getInstance().player.displayClientMessage(message, false);
    }
}
