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

public class Dictionary {
    private final Reader reader;
    private final StringBuilder resWriter;
    private long collectionSize;
    private long dictionarySize;
    private String[] words;
    private int wordsNum = 0;
    private int uniqueWords = 0;
    private int repeatedWords = 0;

    public Dictionary(String filePath) {
        this.words = new String[100];
        this.reader = new Reader();
        this.resWriter = new StringBuilder();
        reader.setIsIncludingTextContent(true); // Optional, to return the tags-excluded version.
        createDictionary(filePath);
    }

    public int getRepeatedWords() {
        return repeatedWords;
    }

    public int getUniqueWords() {
        return uniqueWords;
    }

    public int getWordsNum() {
        return wordsNum;
    }

    public long getCollectionSize() {
        return collectionSize;
    }

    public long getDictionarySize() {
        return dictionarySize;
    }

    public void createDictionary(String filePath) {
        final File folder = new File(filePath);
        if (folder.listFiles() != null) {
            createDictionaryFolder(folder);
        } else {
            BookSection bookSection;
            try {
                reader.setFullContent(filePath); // Must call before readSection.
                final int pagesNum = reader.getToc().getNavMap().getNavPoints().size();
                for (int pageIndex = 1; pageIndex < pagesNum; pageIndex++) {
                    bookSection = reader.readSection(pageIndex);
                    String sectionTextContent = bookSection.getSectionTextContent(); // Excludes html tags.
                    // Split the line into words
                    String[] wordsSection = sectionTextContent.split("\\W");
                    addUniqueWords(wordsSection);
                }
                System.out.println("Dictionary for " + filePath + " has been created");
            } catch (ReadingException | OutOfPagesException e) {
                e.printStackTrace();
            }
            wordsNum = uniqueWords + repeatedWords;
        }
    }

    public void writeToFile(String fileName) {
        File dictionary = new File("src/main/dictionaries/" + fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dictionary))
        ) {
            bufferedWriter.write(resWriter.toString());
            System.out.println("Successfully written to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
        dictionarySize = dictionary.length();
    }

    private void createDictionaryFolder(File folder) {
        for (File book : Objects.requireNonNull(folder.listFiles())) {
            collectionSize += book.length();
            createDictionary(book.getPath());
        }
    }

    private void addWordToArr(String word) {
        words[uniqueWords++] = word;
        if (uniqueWords == words.length) {
            String[] newWords = new String[words.length * 2];
            System.arraycopy(words, 0, newWords, 0, words.length);
            setWords(newWords);
        }
    }

    private void setWords(String[] words) {
        this.words = words;
    }

    private void addUniqueWords(String[] wordsSection) {
        for (String word : wordsSection) {
            word = word.toLowerCase();
            if (!Arrays.asList(words).contains(word)) {
                resWriter.append(word).append('\n');
                addWordToArr(word);
            } else
                repeatedWords++;
        }
    }
}