package fr.madu59.mixin.client;

import java.util.List;

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
    CommandSuggestions this$0;

    @Shadow
    private int current;

    @Shadow
    private List<Suggestion> suggestionList;

    @Inject(method = "useSuggestion", at = @At("TAIL"))
    private void useSuggestion(CallbackInfo ci) {

        EditBox editBox = this.this$0.input;
        Suggestion suggestion = this.suggestionList.get(this.current);

        if(EmojiManager.suggestionMap.containsKey(suggestion.getText())){
            Emoji emoji = EmojiManager.suggestionMap.get(suggestion.getText());
            editBox.deleteChars(- emoji.getSuggestion().codePointCount(0,emoji.getSuggestion().length()));
            editBox.setHighlightPos(editBox.getCursorPosition());
            editBox.insertText(emoji.getEmoji());
            return;
        }
    }
}