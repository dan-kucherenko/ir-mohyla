package kma.ir.kucherenko;

public class Main {
    public static void main(String[] args) {
        TermDocument dictionary = new TermDocument("src/main/books");
        dictionary.writeTermDoc("dictionary.txt");
        dictionary.createIncidenceMatrix();
        dictionary.writeIncidenceMatrix("incidence_matrix.txt");
    }
}