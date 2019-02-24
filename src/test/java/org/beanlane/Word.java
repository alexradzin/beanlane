package org.beanlane;

import static java.lang.String.format;

public class Word {
    private String word;
    private int length;

    public Word(String word) {
        this.word = word;
        length = word.length(); // Throws NPE if word is null
    }

    @Override
    public String toString() {
        return format("Word{word=%s, length=%s}", word, length);
    }
}
