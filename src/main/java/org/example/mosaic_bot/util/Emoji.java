package org.example.mosaic_bot.util;

public enum Emoji {
    WAVING_HAND("\uD83D\uDC4B"),
    PERSON("\uD83D\uDE4D\u200D♂\uFE0F"),
    TICK("✅"),
    CROSS("❌");
    private final String emojiCode;

    Emoji(String emojiCode) {
        this.emojiCode = emojiCode;
    }

    public String getCode() {
        return emojiCode;
    }
}