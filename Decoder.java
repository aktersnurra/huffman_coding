package huffman_coding;

import java.io.IOException;

/**
 * @author Gustaf Rydholm
 * @author Addi Djikic
 *         CELTE13
 *         DD1332
 *         2016-02-25
 */
public class Decoder {

    BitFileReader bitFileReader;
    HuffmanCoding huffmanCoding;

    Decoder(String[] args) throws IOException {
        final long startTime = System.currentTimeMillis();
        try {
            String pathToCompression = args[0];
            String pathToTrieRoot = args[1];
            String nameOfDecodedFile = args[2];
            try {
                bitFileReader = new BitFileReader(pathToCompression, pathToTrieRoot);
                huffmanCoding = new HuffmanCoding(bitFileReader, nameOfDecodedFile);
            } catch (IOException e) {
                System.err.println("Usage: java Decoder -path to the compressed file- -path to the root- -name of the decompressed file-");
                System.err.println("Could not open file.");
                e.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: java Decoder -path to the compressed file- -path to the root- -name of the decompressed file-");
            e.printStackTrace();
        }

        final long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time to decompress: " + elapsedTime / 1000 + " s");
    }

    public static void main(String[] args) throws IOException {
        new Decoder(args);
    }

}
