package kma.ir.kucherenko.posting_weight;

import com.kursx.parser.fb2.Description;
import com.kursx.parser.fb2.DocumentInfo;
import com.kursx.parser.fb2.FictionBook;
import com.kursx.parser.fb2.Person;
import kma.ir.kucherenko.spimi.Spimi;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
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

    public List<PostingWeight> findDocuments(String word) {
        String searchWord = word.toLowerCase().trim();
        List<PostingWeight> result = new LinkedList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(SPIMI_RES))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(":");
                if (Objects.equals(searchWord, split[0])) {
                    String[] postings = split[1].replace('[', ' ').replace(']', ' ').split(",");
                    for (int i = 0; i < postings.length; i++) {
                        int bookId = Integer.parseInt(postings[i].trim());
                        FictionBook book = new FictionBook(documents[bookId - 1]);
                        PostingWeight postingDocument = new PostingWeight(bookId - 1, Scores.CONTENT);
                        checkTitle(book, searchWord, postingDocument);
                        checkAuthors(book, searchWord, postingDocument);
                        result.add(postingDocument);
                    }
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return result;
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
        for (int j = 0; j < authorArr.size(); j++) {
            Person author = authorArr.get(j);
            if (author.getFullName().toLowerCase().contains(searchWord))
                postingDocument.addZoneWeight(Scores.AUTHOR);
        }
    }

//    private void checkDescription(FictionBook book, String searchWord, PostingWeight postingDocument) {
//        Description bookDescription = book.getDescription();
//        DocumentInfo docInfo = bookDescription.getDocumentInfo();
//        docInfo.g
//        for (String title : titleArr) {
//            if (title.toLowerCase().equals(searchWord))
//                postingDocument.addZoneWeight(Scores.TITLE);
//        }
//    }
}
