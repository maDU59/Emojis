package fr.madu59.mixin.client;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import fr.madu59.utils.EmojiManager;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.commands.SharedSuggestionProvider;

@Mixin(CommandSuggestions.class)
public abstract class CommandSuggestionsMixin {

	private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(\\s+)");
	private static final Pattern COLLON_PATTERN = Pattern.compile("(:)");

	@Shadow
    @Final
    EditBox input;

	@Shadow
    @Final
    boolean commandsOnly;

	@Shadow
	public abstract void showSuggestions(boolean bl);

	@Shadow
    @Nullable
    private CompletableFuture<Suggestions> pendingSuggestions;
	
	@Inject(at = @At("TAIL"), method = "updateCommandInfo", cancellable = true)
	private void updateCommandInfo(CallbackInfo ci) {

		String string = this.input.getValue();
		StringReader stringReader = new StringReader(string);
		boolean isCommand = stringReader.canRead() && stringReader.peek() == '/';
		if (isCommand) {
			stringReader.skip();
		}

		if (!(this.commandsOnly || isCommand)) {
			
			int cursorPos = this.input.getCursorPosition();
			String beforeCursorText = string.substring(0, cursorPos);
			int emojiStart = getLastPattern(beforeCursorText, COLLON_PATTERN);
			int lastSpace = getLastPattern(beforeCursorText, WHITESPACE_PATTERN);

			if (emojiStart >= 0 && emojiStart >= lastSpace && emojiStart <= cursorPos && beforeCursorText.charAt(emojiStart) == ':'){

				this.pendingSuggestions = SharedSuggestionProvider.suggest(EmojiManager.suggestions, new SuggestionsBuilder(beforeCursorText, emojiStart));
				this.pendingSuggestions.thenRun(() -> {
					if (!this.pendingSuggestions.isDone()) {
						return;
					}
					this.showSuggestions(false);
				});
				ci.cancel();
			}
		}
	}

	private int getLastPattern(String string, Pattern pattern){
		if (string == null || string.isEmpty()){
			return -1;
		}
		Matcher matcher = pattern.matcher(string);
		int lastIndex = -1;

		while (matcher.find())
		{
			lastIndex = matcher.start();
		}
		
		return lastIndex;
	}
}