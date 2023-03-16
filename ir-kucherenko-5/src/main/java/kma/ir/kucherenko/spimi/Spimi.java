package kma.ir.kucherenko.spimi;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class Spimi {
    private final Reader reader;
    private final File[] documents;
    private final String outputFilePath;

    public Spimi(String filePath, String outputFilePath) {
        this.documents = new File(filePath).listFiles();
        this.reader = new Reader();
        this.outputFilePath = outputFilePath;
        this.reader.setIsIncludingTextContent(true);
    }

    public void executeSpimiAlg() {
        long startTime = System.currentTimeMillis();
        indexBlocks();
        Map<String, List<Integer>> mergedIndex = mergeIndex();
        writeToFile("src/main/additional_files/spimi_result", mergedIndex);
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }

    private void indexBlocks() {
        // Read input files and split into blocks
        BookSection bookSection;
        int blockId = 0;
        int docId = 1;
        for (File file : documents) {
            try {
                reader.setFullContent(file.getPath());
                int numOfPages = getNumOfPages();
                for (int pageIndex = 1; pageIndex < numOfPages; pageIndex++) {
                    bookSection = reader.readSection(pageIndex);
                    String sectionTextContent = bookSection.getSectionTextContent();
                    String[] wordsSection = sectionTextContent.split("\\W+");
                    List<String> block = new ArrayList<>(Arrays.asList(wordsSection));
                    writeBlockToFile(blockId, docId, block);
                    blockId++;
                }
            } catch (ReadingException | OutOfPagesException e) {
                e.printStackTrace();
            }
            docId++;
        }
    }

    private Map<String, List<Integer>> mergeIndex() {
        String outputFolderPath = Paths.get(outputFilePath).getParent().toString();
        Map<String, List<Integer>> mergedIndex = new TreeMap<>();
        for (File blockFile : new File(outputFolderPath + "/blocks").listFiles()) {
            try (BufferedReader br = new BufferedReader(new FileReader(blockFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    List<String> parts = Arrays.asList(line.split(":"));
                    String term = parts.get(0);
                    List<Integer> postingsList = parts.subList(1, parts.size())
                            .stream()
                            .map(Integer::parseInt)
                            .toList();
                    List<Integer> mergedPostingsList = mergedIndex.getOrDefault(term, new ArrayList<>());
                    mergedPostingsList.addAll(postingsList);
                    mergedIndex.put(term, mergePostingsLists(mergedPostingsList));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mergedIndex;
    }

    private void writeToFile(String fileName, Map<String, List<Integer>> mergedIndex) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (String term : mergedIndex.keySet()) {
                List<Integer> postingsList = mergedIndex.get(term);
                String postingsStr = String.join(",", postingsList.stream().map(Object::toString).toArray(String[]::new));
                bw.write(term + ":" + postingsStr + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeBlockToFile(int blockId, int docId, List<String> block) {
        String outputFolderPath = Paths.get(outputFilePath).getParent().toString();
        File blockFile = new File(outputFolderPath + "/blocks/block_" + blockId);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(blockFile))) {
            for (String term : block) {
                bw.write(term + ':' + docId + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getNumOfPages() {
        return reader.getToc().getNavMap().getNavPoints().size();
    }

    private List<Integer> mergePostingsLists(List<Integer> postingsList) {
        Set<Integer> set = new HashSet<>(postingsList);
        List<Integer> mergedPostingsList = new ArrayList<>(set);
        Collections.sort(mergedPostingsList);
        return mergedPostingsList;
    }
}
