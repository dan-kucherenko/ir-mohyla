package kma.ir.kucherenko.spimi;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Spimi {
    private static int BLOCK_SIZE = 1000;
    private final Reader reader;
    private final StringBuilder resBuilder;
    private final File[] documents;
    int filesNum;

    public Spimi(String filePath) {
        documents = new File(filePath).listFiles();
        filesNum = documents.length;
        resBuilder = new StringBuilder();
        reader = new Reader();
        reader.setIsIncludingTextContent(true);
    }

    public void executeSpimiAlg() {
        long startTime = System.currentTimeMillis();
        List<List<String>> blocks = buildBlocks();
        Map<String, List<Integer>> indexes = indexBlocks(blocks);
        Map<String, List<Integer>> res =  mergedIndex(indexes);
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }
    public void writeToFile(String fileName) {
        new File("src/main/additional_files/").mkdirs();
        File spimiRes = new File("src/main/additional_files/" + fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(spimiRes))
        ) {
            bufferedWriter.write(String.valueOf(resBuilder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<List<String>> buildBlocks() {
        // Step 1: Read input files and split into blocks
        BookSection bookSection;
        List<List<String>> blocks = new ArrayList<>();
        for (File file : documents) {
            List<String> block = new ArrayList<>();
            try {
                reader.setFullContent(file.getPath());
                int numOfPages = getNumOfPages();
                for (int pageIndex = 1; pageIndex < numOfPages; pageIndex++) {
                    bookSection = reader.readSection(pageIndex);
                    String sectionTextContent = bookSection.getSectionTextContent();
                    String[] wordsSection = sectionTextContent.split("\\W+");
                    block.addAll(List.of(wordsSection));
                    resBuilder.append(block);
                    if (block.size() >= BLOCK_SIZE) {
                        blocks.add(block);
                        block = new ArrayList<>();
                    }
                }
                if (!block.isEmpty())
                    blocks.add(block);
            } catch (ReadingException | OutOfPagesException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        return blocks;
    }

    private Map<String, List<Integer>> indexBlocks(List<List<String>> blocks) {
        Map<String, List<Integer>> indexedBlocks = new HashMap<>();
        int blockId = 0;
        for (List<String> block : blocks) {
            for (String term : block) {
                List<Integer> postingsList = indexedBlocks.getOrDefault(term, new ArrayList<>());
                if (postingsList.isEmpty() || postingsList.get(postingsList.size() - 1) != blockId)
                    postingsList.add(blockId);
                indexedBlocks.put(term, postingsList);
            }
            blockId++;
        }
        return indexedBlocks;
    }

    private Map<String, List<Integer>> mergedIndex(Map<String, List<Integer>> partialIndex) {
        Map<String, List<Integer>> index = new TreeMap<>();
        for (String term : partialIndex.keySet()) {
            List<Integer> postingsList = partialIndex.get(term);
            List<Integer> mergedPostingsList = index.getOrDefault(term, new ArrayList<>());
            mergedPostingsList.addAll(postingsList);
            index.put(term, mergePostingsLists(mergedPostingsList));
        }
        return index;
    }

    private List<Integer> mergePostingsLists(List<Integer> postingsList) {
        Set<Integer> set = new HashSet<>(postingsList);
        List<Integer> mergedPostingsList = new ArrayList<>(set);
        Collections.sort(mergedPostingsList);
        return mergedPostingsList;
    }

    private int getNumOfPages() {
        return reader.getToc().getNavMap().getNavPoints().size();
    }
}
