package utils;

/**
 * Created by Matthieu on 27/11/2017.
 */
public class FileGenerator {

    public byte[] generateFile(int length){
        int totalSize = (Integer.valueOf(length) * 1048576) / 4;
        byte[] out = new byte[totalSize];
        for (int i = 0; i < totalSize; i++) {
            out[i] = (byte) 1;
        }
        return out;
    }
}
