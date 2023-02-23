package kma.ir.kucherenko.biword;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;
import kma.ir.kucherenko.Index;

import java.io.File;
import java.util.*;

public class BiWordIndex extends Index<BiWord, Set<Integer>> {
    private String prevWord = null;

    public BiWordIndex(String filePath) {
        index = new HashMap<>();
        booksFolder = new File(filePath).listFiles();
        reader = new Reader();
        reader.setIsIncludingTextContent(true);
        createIndex(filePath);
    }

    public void createIndex(String filePath) {
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
                        addWordIfMapNContains(word, Arrays.asList(booksFolder).indexOf(folder) + 1);
                    }
                }
            } catch (ReadingException | OutOfPagesException e) {
                e.printStackTrace();
            }
        }
    }

    private void addWordIfMapNContains(String word, int bookIndex) {
        if (!"".equals(word) && prevWord != null && !"".equals(prevWord)) {
            BiWord curWord = new BiWord(prevWord, word);
            Set<Integer> curBiSet = this.index.get(curWord);
            if (curBiSet == null) {
                curBiSet = new LinkedHashSet<>();
                curBiSet.add(bookIndex);
                this.index.put(curWord, curBiSet);
            } else
                this.index.get(curWord).add(bookIndex);
        }
        this.prevWord = word;
    }
}
