package kma.ir.kucherenko;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TermDocument {
    private final Map<String, Set<String>> termDoc;
    private final Reader reader;
    private final File[] books;
    private final String path;

    public TermDocument(String filePath) {
        termDoc = new TreeMap<>();
        reader = new Reader();
        books = new File(filePath).listFiles();
        path = filePath;
        // Optional, to return the tags-excluded version.
        reader.setIsIncludingTextContent(true);
        createTermDoc(filePath);
    }

    public Map<String, Set<String>> getTermDoc() {
        return termDoc;
    }

    public File[] getBooks() {
        return books;
    }

    public String getPath() {
        return path;
    }

    public void createTermDoc(String filePath) {
        File folder = new File(filePath);
        if (folder.listFiles() != null)
            createDictionaryForFolders(folder);
        else {
            BookSection bookSection = null;
            Term curWord = null;
            String bookName = filePath.replace("src\\main\\collection\\", "");
            try {
                reader.setFullContent(filePath); // Must call before readSection.
                int numOfPages = reader.getToc().getNavMap().getNavPoints().size();
                for (int pageIndex = 1; pageIndex < numOfPages; pageIndex++) {
                    bookSection = reader.readSection(pageIndex);
                    String sectionTextContent = bookSection.getSectionTextContent(); // Excludes html tags.
                    // Split the line into words
                    String[] wordsSection = sectionTextContent.split("\\W+");
                    for (String word : wordsSection) {
                        word = word.toLowerCase().replaceAll("^\\W+","")
                                .replaceAll("\\W+$", "").replaceAll("[0-9]", "");
                        if(word.equals(""))
                            continue;
                        curWord = new Term(word);
                        boolean hasKey = termDoc.containsKey(curWord.getWord());
                        if (hasKey)
                            termDoc.get(curWord.getWord()).add(bookName);
                        else {
                            curWord = new Term(word);
                            curWord.addBook(bookName);
                            termDoc.put(curWord.getWord(), curWord.getBookSet());
                        }
                    }
                }
            } catch (ReadingException | OutOfPagesException e) {
                e.printStackTrace();
            }
        }
    }
    public void writeTermDoc(String fileName) {
        new File("src/main/additional_files/").mkdirs();
        File dictionary = new File("src/main/additional_files/" + fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dictionary));
        ) {
            bufferedWriter.write(String.valueOf(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDictionaryForFolders(File folder) {
        for (File book : Objects.requireNonNull(folder.listFiles()))
            createTermDoc(book.getPath());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (Map.Entry<String, Set<String>> entry : termDoc.entrySet())
            sb.append(entry.getKey()).append('\n');
        return sb.append("\n}").toString();
    }
}