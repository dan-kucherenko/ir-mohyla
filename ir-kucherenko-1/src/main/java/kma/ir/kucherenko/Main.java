package kma.ir.kucherenko;

import java.io.File;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        final File folder = new File("src/main/books");
        Dictionary dictionary = new Dictionary();
        for (File book : Objects.requireNonNull(folder.listFiles()))
            dictionary.createDictionary("src/main/books/" + book.getName());
        dictionary.writeToFile("dictionary.txt");
        System.out.println("Number of unique words:" + dictionary.getUniqueWords());
        System.out.println("Number of repeated words: " + dictionary.getRepeatedWords());
        System.out.println("General number of words: " + dictionary.getWordsNum());
    }
}
