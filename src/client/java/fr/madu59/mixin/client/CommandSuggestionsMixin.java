package fr.madu59.mixin.client;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import fr.madu59.EmojiModClient;
import fr.madu59.emoji.EmojiManager;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.commands.SharedSuggestionProvider;

@Mixin(CommandSuggestions.class)
public abstract class CommandSuggestionsMixin {

	@Shadow
    EditBox input;

	@Shadow
    boolean commandsOnly;

	@Shadow
	public abstract void showSuggestions(final boolean bl);

	@Shadow
    @Nullable
    private CompletableFuture<Suggestions> pendingSuggestions;
	
	@Inject(at = @At("TAIL"), method = "updateCommandInfo")
	private void updateCommandInfo(CallbackInfo ci) {

		String string = this.input.getValue();
		boolean isCommand = string.length() > 0 && string.charAt(0) == '/';

		if (!(this.commandsOnly || isCommand)) {
			
			int cursorPos = this.input.getCursorPosition();
			String beforeCursorText = string.substring(0, cursorPos);
			int emojiStart = beforeCursorText.lastIndexOf(':');
			int lastSpace = EmojiModClient.getLastSpace(beforeCursorText);

			if (emojiStart >= 0 && emojiStart >= lastSpace && emojiStart <= cursorPos && beforeCursorText.charAt(emojiStart) == ':'){

				this.pendingSuggestions = SharedSuggestionProvider.suggest(EmojiManager.suggestionMap.keySet(), new SuggestionsBuilder(beforeCursorText, emojiStart));
				this.pendingSuggestions.thenRun(() -> {
					if (!this.pendingSuggestions.isDone()) {
						return;
					}
					this.showSuggestions(false);
				});
				return;
			}
		}
	}
}