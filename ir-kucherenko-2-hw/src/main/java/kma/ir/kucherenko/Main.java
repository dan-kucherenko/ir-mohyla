package kma.ir.kucherenko;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        TermDocument termDocument = new TermDocument("src/main/collection");
//        InvertedIndex invertedIndex = new InvertedIndex(termDocument);
//        invertedIndex.writeInvertedIndex("inverted_index.txt");


        BooleanSearch bs = new BooleanSearch();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your request: ");
        String query = sc.nextLine();


        while (!query.equals("stop")) {
            query = bs.validateQuery(query);
//            bs.booleanSearch(query);
            System.out.println(query);
            System.out.println("If you want to continue, just type in a new request");
            query = sc.nextLine();
        }
    }
}