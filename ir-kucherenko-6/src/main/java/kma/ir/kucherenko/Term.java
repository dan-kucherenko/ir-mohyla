package kma.ir.kucherenko;

import java.util.LinkedHashSet;
import java.util.Set;

public class Term {
    private final String word;
    private final Set<String> bookSet;

    public Term(String word) {
        this.word = word;
        this.bookSet = new LinkedHashSet<>();
    }

    public String getWord() {
        return this.word;
    }

    public Set<String> getBookSet() {
        return bookSet;
    }

    public void addBook(String book) {
        bookSet.add(book);
    }

    @Override
    public String toString() {
        return this.word;
    }
}