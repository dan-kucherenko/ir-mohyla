package kma.ir.kucherenko;

public class Main {
    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary("src/main/books");
        dictionary.writeToFile("dictionary.txt");
        System.out.println("Number of repeated words: " + dictionary.getRepeatedWords());
        System.out.println("Number of unique words: " + dictionary.getUniqueWords() + '(' + dictionary.getDictionarySize() + " bytes)");
        System.out.println("General number of words: " + dictionary.getWordsNum() + '(' + dictionary.getCollectionSize() + " bytes)");
    }
}
