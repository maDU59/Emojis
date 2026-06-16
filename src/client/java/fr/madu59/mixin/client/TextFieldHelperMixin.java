package fr.madu59.mixin.client;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import fr.madu59.EmojiModClient;
import fr.madu59.emoji.Emoji;
import fr.madu59.emoji.EmojiManager;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.input.CharacterEvent;

@Mixin(TextFieldHelper.class)
public abstract class TextFieldHelperMixin {

    @Shadow
    Supplier<String> getMessageFn;

    @Shadow
    private int cursorPos;

    @Shadow
    public abstract void removeCharsFromCursor(int i);

    @Shadow
    public abstract void insertText(String string);

    @Inject(method = "charTyped", at = @At("RETURN"))
    private void charTyped(CharacterEvent characterEvent, CallbackInfoReturnable<Boolean> ci) {

        if(!characterEvent.codepointAsString().equals(":")) return;

        String beforeCursorText = (String)this.getMessageFn.get();
        int cursorPos = this.cursorPos;
        beforeCursorText = beforeCursorText.substring(0, cursorPos - 1);

        int emojiStart = beforeCursorText.lastIndexOf(':');
        int lastSpace = EmojiModClient.getLastSpace(beforeCursorText);

        if (emojiStart >= lastSpace && emojiStart >= 0){
            beforeCursorText = beforeCursorText.substring(emojiStart) + ":";

            for (Emoji emoji : EmojiManager.emojiList) {
                if (beforeCursorText.equals(emoji.getId())) {
                    this.removeCharsFromCursor(- emoji.getId().length());
                    this.insertText(emoji.getEmoji());
                    return;
                }
            }
        }
    }
}