package kma.ir.kucherenko.spimi;

import java.util.ArrayList;
import java.util.List;

public class SpimiTerm implements Comparable<SpimiTerm> {
    private String term;
    private List<Integer> postingsList;

    public SpimiTerm(String term, List<Integer> postingsList) {
        super();
        this.term = term;
        this.postingsList = postingsList;
    }

    public SpimiTerm(String str) {
        String[] groups = str.split(" ");
        String[] docIDs = groups[1].split(",");

        List<Integer> docIDarr = new ArrayList<>();

        for (String docID : docIDs) {
            docIDarr.add(Integer.parseInt(docID));
        }

        this.term = groups[0];
        this.postingsList = docIDarr;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<Integer> getPostingsList() {
        return postingsList;
    }

    public void setPostingsList(List<Integer> postingsList) {
        this.postingsList = postingsList;
    }

    @Override
    public int compareTo(SpimiTerm o) {
        return this.term.compareTo(o.term);
    }
}
