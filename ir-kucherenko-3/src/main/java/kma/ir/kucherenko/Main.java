package kma.ir.kucherenko;

import kma.ir.kucherenko.biword.BiWordIndex;

public class Main {
    public static void main(String[] args) {
        BiWordIndex twoWordIndex = new BiWordIndex("src/main/collection");
        twoWordIndex.writeTermDoc("two_word_index.txt");
    }
}