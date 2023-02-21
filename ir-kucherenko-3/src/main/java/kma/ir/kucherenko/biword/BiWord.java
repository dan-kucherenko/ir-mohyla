package kma.ir.kucherenko.biword;

public record BiWord(String firstWord, String secondWord) {

    @Override
    public boolean equals(Object o) {
        return o instanceof BiWord biWord &&
                biWord.firstWord.equals(firstWord) &&
                biWord.secondWord.equals(secondWord);
    }

    @Override
    public String toString() {
        return firstWord + ' ' + secondWord;
    }
}