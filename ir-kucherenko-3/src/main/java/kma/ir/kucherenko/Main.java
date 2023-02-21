package kma.ir.kucherenko;

import kma.ir.kucherenko.biword.BiWordIndex;
import kma.ir.kucherenko.coord_inverted.CoordInverted;

public class Main {
    public static void main(String[] args) {
        BiWordIndex biWordIndex = new BiWordIndex("src/main/collection");
        biWordIndex.writeToFile("bi_word_index.txt");

        CoordInverted coordInverted = new CoordInverted("src/main/collection");
        coordInverted.writeToFile("coordinates_inverted.txt");
    }
}