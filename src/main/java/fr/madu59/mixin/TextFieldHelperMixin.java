package fr.madu59.mixin;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.madu59.emoji.Emoji;
import fr.madu59.emoji.EmojiManager;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.input.CharacterEvent;

@Mixin(TextFieldHelper.class)
public abstract class TextFieldHelperMixin {

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(\\s+)");
	private static final Pattern COLLON_PATTERN = Pattern.compile("(:)");

    @Shadow
    @Final
    Supplier<String> getMessageFn;

    @Shadow
    @Final
    private int cursorPos;

    @Shadow
    public abstract void removeCharsFromCursor(int i);

    @Shadow
    public abstract void insertText(String string);

    @Inject(method = "charTyped", at = @At("RETURN"))
    private void charTyped(CharacterEvent characterEvent, CallbackInfoReturnable<Boolean> ci) {

        if(! characterEvent.codepointAsString().equals(":")) return;

        String value = (String)this.getMessageFn.get();
        int cursorPos = this.cursorPos;
        value = value.substring(0, cursorPos - 1);

        int emojiStart = getLastPattern(value, COLLON_PATTERN);
        int lastSpace = getLastPattern(value, WHITESPACE_PATTERN);

        if (emojiStart >= lastSpace && emojiStart >= 0){
            value = value.substring(emojiStart) + ":";

            for (Emoji emoji : EmojiManager.emojiList) {
                if (value.equals(emoji.getId())) {
                    this.removeCharsFromCursor(- emoji.getId().length());
                    this.insertText(emoji.getEmoji());
                    return;
                }
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