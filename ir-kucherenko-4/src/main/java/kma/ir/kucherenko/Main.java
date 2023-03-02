package kma.ir.kucherenko;

import kma.ir.kucherenko.permuterm.PermuTerm;
import kma.ir.kucherenko.trigram.TriGram;
import kma.ir.kucherenko.trie.TermDocTrie;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        TermDocument termDocument = new TermDocument("src/main/collection");
        TermDocTrie termDocTrie = new TermDocTrie(termDocument);
        termDocTrie.write("trie.txt");
        PermuTerm permuTerm = new PermuTerm(termDocument);
        permuTerm.createPermuTerm();
        permuTerm.write("permuterm_index.txt");
        TriGram triGram = new TriGram();
        triGram.createTriGram(termDocument);
        triGram.write("trigram_index.txt");

        Scanner sc = new Scanner(System.in);
        System.out.println("Trie search:");
        String query = sc.nextLine();
        while (!query.equals("stop")) {
            System.out.println("Result of trie search for " + query + " is " + termDocTrie.getTrie().searchTrie(query).toString());
            System.out.println("If you want to continue, just type in a new request");
            query = sc.nextLine();
        }

        System.out.println("Trigram search:");
        query = sc.nextLine();
        while (!query.equals("stop")) {
            System.out.println("Result of trigram search for " + query + " is " + triGram.trigramSearch(query));
            System.out.println("If you want to continue, just type in a new request");
            query = sc.nextLine();
        }

        System.out.println("PermutermIndex search:");
        query = sc.nextLine();
        while (!query.equals("stop")) {
            System.out.println("Result of permuterm search for " + query + " is " + permuTerm.permuTermWildCardSearch(query));
            System.out.println("If you want to continue, just type in a new request");
            query = sc.nextLine();
        }
    }
}