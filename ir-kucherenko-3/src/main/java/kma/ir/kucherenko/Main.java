package kma.ir.kucherenko;

import kma.ir.kucherenko.biword.BiWordIndex;
import kma.ir.kucherenko.coord_inverted.CoordInverted;
import kma.ir.kucherenko.search.BiWordSearcher;
import kma.ir.kucherenko.search.CoordIndexSearcher;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BiWordIndex biWordIndex = new BiWordIndex("src/main/collection");
        biWordIndex.writeToFile("bi_word_index.txt");
        CoordInverted coordInverted = new CoordInverted("src/main/collection");
        coordInverted.writeToFile("coordinates_inverted.txt");

        BiWordSearcher biWordSearcher = new BiWordSearcher(biWordIndex);
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your phrase you want to find: ");
        String query = sc.nextLine();
        while (!query.equals("stop")) {
            System.out.println("Result of request:" + query + " is " + Arrays.toString(biWordSearcher.search(query)));
            System.out.println("If you want to continue, just type in a new request");
            query = sc.nextLine();
        }

        CoordIndexSearcher coordIndexSearcher = new CoordIndexSearcher(coordInverted);
        System.out.print("Enter query for coordinated index search: ");
        query = sc.nextLine();
        while (!query.equals("stop")) {
            System.out.println("Result of request:" + query + " is " + Arrays.toString(coordIndexSearcher.search(query)));
            System.out.println("If you want to continue, just type in a new request");
            query = sc.nextLine();
        }
    }
}