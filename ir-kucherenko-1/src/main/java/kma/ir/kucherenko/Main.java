package kma.ir.kucherenko;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        final File folder = new File("src/main/books");
        Dictionary dictionary = new Dictionary();
        for (File book : folder.listFiles())
            dictionary.createDictionary("src/main/books/" + book.getName());
        dictionary.writeToFile("dictionary.txt");
        System.out.println("Number of unique words is:" + dictionary.getUniqueWords() + "\nNumber of repeated words is: " + dictionary.getRepeatedWords() + "\nGeneral number of words: " + dictionary.getWordsNum());
    }
}
