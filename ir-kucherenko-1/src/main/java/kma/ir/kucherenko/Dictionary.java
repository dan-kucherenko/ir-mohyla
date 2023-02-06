package kma.ir.kucherenko;

import com.github.mertakdut.BookSection;
import com.github.mertakdut.Reader;
import com.github.mertakdut.exception.OutOfPagesException;
import com.github.mertakdut.exception.ReadingException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Dictionary {
    private Byte size;
    private String[] words = new String[0];
    private int wordsNum = 0;
    private int uniqueWords = 0;
    private int repeatedWords = 0;

    public void setWords(String[] words) {
        this.words = words;
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

    private boolean contains(String word) {
        for (String w : words) {
            if (w.equals(word))
                return true;
        }
        return false;
    }

    public void createDictionary(String filePath) {
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
                for (String word : wordsSection) {
                    if (!contains(word)) {
                        uniqueWords++;
                        addWord(word);
                    } else
                        repeatedWords++;
                }
            }
            System.out.println("Dictionary for " + filePath.replace("src/main/books/", "") + " has been created");
        } catch (ReadingException | OutOfPagesException e) {
            e.printStackTrace();
        }
        Arrays.sort(words);
        wordsNum = uniqueWords + repeatedWords;
    }

    private void addWord(String word) {
        String[] newWords = new String[words.length + 1];
        System.arraycopy(words, 0, newWords, 0, words.length);
        newWords[words.length] = word;
        setWords(newWords);
    }

    public void writeToFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/dictionaries/" + fileName));
        ) {
            for (String word : words)
                sb.append(word);
            bufferedWriter.write(sb.toString());
            System.out.println("Successfully written to file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
