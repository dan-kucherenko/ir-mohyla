package kma.ir.kucherenko;

import kma.ir.kucherenko.compressors.dictionary.DictionaryCompressor;
import kma.ir.kucherenko.compressors.invertedindex.GammaInvIndexCompressor;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        TermDocument termDocument = new TermDocument("src/main/collection");
        termDocument.writeTermDoc("termdoc");

        InvertedIndex ii = new InvertedIndex(termDocument);
        ii.writeInvertedIndex("inverted_index");

        DictionaryCompressor dictionaryCompressor = new DictionaryCompressor("src/main/collection");
        dictionaryCompressor.frontWrapping("dict_compressed");

        GammaInvIndexCompressor invIndexCompressor = new GammaInvIndexCompressor();
        invIndexCompressor.createCompressedInvIndex(new File("src/main/additional_files/inverted_index"),
                new File("src/main/additional_files/inverted_index_compressed"));
    }
}