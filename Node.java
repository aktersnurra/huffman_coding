package huffman_coding;

import java.io.Serializable;

/**
 * @author Gustaf Rydholm
 * @author Addi Djikic
 *         CELTE13
 *         DD1332
 *         2016-02-25
 */

public class Node implements Serializable {
    char symbol;            //A byte in the uncompressed file.
    int weight;             //The weight of the byte in the file.

    Node left, right;       //Children of the current node.

    //Constructor for the leaf.
    Node(char symbol, int weight) {
        this.symbol = symbol;
        this.weight = weight;
        this.left = null;
        this.right = null;
    }

    //Constructor for a branch node.
    Node(int weight) {
        this.weight = weight;
        this.left = null;
        this.right = null;
    }

    int getWeight() {
        return weight;
    }

    //Checks if the current node is a leaf.
    boolean isLeaf() {
        return left == null && right == null;
    }

    char getSymbol() {
        return symbol;
    }

    public String toString() {
        String node = "(" + symbol + ", " + weight + ")";
        return node;
    }
}
