package huffman_coding;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Gustaf Rydholm
 * @author Addi Djikic
 *         CELTE13
 *         DD1332
 *         2016-02-25
 */

public class BitFileReader {
    private static final int EOF = -1;          //end of file.
    private static final int maxByte = 256;     //Maximum size of a byte.

    private BufferedInputStream is;             //the input stream.
    private static ObjectInputStream ois;       //the input stream of the trie root.

    private int buffer;                         //current byte read from file.

    private String pathToFile;                  //Path to the file.
    private String pathToTrie;                  //Path to the huffman trie.

    private int bitIndex;                       //Index of the bit in the array.
    private int bitsLeft;                       //Number of bits left in the current byte.
    private boolean[] bitArray;                 //Array of bits read from file.
    boolean bit;                                //Current bit the the byte.
    int[] weightArray;                          //Array for the weight of each byte.
    char[] inputArray;                          //The input of the file as chars.
    Map<Character, String> mapOfTriePath;       //Path to the leaf in the Huffman trie.
    Set<Character> setOfBytes;                  //A set of all bytes in the input file.
    Node trieRoot;                              //Root of the Huffman trie.


    /**
     * Constructor for the encoding.
     *
     * @param pathToFile path name to the file that is going to be compressed.
     * @throws IOException taken care of in the Encoding constructor.
     */
    BitFileReader(String pathToFile) throws IOException {
        this.pathToFile = pathToFile;
        reader();
        getWeight();
        close();
    }

    /**
     * Constructor for the decoding.
     *
     * @param pathToFile path to the compressed file.
     * @param pathToTrie path to the root of the Huffman trie.
     * @throws IOException handled in the decoding constructor.
     */
    BitFileReader(String pathToFile, String pathToTrie) throws IOException {
        this.pathToFile = pathToFile;
        this.pathToTrie = pathToTrie;
        reader();
        makeByteArray();
        readTrie();
        close();
    }

    /**
     * Fill the buffer with the next byte in the file.
     */
    private void nextBuffer() {
        try {
            buffer = is.read();
        } catch (IOException e) {
            System.err.println("End Of File.");
            buffer = EOF;
        }
    }

    /**
     * Reads the file, fills the buffer with the first byte in the file.
     * readFile returns the input as a String.
     * inputArray - a <tt>char</tt> array of the stringInput.
     *
     * @throws IOException
     */
    private void reader() throws IOException {
        is = new BufferedInputStream(new FileInputStream(pathToFile));
        nextBuffer();
        String stringInput = readFile();
        inputArray = stringInput.toCharArray();
    }


    /**
     * ----------------------------------------------------
     * <p>
     * Methods for encoding.
     * <p>
     * ----------------------------------------------------
     */

    /**
     * Calculates the weight of each byte in the file.
     */
    private void getWeight() {
        weightArray = new int[maxByte];
        for (int i = 0; i < inputArray.length; i++) {
            weightArray[inputArray[i]]++;
        }
    }

    /**
     * Checks if the input is empty.
     * Adds the current buffer to currentByte, then calls for the update of the buffer.
     *
     * @return a char between the value of 0 and 255.
     */
    private char readByte() {
        if (isEmpty()) throw new RuntimeException("The input stream is empty.");
        int currentByte = buffer;
        nextBuffer();
        return (char) (currentByte & 0xff);
    }

    /**
     * Reads through the file and returns a string of the whole input.
     *
     * @return a string of the whole file.
     */
    private String readFile() {
        setOfBytes = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        while (!isEmpty()) {
            char currentByte = readByte();
            setOfBytes.add(currentByte);
            sb.append(currentByte);
        }
        return sb.toString();
    }

    /**
     * ----------------------------------------------------
     * <p>
     * Methods for decoding.
     * <p>
     * ----------------------------------------------------
     */

    /**
     * Reads a file containing the root of the Huffman trie.
     *
     * @throws IOException handled in the constructor of the Decoder.
     */
    private void readTrie() throws IOException {
        ois = new ObjectInputStream(new FileInputStream(pathToTrie));
        try {
            trieRoot = (Node) ois.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("The root file does not contain a object of type 'Node'");
            e.printStackTrace();
        }
        ois.close();
    }

    /**
     * Takes each element in the set and decodes the byte to a string of 1's and 0's.
     *
     * Local because of JUNIT test.
     */
    void makeByteArray() {
        mapOfTriePath = new HashMap<>();
        for (char element : setOfBytes) {
            bitsLeft = 8;
            bitIndex = 0;
            bitArray = new boolean[8];
            buffer = element;
            while (bitsLeft >= 1) {
                readBit();
                makeBitArray(element);
            }
        }
    }

    /**
     * Adds each <tt>boolean</tt> bit to the bitArray. If the bitArray contains all 8 bits it will convert the
     * <tt>boolean</tt> Array to a string, then put the byte (<tt>char</tt>) as key
     * and the value as the string of 1s and 0s in the <tt>HashMap</tt> mapOfTriePath.
     *
     * @param element a byte in the file.
     */
    private void makeBitArray(char element) {
        String triePath = "";
        bitArray[bitIndex] = bit;
        bitIndex++;
        if (bitsLeft == 0) {
            if (bitArray != null) {
                for (boolean b : bitArray) {
                    triePath = triePath.concat(b == true ? "1" : "0");
                }
                mapOfTriePath.put(element, triePath);
            }
        }

    }

    /**
     * Calculates all the bits as 1s and 0s of the current byte.
     *
     * @return a specific bit in the byte represented as a <tt>boolean</tt> value.
     */
    private boolean readBit() {
        if (isEmpty()) throw new RuntimeException("The input stream is empty.");
        bitsLeft--;
        // Right shifts the buffer with the number of bits left, then a bitwise and operation with 1.
        // returns true if == 1.
        bit = ((buffer >> bitsLeft) & 1) == 1;
        return bit;
    }


    private boolean isEmpty() {
        return buffer == EOF;
    }

    private void close() throws IOException {
        is.close();
    }

}