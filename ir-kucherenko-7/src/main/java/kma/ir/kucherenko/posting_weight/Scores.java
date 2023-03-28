package kma.ir.kucherenko.posting_weight;

public enum Scores {
    TITLE(0.25),
    AUTHOR(0.15),

    DESCRIPTION(0.1),
    CONTENT(0.5);

    private final double weight;

    Scores(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }
}
