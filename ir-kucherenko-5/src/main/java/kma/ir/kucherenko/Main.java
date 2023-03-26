package kma.ir.kucherenko;

import kma.ir.kucherenko.spimi.Spimi;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Spimi spimi = new Spimi("src/main/collection/");
        spimi.executeSPIMI();
    }
}