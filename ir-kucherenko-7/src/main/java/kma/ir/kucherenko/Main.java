package kma.ir.kucherenko;

import kma.ir.kucherenko.posting_weight.PostingWeightSearcher;

public class Main {
    public static void main(String[] args) {
        PostingWeightSearcher pws = new PostingWeightSearcher();
        System.out.println(pws.findDocuments("clockwork"));
    }
}