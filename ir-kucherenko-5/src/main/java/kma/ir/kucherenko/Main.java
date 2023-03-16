package kma.ir.kucherenko;

import kma.ir.kucherenko.spimi.Spimi;

public class Main {
    public static void main(String[] args) {
        Spimi spimi = new Spimi("src/main/collection/", "src/main/additional_files/spimi_result");
        spimi.executeSpimiAlg();
    }
}