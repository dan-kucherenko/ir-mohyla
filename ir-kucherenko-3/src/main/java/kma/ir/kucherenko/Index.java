package kma.ir.kucherenko;

import com.github.mertakdut.Reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public abstract class Index <T1, T2> {
    protected Map<T1, T2> index;
    protected Reader reader;
    protected File[] booksFolder;

    public abstract void createIndex(String filePath);

    public void writeToFile(String fileName) {
        new File("src/main/additional_files/").mkdirs();
        File dictionary = new File("src/main/additional_files/" + fileName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dictionary))
        ) {
            bufferedWriter.write(String.valueOf(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String formatWord(String word) {
        return word.toLowerCase().replaceAll("^\\W+", "")
                .replaceAll("\\W+$", "");
    }

    protected int getNumOfPages() {
        return reader.getToc().getNavMap().getNavPoints().size();
    }

    protected void createDictionaryForFolders(File folder) {
        for (File book : Objects.requireNonNull(folder.listFiles()))
            createIndex(book.getPath());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<T1, T2> map : index.entrySet())
            sb.append(map.getKey()).append(':').append(map.getValue()).append('\n');
        return sb.toString();
    }
}
