package kma.ir.kucherenko;

import java.util.LinkedHashSet;
import java.util.Set;

public class Word {
    private String word;
    private final Set<String> bookSet;

    public Word() {
        this.bookSet = new LinkedHashSet<>();
    }

    public Word(String word) {
        this.word = word;
        this.bookSet = new LinkedHashSet<>();
    }

    public String getWord() {
        return word;
    }

    public Set<String> getBookSet() {
        return bookSet;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void addBook(String book) {
        bookSet.add(book);
    }

    @Override
    public  String toString(){
        return word;
    }
}
