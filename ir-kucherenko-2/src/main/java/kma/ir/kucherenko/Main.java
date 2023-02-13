package kma.ir.kucherenko;

public class Main {
    public static void main(String[] args) {
        TermDocument termDocument = new TermDocument("src/main/collection");

        IncidenceMatrix incidenceMatrix = new IncidenceMatrix(termDocument);
        incidenceMatrix.writeIncidenceMatrix("incidence_matrix.txt");

        InvertedIndex invertedIndex = new InvertedIndex(termDocument);
        invertedIndex.writeInvertedIndex("inverted_index.txt");
    }
}