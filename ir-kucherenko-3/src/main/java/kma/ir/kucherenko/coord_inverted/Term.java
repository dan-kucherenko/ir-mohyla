package kma.ir.kucherenko.coord_inverted;

import java.util.LinkedHashSet;
import java.util.Set;

public class Term <T> {
    private final String word;
    private final Set<T> bookSet;

    public Term(String word) {
        this.word = word;
        this.bookSet = new LinkedHashSet<>();
    }

    public String getWord() {
        return this.word;
    }

    public Set<T> getBookSet() {
        return bookSet;
    }

    public void addBook(T book) {
        bookSet.add(book);
    }

    @Override
    public String toString() {
        return this.word;
    }
}
