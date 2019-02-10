package huffman_coding;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gustaf Rydholm
 * @author Addi Djikic
 *         CELTE13
 *         DD1332
 *         2016-02-25
 */
public class HuffmanCoding {
    private PriorityQueueHuffman priorityQueueHuffman;
    private BitFileReader bitFileReader;
    private BitFileWriter bitFileWriter;

    private Node root, nextNode;                            //Node for the Huffman trie.
    private Map<Character, Integer> decodingMap;            //The key is a byte and the value is the current weight of the byte.
    private Map<Character, String> pathMap;                 //The key is the byte and the value is the path in the trie to the byte.
    private String nameOfDecodedFile;                       //Name for the decoded file.


    //Constructor for compressing a file
    HuffmanCoding(BitFileReader bitFileReader, PriorityQueueHuffman priorityQueueHuffman) {
        this.priorityQueueHuffman = priorityQueueHuffman;
        this.bitFileReader = bitFileReader;
        this.bitFileWriter = new BitFileWriter();
        pathMap = new HashMap<>();
        buildHuffmanTrie();
        buildCodes();
        writeCompressedOutput();
    }


    //Constructor for decompressing a file.
    HuffmanCoding(BitFileReader bitFileReader, String nameOfDecodedFile) {
        this.bitFileReader = bitFileReader;
        this.bitFileWriter = new BitFileWriter();
        this.root = bitFileReader.trieRoot;
        this.nameOfDecodedFile = nameOfDecodedFile;
        decodingMap = new HashMap<>();
        decodeHuffmanTrie();
    }

    /**
     * ----------------------------------------------------
     *
     *               Methods for building the trie.
     *
     * ----------------------------------------------------
     */

    /**
     * Builds the Huffman trie via a priority queue, where the final element in the queue
     * is the root of the trie.
     */
    private void buildHuffmanTrie() {
        Node i, j;
        int n = priorityQueueHuffman.cardinalOfPQ() - 1, newWeight;
        for (int k = 0; k < n; k++) {
            i = priorityQueueHuffman.pop();
            j = priorityQueueHuffman.pop();

            newWeight = i.getWeight() + j.getWeight();
            Node newNode = new Node(newWeight);
            newNode.right = i;
            newNode.left = j;

            priorityQueueHuffman.push(newNode);
        }
        root = priorityQueueHuffman.pop();
        bitFileWriter.writeTrie(root);
    }

    /**
     * Initialization of the code building for each byte.
     */
    private void buildCodes() {
        if (root == null) return;
        buildCodes(root, "");
    }

    /**
     * Recursive method that build the Huffman coding for each byte in the file.
     *
     * @param node        a node in the Huffman trie.
     * @param currentPath the current calculated path taken in the trie.
     */
    private void buildCodes(Node node, String currentPath) {
        if (node == null) return;

        //If a leaf is found, the byte and the path is put as key and value in the pathMap.
        if (node.isLeaf()) {
            pathMap.put(node.getSymbol(), currentPath);

        }

        else {
            buildCodes(node.left, currentPath + "0");
            buildCodes(node.right, currentPath + "1");
        }
    }

    /**
     * Writes the Huffman coding of each byte to a file called compressed.bin.
     */
    private void writeCompressedOutput() {
        bitFileWriter.openFile("compressed.bin");
        for (char b : bitFileReader.inputArray) {
            //Takes each bytes in the input array and gets it's Huffman coding from the pathMap.
            String pathInTrie = pathMap.get(b);
            bitFileWriter.writePath(pathInTrie);
        }
        bitFileWriter.close();
    }

    /**
     * ----------------------------------------------------
     *
     *               Methods for decoding the trie.
     *
     * ----------------------------------------------------
     */


    /**
     * Decodes the Huffman trie by going down through it bit by bit of each byte from the input file.
     * Open and closes the file which contains the decompressed bytes.
     */
    private void decodeHuffmanTrie() {
        bitFileWriter.openFile(nameOfDecodedFile);
        nextNode = root;
        for (char b : bitFileReader.inputArray) {
            //A string containing 1' s and 0's so we can go through the Huffman trie (the byte represented as a binary string).
            String binaryStr = bitFileReader.mapOfTriePath.get(b);
            for (int i = 0; i < binaryStr.length(); i++) {

                if (binaryStr.charAt(i) == '0') {
                    nextNode = nextNode.left;
                    checkNode(nextNode);
                }

                else if (binaryStr.charAt(i) == '1') {
                    nextNode = nextNode.right;
                    checkNode(nextNode);
                }
            }
        }
        bitFileWriter.closeFile();
    }

    /**
     * Checks if the current node is a leaf in the Huffman trie, if so the byte (symbol) in the node
     * written to the decompressed file.
     *
     * @param node the current node in the Huffman trie.
     */
    private void checkNode(Node node) {
        int currentWeight = 0; //Initilize the current weight of the byte.

        if (node.isLeaf()) {

            char key = node.getSymbol(); //The byte of the node, which serves a key in the decodingMap

            //Checks key is not in the map. Otherwise the current weight of the byte is compared to the total weight of the byte.
            //This is done in order to prevent that a byte is printed out more times than it should.
            if (!decodingMap.containsKey(key) || decodingMap.get(key) < node.getWeight()) {
                //Get the current weight if the byte is already in the map.
                if (decodingMap.containsKey(key)) currentWeight = decodingMap.get(key);
                currentWeight++;
                decodingMap.put(key, currentWeight);        //Update/put the key and current weight in the map.
                bitFileWriter.writeDecompressed(key);       //Write the decompressed byte to file.
            }
            nextNode = root;                                //If leaf, return to the root.
        }
    }
}
