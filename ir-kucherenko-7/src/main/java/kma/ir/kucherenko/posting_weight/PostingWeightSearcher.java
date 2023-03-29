package kma.ir.kucherenko.posting_weight;

import com.kursx.parser.fb2.FictionBook;
import com.kursx.parser.fb2.Person;
import kma.ir.kucherenko.spimi.Spimi;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PostingWeightSearcher {
    private final static String COLLECTION_PATH = "src/main/collection";
    private final File[] documents;
    private final static String SPIMI_RES = "src/main/additional_files/spimi_result";

    public PostingWeightSearcher() {
        this.documents = new File(COLLECTION_PATH).listFiles();
        Spimi spimi = new Spimi(COLLECTION_PATH);
        spimi.executeSPIMI();
    }

    public String findDocuments(String query) {
        List<PostingWeight> result = new LinkedList<>();
        for (String word : query.split(" ")) {
            String searchWord = word.toLowerCase().trim();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(SPIMI_RES))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] split = line.split(":");
                    if (Objects.equals(searchWord, split[0])) {
                        String[] postings = split[1].replace('[', ' ').replace(']', ' ').split(",");
                        for (String posting : postings) {
                            int bookId = Integer.parseInt(posting.trim());
                            FictionBook book = new FictionBook(documents[bookId - 1]);
                            PostingWeight postingDocument = new PostingWeight(bookId, Scores.CONTENT);
                            checkTitle(book, searchWord, postingDocument);
                            checkAuthors(book, searchWord, postingDocument);
                            result.add(postingDocument);
                        }
                        break;
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Resulting list with weight: " + result);
        Map<Integer, Double> resMap = sumThePostings(result);
        return convertKeyToTitle(getMaxKey(resMap));
    }

    private void checkTitle(FictionBook book, String searchWord, PostingWeight postingDocument) {
        String[] titleArr = book.getTitle().split(" ");
        for (String title : titleArr) {
            if (title.toLowerCase().contains(searchWord))
                postingDocument.addZoneWeight(Scores.TITLE);
        }
    }

    private void checkAuthors(FictionBook book, String searchWord, PostingWeight postingDocument) {
        ArrayList<Person> authorArr = book.getAuthors();
        for (Person author : authorArr) {
            if (author.getFullName().toLowerCase().contains(searchWord))
                postingDocument.addZoneWeight(Scores.AUTHOR);
        }
    }

    private Map<Integer, Double> sumThePostings(List<PostingWeight> list) {
        Map<Integer, Double> postingMap = new HashMap<>();
        for (PostingWeight postingWeight : list) {
            int currId = postingWeight.getId();
            double currPostingWeight = postingWeight.getWeight();
            if (postingMap.containsKey(currId)) {
                double currWeight = postingMap.get(currId);
                postingMap.put(currId, currPostingWeight + currWeight);
            } else
                postingMap.put(currId, currPostingWeight);
        }
        return postingMap;
    }

    private int getMaxKey(Map<Integer, Double> map) {
        double maxVal = -1;
        int maxKey = -1;
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            if (entry.getValue() > maxVal) {
                maxVal = entry.getValue();
                maxKey = entry.getKey();
            }
        }
        return maxKey;
    }

    private String convertKeyToTitle(int key) {
        return documents[key-1].getName();
    }
}
