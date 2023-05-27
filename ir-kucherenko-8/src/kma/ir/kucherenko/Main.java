package kma.ir.kucherenko;

import kma.ir.kucherenko.clusterisation.Clusterisator;

public class Main {
    public static void main(String[] args) {
        int k = 5; // Number of clusters
        int maxIterations = 10; // Maximum number of iterations
        double threshold = 0.01; // Threshold for convergence

        Clusterisator clusterisator = new Clusterisator("/Users/daniil/Developer/ir-mohyla/ir-kucherenko-8/collection");
//        List<List<File>> clusters = clusterisator.cluster(k, maxIterations, threshold);
//
//        // Print the clusters
//        for (int i = 0; i < clusters.size(); i++) {
//            System.out.println("Cluster " + i + ":");
//            for (File file : clusters.get(i)) {
//                System.out.println(file.getName());
//            }
//            System.out.println();
//        }
    }
}