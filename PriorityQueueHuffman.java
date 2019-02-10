package huffman_coding;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author Gustaf Rydholm
 * @author Addi Djikic
 *         CELTE13
 *         DD1332
 *         2016-02-25
 */

class PriorityQueueHuffman {
    BitFileReader bitFileReader;
    PriorityQueue<Node> priorityQueue;

    PriorityQueueHuffman(BitFileReader bitFileReader) {
        this.bitFileReader = bitFileReader;
        setPriorityQueue();
    }

    /**
     * Initializes the priority queue, which is sorted by the weight of the node. The <tt>Nodes</tt> in the queue
     * are sorted by their weights. The two <tt>Nodes</tt> with the smallest weights are popped.
     */
    void setPriorityQueue() {
        int weight;
        priorityQueue = new PriorityQueue<>(bitFileReader.setOfBytes.size(),
                (Comparator<Node>) (node1, node2) -> node1.getWeight() - node2.getWeight());

        for (char b : bitFileReader.setOfBytes) {
            weight = bitFileReader.weightArray[b];
            Node node = new Node(b, weight);
            priorityQueue.add(node);
        }

    }

    void push(Node node) {
        priorityQueue.add(node);
    }

    Node pop() {
        return priorityQueue.poll();
    }

    Node peek() {
        return priorityQueue.peek();
    }

    int cardinalOfPQ() {
        return priorityQueue.size();
    }

    boolean isEmpty() {
        return priorityQueue.size() == 0;
    }

}