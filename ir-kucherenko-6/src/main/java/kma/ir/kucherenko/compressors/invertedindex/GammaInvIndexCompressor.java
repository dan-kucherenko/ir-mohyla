package kma.ir.kucherenko.compressors.invertedindex;

import java.io.*;
import java.util.BitSet;

public class GammaInvIndexCompressor {
    public void createCompressedInvIndex(File sourceIndex, File resultIndex) {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceIndex));
             BufferedWriter writerBuff = new BufferedWriter(new FileWriter(resultIndex))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] twoParts = line.split(":");
                String[] docs = twoParts[1].replace("[", "").replace("]", "").split(",");
                writerBuff.write(twoParts[0] + ":");
                int difference = 0;
                for (int i = 0; i < docs.length; i++) {
                    if (i == 0)
                        difference = Integer.parseInt(docs[i]) - difference;
                    else
                        difference = Integer.parseInt(docs[i].trim()) - Integer.parseInt(docs[i - 1].trim());
                    byte[] byteArr = getByteArray(getGammaCode(difference));
                    for (int b : byteArr)
                        writerBuff.write(b);
                }
                writerBuff.write('\n');
                writerBuff.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getByteArray(String code) {
        BitSet bits = new BitSet(code.length());
        for (int i = 0; i < code.length(); i++)
            bits.set(i, code.charAt(i) == '1');
        return bits.toByteArray();
    }

    private String getGammaCode(int gap) {
        String binaryGap = Integer.toBinaryString(gap);
        String gapOffset = binaryGap.substring(1);
        int length = gapOffset.length();
        StringBuilder unaryCode = new StringBuilder();
        while (length > 0) {
            unaryCode.append("0");
            length--;
        }
        unaryCode.append("1");
        return unaryCode.toString() + gapOffset;
    }
}
