package kma.ir.kucherenko.compressors.dictionary;

import kma.ir.kucherenko.TermDocument;

import java.io.*;

public class DictionaryCompressor {
    TermDocument termDocument = null;

    public DictionaryCompressor(String filePath) {
        termDocument = new TermDocument(filePath);
    }

    public void frontWrapping(String filePath) {
        String[] blockTerms = null;
        int blockIterator = 0;
        String firstTerm = "";
        try (BufferedWriter frontCodingWriter = new BufferedWriter(new FileWriter("src/main/additional_files/" + filePath))) {
            for (String term : termDocument.getTermDoc().keySet()) {
                StringBuilder sb = new StringBuilder();
                if (blockIterator == 0) { // create new blockTerms
                    blockTerms = new String[5];
                    firstTerm = term;
                }
                if (blockIterator < 5) { // add 5 sorted terms
                    if (termDocument.getTermDoc().get(term) == null)
                        break;
                    blockTerms[blockIterator] = term;
                    blockIterator++;
                }
                if (blockIterator == blockTerms.length) {
                    int blockLength = 5;
                    // get a size of common part of the word
                    int commonPartSize = getSizeOfCommonPart(firstTerm, blockTerms, blockLength);
                    writeCoded(blockTerms, sb, firstTerm, commonPartSize, blockLength);
                    frontCodingWriter.write(sb + "\n");
                    frontCodingWriter.flush();
                    blockIterator = 0;
                }
            }
            frontCodingWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public void frontDecoding(String inputFilePath, String outputFilePath) {
        StringBuilder decoded = new StringBuilder();
        String encodedString;
        try (BufferedReader frontCodingReader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter decodedTermsWriter = new BufferedWriter(new FileWriter(outputFilePath))) {
            while ((encodedString = frontCodingReader.readLine()) != null) {
                int i = 0;
                while (Character.isDigit(encodedString.charAt(i)))
                    i++;
                int prefixLength = Integer.parseInt(encodedString.substring(0, i));
                String prefix = encodedString.substring(i, i + prefixLength);
                i += prefixLength + 1;
                while (i < encodedString.length()) {
                    int suffixLengthStart = i;
                    int suffixLength;
                    if (Integer.parseInt(String.valueOf(encodedString.charAt(suffixLengthStart))) == 0)
                        suffixLength = 0;
                    else {
                        while (Character.isDigit(encodedString.charAt(i)))
                            i++;
                        suffixLength = Integer.parseInt(encodedString.substring(suffixLengthStart, i));
                    }
                    if (suffixLength == 0) {
                        decoded.append(prefix);
                        i++;
                    } else {
                        decoded.append(prefix).append(encodedString, i,  i + suffixLength);
                        i += suffixLength;
                    }
                    decodedTermsWriter.write(decoded.toString() + '\n');
                    decoded.setLength(0); // clear the StringBuilder for next iteration
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getSizeOfCommonPart(String firstTerm, String[] blockTerms, int blockLength) {
        boolean flag = true;
        int commonPartSize = 0;
        for (int i = 0; i < firstTerm.length(); i++) {
            for (int j = 0; j < blockLength; j++) {
                if (firstTerm.charAt(i) != blockTerms[j].charAt(i) || blockTerms[j].length() < i) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                commonPartSize++;
            else
                break;
        }
        return commonPartSize;
    }

    private void writeCoded(String[] blockTerms, StringBuilder sb, String firstTerm, int commonPartSize,
                            int blockLength) {
        sb.append(firstTerm.substring(0, commonPartSize).length()).append(firstTerm, 0, commonPartSize).append('*');
        for (int i = 0; i < blockLength; i++)
            sb.append(blockTerms[i].substring(commonPartSize).length()).append(blockTerms[i].substring(commonPartSize));
    }
}
