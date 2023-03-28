package kma.ir.kucherenko.posting_weight;

import java.util.Arrays;

public class PostingWeight implements Comparable<Scores> {
    private final int id;
    private Scores[] zoneScores;

    public PostingWeight(int id, Scores score) {
        this.id = id;
        this.zoneScores = new Scores[] {score};
    }

    public int getId() {
        return id;
    }

    public double getWeight() {
        double weight = 0;
        for (Scores zoneScore : zoneScores)
            weight += zoneScore.getWeight();
        return weight;
    }

    public void addZoneWeight(Scores score) {
        for (Scores zone : zoneScores)
            if (zone == score)
                return;
        Scores[] temporaryScores = new Scores[zoneScores.length + 1];
        System.arraycopy(zoneScores, 0, temporaryScores, 0, zoneScores.length);
        zoneScores = temporaryScores;
        zoneScores[zoneScores.length - 1] = score;
        Arrays.sort(zoneScores);
    }

    @Override
    public int compareTo(Scores that) {
        return Double.compare(getWeight(), that.getWeight());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(id + " : ");
        for (Scores zoneScore : zoneScores)
            result.append(zoneScore.getWeight()).append(',');
        return result.toString();
    }
}
