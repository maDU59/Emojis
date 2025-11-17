package fr.madu59.mixin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.madu59.emoji.Emoji;
import fr.madu59.emoji.EmojiManager;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;

@Mixin(EditBox.class)
public abstract class EditBoxMixin {

    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("(\\s+)");
	private static final Pattern COLLON_PATTERN = Pattern.compile("(:)");

    @Shadow
    public abstract String getValue();

    @Shadow
    public abstract int getCursorPosition();

    @Shadow
    public abstract void deleteChars(int i);

    @Shadow
    public abstract void setHighlightPos(int i);

    @Shadow
    public abstract void insertText(String string);

    @Inject(method = "charTyped", at = @At("RETURN"))
    private void useSuggestion(CharacterEvent charEvent, CallbackInfoReturnable<Boolean> ci) {
        if (ci.getReturnValue() && charEvent.codepointAsString().equals(":")){

            String value = this.getValue();
            int cursorPos = this.getCursorPosition();
            value = value.substring(0, cursorPos - 1);

            int emojiStart = getLastPattern(value, COLLON_PATTERN);
			int lastSpace = getLastPattern(value, WHITESPACE_PATTERN);

            if (emojiStart >= lastSpace && emojiStart >= 0){
                value = value.substring(emojiStart) + ":";

                for (Emoji emoji : EmojiManager.emojiList) {
                    if (value.equals(emoji.getId())) {
                        this.deleteChars(- emoji.getId().length());
                        this.setHighlightPos(this.getCursorPosition());
                        this.insertText(emoji.getEmoji());
                        return;
                    }
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