package kma.ir.kucherenko.biword;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BiWordIndex {
    private final Map<BiWord, Set<Integer>> biWordIndex;
    private final Reader reader;
    private final File[] booksFolder;
    private String prevWord = null;


    public BiWordIndex(String filePath) {
        biWordIndex = new HashMap<>();
        booksFolder = new File(filePath).listFiles();
        reader = new Reader();
        reader.setIsIncludingTextContent(true);
        createTermDoc(filePath);
    }

    public void createTermDoc(String filePath) {
        File folder = new File(filePath);
        if (folder.listFiles() != null)
            createDictionaryForFolders(folder);
        else {
            BookSection bookSection;
            String bookName = filePath.replace("src\\main\\collection\\", "");
            try {
                reader.setFullContent(filePath); // Must call before readSection.
                int numOfPages = getNumOfPages();
                for (int pageIndex = 1; pageIndex < numOfPages; pageIndex++) {
                    bookSection = reader.readSection(pageIndex);
                    String sectionTextContent = bookSection.getSectionTextContent(); // Excludes html tags.
                    String[] wordsSection = sectionTextContent.split("[\\s\\[\\]().”“’‘,!—•:;…]");
                    for (String word : wordsSection) {
                        word = formatWord(word);
                        addWordIfMapNContains(word, Arrays.asList(booksFolder).indexOf(folder));
                    }
                }
                System.out.println("Dictionary for " + bookName + " has been created");
            } catch (ReadingException | OutOfPagesException e) {
                e.printStackTrace();
            }
        }
    }


    public void writeTermDoc(String fileName) {
        new File("src/main/additional_files/").mkdirs();
        File dictionary = new File("src/main/additional_files/" + fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dictionary))
        ) {
            bufferedWriter.write(String.valueOf(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatWord(String word) {
        return word.toLowerCase().replaceAll("^\\W+", "")
                .replaceAll("\\W+$", "");
    }

    private int getNumOfPages() {
        return reader.getToc().getNavMap().getNavPoints().size();
    }

    private void addWordIfMapNContains(String word, int bookIndex) {
        if (!"".equals(word) && prevWord != null && !"".equals(prevWord)) {
            BiWord curWord = new BiWord(prevWord, word);
            Set<Integer> curBiSet = this.biWordIndex.get(curWord);
            if (curBiSet == null) {
                curBiSet = new LinkedHashSet<>();
                curBiSet.add(bookIndex);
                this.biWordIndex.put(curWord, curBiSet);
            } else
                this.biWordIndex.get(curWord).add(bookIndex);
        }
        this.prevWord = word;
    }

    private void createDictionaryForFolders(File folder) {
        for (File book : Objects.requireNonNull(folder.listFiles()))
            createTermDoc(book.getPath());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<BiWord, Set<Integer>> map : biWordIndex.entrySet())
            sb.append(map.getKey()).append(':').append(map.getValue()).append('\n');
        return sb.toString();
    }
}
