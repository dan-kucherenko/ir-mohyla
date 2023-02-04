package kma.ir.kucherenko;

public class Main {
    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary();
        dictionary.createDictionary("src/main/books/A Clockwork Orange - Antony Burgess.epub");
        dictionary.writeToFile("dictionary.txt");
        System.out.println("Number of unique words is:" + dictionary.getUniqueWords() + "\nNumber of repeated words is: " + dictionary.getRepeatedWords());
    }
}
