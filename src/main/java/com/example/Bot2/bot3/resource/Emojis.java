package com.example.Bot2.bot3.resource;


import com.vdurmont.emoji.EmojiParser;

public enum Emojis {
    SAVE(":envelope_with_arrow:"),
    DUMP(":put_litter_in_its_place:"),
    UP(":point_up_2:"),

    WAVE(":wave:");

    private final String value;

    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

    Emojis(String value) {
        this.value = value;
    }
}



