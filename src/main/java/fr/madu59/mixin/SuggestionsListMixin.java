package fr.madu59.mixin;

import java.util.List;
import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.suggestion.Suggestion;

import fr.madu59.emoji.Emoji;
import fr.madu59.emoji.EmojiManager;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;

@Mixin(CommandSuggestions.SuggestionsList.class)
public abstract class SuggestionsListMixin {
    @Shadow
    private int current;
    
    @Shadow
    @Final
    private List<Suggestion> suggestionList;

    @Inject(method = "useSuggestion", at = @At("TAIL"))
    private void useSuggestion(CallbackInfo ci) {

        CommandSuggestions outer;
        try {
            Field outerField = this.getClass().getDeclaredField("this$0");
            outerField.setAccessible(true);
            outer = (CommandSuggestions) outerField.get(this);
        } catch (ReflectiveOperationException e) {
            return;
        }

        EditBox editBox = ((CommandSuggestionsAccessor) outer).getInput();
        Suggestion suggestion = this.suggestionList.get(this.current);

        for (Emoji emoji : EmojiManager.emojiList) {
            if (suggestion.getText().equals(emoji.getSuggestion())) {
                editBox.deleteChars(- emoji.getSuggestion().codePointCount(0,emoji.getSuggestion().length()));
                editBox.setHighlightPos(editBox.getCursorPosition());
                editBox.insertText(emoji.getEmoji());
                return;
            }
        }
    }
}