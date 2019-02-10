package huffman_coding;

import java.io.IOException;

/**
 * @author Gustaf Rydholm
 * @author Addi Djikic
 *         CELTE13
 *         DD1332
 *         2016-02-25
 */
public class Encoder {

    BitFileReader bitFileReader;
    HuffmanCoding huffmanCoding;
    PriorityQueueHuffman priorityQueueHuffman;

    Encoder(String[] args) {
        final long startTime = System.currentTimeMillis();

        try {
            String pathToString = args[0];               //Name of the file that's going to be compressed.
            try {
                bitFileReader = new BitFileReader(pathToString);
                priorityQueueHuffman = new PriorityQueueHuffman(bitFileReader);
                huffmanCoding = new HuffmanCoding(bitFileReader, priorityQueueHuffman);
            } catch (IOException e) {
                System.err.println("Could not open file.");
                e.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage: java Decoder -name of file to compress-");
            e.printStackTrace();
        }

        final long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time to compile: " + elapsedTime / 1000 + " s");
    }

    public static void main(String[] args) throws IOException {
        new Encoder(args);
    }

}
