package kma.ir.kucherenko;

import kma.ir.kucherenko.biword.BiWordIndex;

public class Main {
    public static void main(String[] args) {
        BiWordIndex biWordIndex = new BiWordIndex("src/main/collection");
        biWordIndex.writeTermDoc("bi_word_index.txt");
    }
}