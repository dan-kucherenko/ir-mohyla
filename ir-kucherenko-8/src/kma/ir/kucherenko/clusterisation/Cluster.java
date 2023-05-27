package kma.ir.kucherenko.clusterisation;

import com.kursx.parser.fb2.FictionBook;

public class Cluster {
    private FictionBook leader;
    private double weight;

    public Cluster(FictionBook leader) {
        this.leader = leader;
    }

    public Cluster(FictionBook leader, double weight){
        this.leader = leader;
        this.weight = weight;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }
}
