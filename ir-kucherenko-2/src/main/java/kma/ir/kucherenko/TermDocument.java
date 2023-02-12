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
    private int[][] incidenceMatrix;
    private final Reader reader;
    private final String path;

    public TermDocument() {
        termDoc = new TreeMap<>();
        reader = new Reader();
        path = "";
        // Optional, to return the tags-excluded version.
        reader.setIsIncludingTextContent(true);
    }

    public TermDocument(String filePath) {
        termDoc = new TreeMap<>();
        reader = new Reader();
        this.path = filePath;
        // Optional, to return the tags-excluded version.
        reader.setIsIncludingTextContent(true);
        createTermDoc(path);
    }

    public void createTermDoc(String filePath) {
        File folder = new File(filePath);
        if (folder.listFiles() != null)
            createDictionaryForFolders(folder);
        else {
            BookSection bookSection = null;
            Term curWord = null;
            String bookName = filePath.replace("src\\main\\books\\", "");
            try {
                reader.setFullContent(filePath); // Must call before readSection.
                int numOfPages = reader.getToc().getNavMap().getNavPoints().size();
                for (int pageIndex = 1; pageIndex < numOfPages; pageIndex++) {
                    bookSection = reader.readSection(pageIndex);
                    String sectionTextContent = bookSection.getSectionTextContent(); // Excludes html tags.
                    // Split the line into words
                    String[] wordsSection = sectionTextContent.split("\\W"); // TODO: correct the regexp

                    for (String word : wordsSection) {
                        word = word.toLowerCase();
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
                System.out.println("Dictionary for " + bookName + " has been created");
            } catch (ReadingException | OutOfPagesException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDictionaryForFolders(File folder) {
        for (File book : Objects.requireNonNull(folder.listFiles()))
            createTermDoc(book.getPath());
    }

    public void createIncidenceMatrix() {
        File[] books = new File(path).listFiles();
        String[] termsKeys = termDoc.keySet().toArray(new String[0]);
        incidenceMatrix = new int[termDoc.size()][books.length];
        for (int i = 0; i < termsKeys.length; i++) {
            for (int j = 0; j < Objects.requireNonNull(books).length; j++) {
                if (termDoc.get(termsKeys[i]).contains(books[j].getName()))
                    incidenceMatrix[i][j] = 1;
                else
                    incidenceMatrix[i][j] = 0;
            }
        }
    }

    public void writeIncidenceMatrix(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < incidenceMatrix.length; i++)
            sb.append(Arrays.toString(incidenceMatrix[i])).append('\n');
        sb.append("\n]");
        new File("src/main/additional_files/").mkdirs();
        File dictionary = new File("src/main/additional_files/" + fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dictionary));
        ) {
            bufferedWriter.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (Map.Entry<String, Set<String>> entry : termDoc.entrySet())
            sb.append(entry.getKey()).append(" - ").append(entry.getValue()).append('\n');
        return sb.append("\n}").toString();
    }

    public void writeTermDoc(String fileName) {
        new File("src/main/additional_files/").mkdirs();
        File dictionary = new File("src/main/additional_files/" + fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dictionary));
        ) {
            bufferedWriter.write(String.valueOf(this));
            System.out.println("Successfully written to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}