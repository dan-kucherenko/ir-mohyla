package kma.ir.kucherenko;

import kma.ir.kucherenko.compressors.dictionary.DictionaryCompressor;

public class Main {
    public static void main(String[] args) {
        TermDocument termDocument = new TermDocument("src/main/collection");
        termDocument.writeTermDoc("termdoc");

        InvertedIndex ii = new InvertedIndex(termDocument);
        ii.writeInvertedIndex("inverted_index");

        DictionaryCompressor dictionaryCompressor = new DictionaryCompressor("src/main/collection");
        dictionaryCompressor.frontWrapping("dict_compressed");
        dictionaryCompressor.frontDecoding("src/main/additional_files/dict_compressed", "src/main/additional_files/dict_decompressed");

//        GammaInvIndexCompressor invIndexCompressor = new GammaInvIndexCompressor();
//        invIndexCompressor.createCompressedInvIndex(new File("src/main/additional_files/inverted_index"),
//                new File("src/main/additional_files/inverted_index_compressed"));
    }
}