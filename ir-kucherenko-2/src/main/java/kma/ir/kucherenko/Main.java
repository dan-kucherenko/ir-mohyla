package kma.ir.kucherenko;

public class Main {
    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary("src/main/books");
        dictionary.writeToFile("dictionary.txt");
    }
}