package fr.madu59.utils;

public class Emoji{

    final String id;
    final String emoji;
    final String suggestion;

    public Emoji(String id, String emoji, String suggestion){
        this.id = id;
        this.emoji = emoji;
        this.suggestion = suggestion;
    }

    public String getId(){
        return this.id;
    }

    public String getEmoji(){
        return this.emoji;
    }

    public String getSuggestion(){
        return this.suggestion;
    }
}