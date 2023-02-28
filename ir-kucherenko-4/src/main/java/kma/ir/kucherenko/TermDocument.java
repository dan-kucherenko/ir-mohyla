package kma.ir.kucherenko;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;

public class TermDocument {
    private final HashSet<String> dictionary;
    private final Reader reader;

    public TermDocument(String filePath) {
        dictionary = new HashSet<>();
        reader = new Reader();
        // Optional, to return the tags-excluded version.
        reader.setIsIncludingTextContent(true);
        createTermDoc(filePath);
    }

    public HashSet<String> getDictionary() {
        return dictionary;
    }

    public void createTermDoc(String filePath) {
        File folder = new File(filePath);
        if (folder.listFiles() != null)
            createDictionaryForFolders(folder);
        else {
            BookSection bookSection;
            try {
                reader.setFullContent(filePath); // Must call before readSection.
                int numOfPages = reader.getToc().getNavMap().getNavPoints().size();
                for (int pageIndex = 1; pageIndex < numOfPages; pageIndex++) {
                    bookSection = reader.readSection(pageIndex);
                    String sectionTextContent = bookSection.getSectionTextContent(); // Excludes html tags.
                    // Split the line into words
                    String[] wordsSection = sectionTextContent.split("\\W+");
                    for (String word : wordsSection) {
                        if (!word.equals("")) {
                            word = word.toLowerCase();
                            dictionary.add(word);
                        }
                    }
                }
            } catch (ReadingException | OutOfPagesException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDictionaryForFolders(File folder) {
        for (File book : Objects.requireNonNull(folder.listFiles()))
            createTermDoc(book.getPath());
    }
}