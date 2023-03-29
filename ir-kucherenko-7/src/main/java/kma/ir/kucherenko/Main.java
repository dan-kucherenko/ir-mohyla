package kma.ir.kucherenko;

import kma.ir.kucherenko.posting_weight.PostingWeightSearcher;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PostingWeightSearcher pws = new PostingWeightSearcher();
        Scanner sc = new Scanner(System.in);
        System.out.println("Posting weight search:");
        String query = sc.nextLine();
        while (!query.equals("stop")) {
            System.out.println("Result of search for '" + query + "' is " + pws.findDocuments(query));
            System.out.println("If you want to continue, just type in a new request");
            query = sc.nextLine();
        }
    }
}