package kma.ir.kucherenko.spimi;

import com.kursx.parser.fb2.Element;
import com.kursx.parser.fb2.FictionBook;
import com.kursx.parser.fb2.Section;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Spimi {
    private static int MAX_TERMS = 10000;
    private final FictionBook[] documents;
    private static final String BLOCK_PATH = "src/main/additional_files/blocks";
    private Map<String, TreeSet<Integer>> index = new TreeMap<>();
    private int counter = 0;
    private int serial = 0;
    private int id = 1;

    public Spimi(String sourcePath) {
        File[] fileDocs = new File(sourcePath).listFiles();
        this.documents = new FictionBook[fileDocs.length];
        try {
            for (int i = 0; i < documents.length; i++)
                documents[i] = new FictionBook(fileDocs[i]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeSPIMI()  {
        buildSpimiIndex();
        try {
            mergeBlocksToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buildSpimiIndex() {
        for (FictionBook document : documents) {
            int docID = id;
            for (Section section : document.getBody().getSections()) {
                for (Element element : section.getElements()) {
                    String[] words = element.getText().trim().toLowerCase().split("\\W+");
                    for (String word : words) {
                        if (!word.equals("") && word.length() >= 1) {
                            if (counter > MAX_TERMS) {
                                // flush to disk
                                System.out.println("flushing...");
                                writeBlockToFile();
                                counter = 0;
                            } else {
                                if (index.containsKey(word)) {
                                    TreeSet<Integer> list = index.get(word);
                                    list.add(docID);
                                    ++counter;
                                } else {
                                    TreeSet<Integer> list = new TreeSet<>();
                                    list.add(docID);
                                    index.put(word, list);
                                    ++counter;
                                }
                            }
                        }
                    }
                }
            }
            id++;
        }
        writeBlockToFile();
    }

    private void writeBlockToFile() {
        try {
            String fileName = serial + ".txt";
            File dir = new File(BLOCK_PATH);
            File actualFile = new File(dir, fileName);
            try (BufferedWriter br = new BufferedWriter(new FileWriter(actualFile));
                 PrintWriter out = new PrintWriter(br)) {
                for (Map.Entry<String, TreeSet<Integer>> entry : index.entrySet()) {
                    String term = entry.getKey();
                    TreeSet<Integer> postingsList = entry.getValue();
                    out.write(term + " ");
                    for (Integer docID : postingsList)
                        out.write(docID + ",");
                    out.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ++serial;
        index = new TreeMap<>();
        System.gc();
    }

    private void mergeBlocksToFile() throws Exception {
        List<BufferedReader> bufferedReaders = new ArrayList<>();
        Files.walk(Paths.get(new File(BLOCK_PATH).getAbsolutePath())).forEach(filePath -> {
            if (Files.isRegularFile(filePath)) {
                try {
                    bufferedReaders.add(new BufferedReader(new FileReader(filePath.toString())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        List<SpimiTerm> tmpEntries = new ArrayList<>();
        String word;
        for (int i = 0; i < bufferedReaders.size(); i++){
//            if ((word = bufferedReaders.get(i).readLine()) != null)
                tmpEntries.add(new SpimiTerm(bufferedReaders.get(i).readLine()));
        }
        try (BufferedWriter br = new BufferedWriter(new FileWriter("src/main/additional_files/spimi_result"));
             PrintWriter out = new PrintWriter(br)) {
            while (bufferedReaders.size() > 0) {
                List<Integer> smallestTermBlockIDs = new ArrayList<>();
                smallestTermBlockIDs.add(0);
                if (tmpEntries.isEmpty())
                    return;
                String smallestWord = tmpEntries.get(0).getTerm();
                // finding next smallest term and getting it's posting list
                for (int i = 1; i < tmpEntries.size(); i++) {
                    String currWord = tmpEntries.get(i).getTerm();
                    if (currWord.compareTo(smallestWord) < 0) {
                        smallestTermBlockIDs = new ArrayList<>();
                        smallestTermBlockIDs.add(i);
                        smallestWord = currWord;
                    } else if (currWord.compareTo(smallestWord) == 0)
                        // if equal words on same position in both blocks
                        smallestTermBlockIDs.add(i);
                }

                List<Integer> mergedPostingList = new ArrayList<>();
                out.write(tmpEntries.get(smallestTermBlockIDs.get(0)).getTerm());
                for (int i = smallestTermBlockIDs.size() - 1; i >= 0; i--) {
                    int blockId = smallestTermBlockIDs.get(i);
                    // merging postings lists
                    mergedPostingList = union(mergedPostingList, tmpEntries.get(blockId).getPostingsList());
                    // getting next term
                    String nextSpimiTerm = bufferedReaders.get(blockId).readLine();
                    if (nextSpimiTerm == null) {
                        tmpEntries.remove(blockId);
                        bufferedReaders.get(blockId).close();
                        bufferedReaders.remove(blockId);
                    } else {
                        SpimiTerm termListPair = new SpimiTerm(nextSpimiTerm);
                        tmpEntries.set(blockId, termListPair);
                    }
                }
                out.write(mergedPostingList + "\n");
            }
        }
        for (File file : Objects.requireNonNull(new File(BLOCK_PATH).listFiles()))
            file.delete();
    }

    private List<Integer> union(List<Integer> list1, List<Integer> list2) {
        List<Integer> resultList = new ArrayList<>();

        int i = 0;
        int j = 0;

        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i) == list2.get(j)) {
                resultList.add(list1.get(i));
                ++i;
                ++j;
            } else if (list1.get(i) < list2.get(j)) {
                resultList.add(list1.get(i));
                ++i;
            } else {
                resultList.add(list2.get(j));
                ++j;
            }
        }

        // copy left-overs
        while (i < list1.size()) {
            resultList.add(list1.get(i));
            ++i;
        }

        while (j < list2.size()) {
            resultList.add(list2.get(j));
            ++j;
        }

        return resultList;
    }
}
