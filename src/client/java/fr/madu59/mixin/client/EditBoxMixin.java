package fr.madu59.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.madu59.EmojiModClient;
import fr.madu59.emoji.Emoji;
import fr.madu59.emoji.EmojiManager;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;

@Mixin(EditBox.class)
public abstract class EditBoxMixin {

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

            String beforeCursorText = this.getValue();
            int cursorPos = this.getCursorPosition();
            beforeCursorText = beforeCursorText.substring(0, cursorPos - 1);

            int emojiStart = beforeCursorText.lastIndexOf(':');
			int lastSpace = EmojiModClient.getLastSpace(beforeCursorText);

            if (emojiStart >= lastSpace && emojiStart >= 0){
                beforeCursorText = beforeCursorText.substring(emojiStart) + ":";

                if(EmojiManager.idMap.containsKey(beforeCursorText)){
                    Emoji emoji = EmojiManager.idMap.get(beforeCursorText);
                    this.deleteChars(- emoji.getId().length());
                    this.setHighlightPos(this.getCursorPosition());
                    this.insertText(emoji.getEmoji());
                    return;
                }
            }
        }
    }
}