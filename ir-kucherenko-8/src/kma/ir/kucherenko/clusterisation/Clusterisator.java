package kma.ir.kucherenko.clusterisation;

import kma.ir.kucherenko.spimi.Spimi;

import java.io.File;
import java.util.*;

public class Clusterisator {
    private final List<File> documents;
    private final List<Spimi> spimiIndexes;

    private List<Cluster> clusters;

    public Clusterisator(String collectionPath) {
        this.documents = createListOfDocs(collectionPath);
        this.spimiIndexes = new ArrayList<>();
        for (File file : documents){
            Spimi spimi = new Spimi(file.getPath());
            spimiIndexes.add(spimi);
            spimi.executeSPIMI();
        }
        clusters = selectInitialCentroids();
    }

    private List<File> createListOfDocs(String collectionPath) {
        File[] docArray = new File(collectionPath).listFiles();
        return new ArrayList<>(Arrays.asList(docArray));
    }

    private ArrayList<Cluster> selectInitialCentroids() {
        ArrayList<Cluster> res = new ArrayList<>();
        Random r = new Random();
        int sqrtN = (int) Math.sqrt(spimiIndexes.size());
        for (int i = 0; i < sqrtN; i++) {
            int index = r.nextInt(spimiIndexes.size());
            res.add(new Cluster(spimiIndexes.get(index).getDocument()));
            spimiIndexes.remove(index);
        }
        return res;
    }

//    public void runKMeans(int k) {
//        // Select initial centroids randomly
//        List<double[]> centroids = selectInitialCentroids(k);
//
//        List<List<double[]>> clusters = new ArrayList<>();
//        for (int i = 0; i < k; i++) {
//            clusters.add(new ArrayList<>());
//        }
//
//        boolean converged = false;
//        double epsilon = 0.0001;
//        int maxIterations = 100;
//
//        int iteration = 0;
//        while (!converged && iteration < maxIterations) {
//            // Assign documents to clusters based on the cosine similarity with centroids
//            for (Spimi indexedDocument : spimiIndexes) {
//                double[] documentVector = documentToDoubleArray(indexedDocument, docFreq);
//                int bestClusterIndex = 0;
//                double maxSimilarity = Double.NEGATIVE_INFINITY;
//                for (int i = 0; i < centroids.size(); i++) {
//                    double similarity = calculateCosineSimilarity(documentVector, centroids.get(i));
//                    if (similarity > maxSimilarity) {
//                        bestClusterIndex = i;
//                        maxSimilarity = similarity;
//                    }
//                }
//                clusters.get(bestClusterIndex).add(documentVector);
//            }
//
//            // Update centroids based on the mean vector of each cluster
//            converged = true;
//            for (int i = 0; i < k; i++) {
//                double[] meanVector = calculateMeanVector(clusters.get(i));
//                double[] oldCentroid = centroids.get(i);
//                centroids.set(i, meanVector);
//                double centroidDistance = calculateCosineSimilarity(oldCentroid, meanVector);
//                if (centroidDistance > epsilon) {
//                    converged = false;
//                }
//                clusters.get(i).clear();
//            }
//            iteration++;
//        }
//
//        // Print cluster results
//        for (int i = 0; i < k; i++) {
//            System.out.println("Cluster " + (i+1) + ":");
//            for (double[] documentVector : clusters.get(i)) {
//                int documentIndex = getDocumentIndex(documentVector);
//                System.out.println(documents.get(documentIndex).getName());
//            }
//            System.out.println();
//        }
//    }

//    private int getDocumentIndex(double[] documentVector) {
//        for (int i = 0; i < spimiIndexes.size(); i++) {
//            double[] vector = documentToDoubleArray(spimiIndexes.get(i), docFreq);
//            if (Arrays.equals(vector, documentVector))
//                return i;
//        }
//        return -1;
//    }

    private double[] calculateMeanVector(List<double[]> cluster) {
        int vectorLength = cluster.get(0).length;
        double[] meanVector = new double[vectorLength];
        for (int i = 0; i < vectorLength; i++) {
            double sum = 0;
            for (double[] documentVector : cluster) {
                sum += documentVector[i];
            }
            meanVector[i] = sum / cluster.size();
        }
        return meanVector;
    }

    private double calculateCosineSimilarity(double[] vector1, double[] vector2) {
        if (vector1.length != vector2.length)
            throw new IllegalArgumentException("Vectors must be of equal length");

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += Math.pow(vector1[i], 2);
            norm2 += Math.pow(vector2[i], 2);
        }

        double denominator = Math.sqrt(norm1) * Math.sqrt(norm2);

        if (denominator == 0)
            return 0.0;
        return dotProduct / denominator;
    }

    public double[] documentToDoubleArray(Spimi indexedDocument, Map<String, Integer> docFreq) {
        int wordCount = indexedDocument.getIndex().size();
        double[] vector = new double[wordCount];

        // Count the number of occurrences of each word in the document
        Map<String, Integer> wordFreq = new HashMap<>();
        for (String word : indexedDocument.getIndex().keySet()) {
            if (!wordFreq.containsKey(word))
                wordFreq.put(word, 1);
            else
                wordFreq.put(word, wordFreq.get(word) + 1);
        }

        // Calculate the TF-IDF weight for each word in the document
        for (int i = 0; i < vector.length; i++) {
            String word = (String) indexedDocument.getIndex().keySet().toArray()[i];
            if (wordFreq.containsKey(word)) {
                double tf = (double) wordFreq.get(word) / wordCount;
                double idf = Math.log((double) docFreq.size() / docFreq.get(word));
                vector[i] = tf * idf;
            }
        }
        return vector;
    }
}
