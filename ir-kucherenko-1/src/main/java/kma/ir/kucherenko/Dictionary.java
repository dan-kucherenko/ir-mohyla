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
    private long collectionSize;
    private long dictionarySize;
    private String[] words = new String[1];
    private int wordsNum = 0;
    private int uniqueWords = 0;
    private int repeatedWords = 0;

    public Dictionary(String filePath) {
        createDictionary(filePath);
    }

    private void setWords(String[] words) {
        this.words = words;
    }

    private void addUniqueWords(String[] wordsSection) {
        for (String word : wordsSection) {
            if (!Arrays.asList(words).contains(word)) {
                uniqueWords++;
                addWordToArr(word);
            } else
                repeatedWords++;
        }
    }

    private void addWordToArr(String word) {
        if (uniqueWords == words.length) {
            String[] newWords = new String[words.length * 2];
            System.arraycopy(words, 0, newWords, 0, words.length);
            newWords[words.length] = word;
            setWords(newWords);
        }
        words[uniqueWords - 1] = word;
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
            for (File book : Objects.requireNonNull(folder.listFiles())) {
                collectionSize += book.length();
                createDictionary(filePath + '/' + book.getName());
            }
        } else {
            Reader reader = new Reader();
            reader.setIsIncludingTextContent(true); // Optional, to return the tags-excluded version.
            int pageIndex = 1;

            try {
                reader.setFullContent(filePath); // Must call before readSection.
                BookSection bookSection = null;
                while (pageIndex != reader.getToc().getNavMap().getNavPoints().size()) {
                    bookSection = reader.readSection(pageIndex);
                    String sectionTextContent = bookSection.getSectionTextContent(); // Excludes html tags.
                    pageIndex++;
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
        StringBuilder sb = new StringBuilder();
        String [] finalResult = new String[uniqueWords];
        System.arraycopy(words, 0, finalResult, 0, uniqueWords);
        Arrays.sort(finalResult);
        File dictionary = new File("src/main/dictionaries/" + fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dictionary));
        ) {
            for (String word : finalResult)
                sb.append(word).append('\n');
            bufferedWriter.write(sb.toString());
            System.out.println("Successfully written to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
        dictionarySize = dictionary.length();
    }
}