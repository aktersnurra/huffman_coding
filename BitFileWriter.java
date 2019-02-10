package huffman_coding;

import java.io.*;

/**
 * @author Gustaf Rydholm
 * @author Addi Djikic
 *         CELTE13
 *         DD1332
 *         2016-02-25
 */
public class BitFileWriter {
    private static BufferedOutputStream out;        //Output stream in order to write the de/compressed file.
    private static ObjectOutputStream oos;          //Output stream for root of the Huffman trie.

    private int buffer;                             //Contains the bits of the Huffman code that will be written to file.
    private int bitsLeft;                           //The number of bits left before the buffer contains 8 bits.

    /**
     * Creates a file with the file name.
     *
     * @param fileName the name of the file.
     */
    void openFile(String fileName) {
        String path = fileName;
        try {
            out = new BufferedOutputStream(new FileOutputStream(
                    path));
        } catch (FileNotFoundException e) {
            System.out.println("Could not create file.");
            e.printStackTrace();
        }
    }

    /**
     * Write the decompressed byte to file.
     *
     * @param b the original byte.
     */
    void writeDecompressed(char b) {
        try {
            out.write((byte) b);
        } catch (IOException e) {
            System.out.println("Could not write " + b + " to file.");
            e.printStackTrace();
        }
    }

    void closeFile() {
        try {
            out.close();
        } catch (IOException e) {
            System.out.println("Could not close the decompressed file.");
            e.printStackTrace();
        }
    }

    /**
     * Writes the Huffman code for each byte to file.
     *
     * @param huffmanCode the path in the trie to a byte.
     */
    void writePath(String huffmanCode) {
        for (int i = 0; i < huffmanCode.length(); i++) {
            if (huffmanCode.charAt(i) == '0') {
                writeBit(false);
            } else if (huffmanCode.charAt(i) == '1') {
                writeBit(true);
            } else throw new IllegalStateException("Illegal state");
        }
    }

    /**
     * Writes the root of the Huffman trie to file.
     *
     * @param root is the root of the Huffman trie.
     */
    void writeTrie(Node root) {
        try {
            oos = new ObjectOutputStream(new FileOutputStream(
                    "huffTrieRoot.bin"));
            oos.writeObject(root);
            oos.close();
        } catch (IOException e) {
            System.err.println("Could not write the Huffman trie root to file.");
            e.printStackTrace();
        }

    }


/**
 * -------------------------------------------------------------------------
 * <p>
 *                      Methods below are taken from:
 *      http://algs4.cs.princeton.edu/55compression/BinaryStdOut.java.html
 * <p>
 * -------------------------------------------------------------------------
 */

    /**
     * Write the specified bit to standard output.
     */
    private void writeBit(boolean bit) {
        // add bit to buffer
        buffer <<= 1;

        //If bit = true, the current LSB will be assigned to 1.
        if (bit) buffer |= 1;

        // if buffer is full (8 bits), write out as a single byte
        bitsLeft++;
        if (bitsLeft == 8) clearBuffer();
    }

    // write out any remaining bits in buffer to standard output, padding with 0s
    private void clearBuffer() {
        if (bitsLeft == 0) return;
        if (bitsLeft > 0) buffer <<= (8 - bitsLeft);
        try {
            out.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitsLeft = 0;
        buffer = 0;
    }

    /**
     * Flush standard output, padding 0s if number of bits written so far
     * is not a multiple of 8.
     */
    public void flush() {
        clearBuffer();
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Flush and close standard output. Once standard output is closed, you can no
     * longer write bits to it.
     */
    public void close() {
        flush();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}