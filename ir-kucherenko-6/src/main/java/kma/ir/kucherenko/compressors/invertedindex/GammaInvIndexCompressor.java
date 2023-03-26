package kma.ir.kucherenko.compressors.invertedindex;

import java.io.*;

public class GammaInvIndexCompressor {
    public void createCompressedInvIndex(File sourceIndex, File resultIndex) {
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceIndex));
             DataOutputStream writerBuff = new DataOutputStream(new FileOutputStream(resultIndex))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] twoParts = line.split(":");
                String[] docs = twoParts[1].trim().replace("[", "").replace("]", "").split(",");
                writerBuff.writeUTF(twoParts[0] + ':');
                int difference = 0;
                for (int i = 0; i < docs.length; i++) {
                    if (i == 0)
                        difference = Integer.parseInt(docs[i]) - difference;
                    else
                        difference = Integer.parseInt(docs[i].trim()) - Integer.parseInt(docs[i - 1].trim());
                    String gammaEncodedDiff = getGammaCode(difference);
                    byte[] encodedDiff = getByteArray(gammaEncodedDiff);
                    writerBuff.write(encodedDiff);
                }
                writerBuff.write('\n');
                writerBuff.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getByteArray(String code) {
        int len = (code.length() + 7) / 8;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            int from = i * 8;
            int to = Math.min(from + 8, code.length());
            String byteStr = code.substring(from, to);
            byte b = (byte) Integer.parseInt(byteStr, 2);
            bytes[i] = b;
        }
        return bytes;
    }

    private String getGammaCode(int gap) {
        String binaryGap = Integer.toBinaryString(gap);
        String gapOffset = binaryGap.substring(1);
        int length = gapOffset.length();
        StringBuilder unaryCode = new StringBuilder();
        while (length > 0) {
            unaryCode.append("1");
            length--;
        }
        unaryCode.append("0");
        return unaryCode + gapOffset;
    }
}
