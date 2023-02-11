package kma.ir.kucherenko;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;

import java.io.File;
import java.util.*;

public class Dictionary {
    private final HashMap<Word, List<String>> words;
    private final Reader reader;

    public Dictionary() {
        words = new HashMap<>();
        reader = new Reader();
        // Optional, to return the tags-excluded version.
        reader.setIsIncludingTextContent(true);
    }

    public Dictionary(String filePath) {
        words = new HashMap<>();
        reader = new Reader();
        // Optional, to return the tags-excluded version.
        reader.setIsIncludingTextContent(true);
        createDictionary(filePath);
    }

    public void createDictionary(String filePath) {
        final File folder = new File(filePath);
        if (folder.listFiles() != null)
            createDictionaryForFolders(folder);
        else {
            String bookName = filePath.replace("src/main/books/", "").replace('_', ' ');
            try {
                reader.setFullContent(filePath); // Must call before readSection.
                int numOfPages = reader.getToc().getNavMap().getNavPoints().size();
                BookSection bookSection = null;
                for (int pageIndex = 1; pageIndex < numOfPages; pageIndex++) {
                    bookSection = reader.readSection(pageIndex);
                    String sectionTextContent = bookSection.getSectionTextContent(); // Excludes html tags.
//                    sectionTextContent.split("\\s");
                    sectionTextContent.split("\\s");
                    // Split the line into words
                    String[] wordsSection = sectionTextContent.split("\\W"); // TODO: correct the regexp

                    Word curWord = null;
                    for (String word : wordsSection) {
                        word = word.toLowerCase();
                        curWord = new Word(word);
                        curWord.addBook(bookName);
                        words.putIfAbsent(curWord, curWord.getBookList());
//                        if (words.putIfAbsent(curWord) && !curWord.getBookList().contains(bookName)) { // add method to get that Word obj and work with its data
//                            System.out.println(words.get(curWord));
//                            curWord.addBook(bookName);
//                        } else {
//                            curWord = new Word(word);
//                            curWord.addBook(bookName);
//                            words.put(curWord, curWord.getBookList());
//                        }
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
            createDictionary(book.getPath());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        for (Map.Entry<Word, List<String>> entry : words.entrySet())
            sb.append(entry.getKey().getWord()).append("-").append(entry.getValue()).append('\n');
        return sb.append("\n}").toString();
    }

//    public void writeToFile(String fileName) {
//        StringBuilder sb = new StringBuilder();
//        File dictionary = new File("src/main/dictionaries/" + fileName);
//        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dictionary));
//        ) {
//            bufferedWriter.write(words.get(fileName));
//            System.out.println("Successfully written to file");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}