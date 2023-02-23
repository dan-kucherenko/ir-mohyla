package kma.ir.kucherenko.coord_inverted;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;
import kma.ir.kucherenko.Index;

import java.io.File;
import java.util.*;

public class CoordInverted extends Index<String, HashMap<Integer, TreeSet<Integer>>> {
    public CoordInverted(String filePath) {
        booksFolder = new File(filePath).listFiles();
        index = new TreeMap<>();
        reader = new Reader();
        reader.setIsIncludingTextContent(true);
        createIndex(filePath);
    }

    @Override
    public void createIndex(String filePath) {
        File folder = new File(filePath);
        if (folder.listFiles() != null)
            createDictionaryForFolders(folder);
        else {
            BookSection bookSection;
            try {
                int position = 0;
                reader.setFullContent(filePath); // Must call before readSection.
                int numOfPages = getNumOfPages();
                for (int pageIndex = 1; pageIndex < numOfPages; pageIndex++) {
                    bookSection = reader.readSection(pageIndex);
                    String sectionTextContent = bookSection.getSectionTextContent(); // Excludes html tags.
                    // Split the line into words
                    String[] wordsSection = sectionTextContent.split("[\\s\\[\\]().”“’‘,!—•:;…]");
                    for (String word : wordsSection) {
                        word = formatWord(word);
                        insertNewTerm(word, getBookIndex(filePath), position);
                        position++;
                    }
                }
            } catch (ReadingException | OutOfPagesException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertNewTerm(String term, int bookIndex, int position) {
        HashMap<Integer, TreeSet<Integer>> innerMap = index.computeIfAbsent(term, k -> new HashMap<>());
        TreeSet<Integer> positions = innerMap.computeIfAbsent(bookIndex, k -> new TreeSet<>());
        positions.add(position);
    }

    private int getBookIndex(String filePath) {
        return Arrays.asList(booksFolder).indexOf(new File(filePath)) + 1;
    }
}
