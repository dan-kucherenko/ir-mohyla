package kma.ir.kucherenko;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Word {
    private String word;
    private List<String> bookList;

    public Word() {
        this.bookList = new ArrayList<>();
    }

    public Word(String word) {
        this.word = word;
        this.bookList = new ArrayList<>();
    }

    public String getWord() {
        return word;
    }

    public List<String> getBookList() {
        return bookList;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void addBook(String book) {
        bookList.add(book);
    }

    @Override
    public  String toString(){
        return word;
    }
}
