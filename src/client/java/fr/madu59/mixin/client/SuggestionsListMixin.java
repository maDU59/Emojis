package fr.madu59.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.brigadier.suggestion.Suggestion;

import fr.madu59.utils.Emoji;
import fr.madu59.utils.EmojiManager;

import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;

@Mixin(CommandSuggestions.SuggestionsList.class)
public abstract class SuggestionsListMixin {

    @Shadow
    @Final
    CommandSuggestions field_21615;

    @Shadow
    private int current;

    @Shadow
    @Final
    private List<Suggestion> suggestionList;

    @Inject(method = "useSuggestion", at = @At("TAIL"))
    private void useSuggestion(CallbackInfo ci) {

        EditBox editBox = this.field_21615.input;
        Suggestion suggestion = this.suggestionList.get(this.current);

        for (Emoji emoji : EmojiManager.emojiList) {
            if (suggestion.getText() == emoji.getSuggestion()) {
                editBox.deleteChars(- (emoji.getId().length() + emoji.getEmoji().length() + 1));
                editBox.setHighlightPos(editBox.getCursorPosition());
                editBox.insertText(emoji.getEmoji());
                return;
            }
        }
    }
}