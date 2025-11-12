package fr.madu59.mixin.client;

import net.minecraft.client.Minecraft;

import java.util.Collection;
import java.util.regex.Pattern;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.commands.SharedSuggestionProvider;

@Mixin(CommandSuggestions.class)
public class ExampleClientMixin {

	private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(\\s+)");

	@Shadow
    @Final
    EditBox input;

	@Shadow
    @Final
    boolean commandsOnly;
	
	@Inject(at = @At("TAIL"), method = "updateCommandInfo")
	private void updateCommandInfo(CallbackInfo info) {
		String string = this.input.getValue();
		StringReader stringReader = new StringReader(string);
		boolean startsWithSlash = stringReader.canRead() && stringReader.peek() == '/';
		if (startsWithSlash) {
			stringReader.skip();
		}

		boolean isCommand = this.commandsOnly || startsWithSlash;
		int cursorPos = this.input.getCursorPosition();
		if (!isCommand) {
			String beforeCursorText = string.substring(0, cursorPos);
		}
	}
}