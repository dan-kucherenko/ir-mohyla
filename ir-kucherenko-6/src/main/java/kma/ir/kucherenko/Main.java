package kma.ir.kucherenko;

import kma.ir.kucherenko.compressors.dictionary.DictionaryCompressor;

import java.io.File;

public class Main {
    public static void main(String[] args) {
//        TermDocument termDocument = new TermDocument("src/main/collection");
//        termDocument.writeTermDoc("termdoc");
                DictionaryCompressor dictionaryCompressor = new DictionaryCompressor("src/main/collection");
                dictionaryCompressor.frontWrapping("dict_compressed");
    }
}