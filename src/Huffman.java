import java.util.*;

public class Huffman {

    //Node class for nodes created in coding process
    static class Node{
        Integer frequency;
        Character character;
        Node left;
        Node right;

        public Node(Character character, Integer frequency){ // a leaf
            this.frequency = frequency;
            this.character = character;
        }

        public Node(Integer frequency, Node left, Node right){ // an internal node
            this.frequency = frequency;
            this.character = null; // internal nodes don't have character
            this.left = left;
            this.right = right;

        }
    }

    public static List<Node> frequencyCount(String text){ // calculating frequency

        Hashtable<Character, Integer> table = new Hashtable<>(); // table of frequencies (each char has a freq)

        // for a single character, add to table / update frequency
        for(int i = 0; i < text.length(); i++){ // Time : O(N) where N is length of the word (number of characters)
            if(table.containsKey(text.charAt(i))){ // .charAt = O(1), .containsKey() = O(M) where M is the amount of single characters in word
                table.replace(text.charAt(i), table.get(text.charAt(i)) + 1); // Time: O(1)
            }else{
                table.put(text.charAt(i), 1); // Time: O(1)
            }
        }

        List<Node> nodes = new ArrayList<>();
        List<Map.Entry<Character, Integer>> entries = new ArrayList<>(table.entrySet());

        // converting the table entries into a leafnode + add to list
        for(int i = 0; i < entries.size(); i++){ // O(M)
            Map.Entry<Character,Integer> entry = entries.get(i);
            nodes.add(new Node(entry.getKey(), entry.getValue()));
        }

        return nodes;
    }

    public static Node buildHuffman(List<Node> nodes){ // building tree

        while(nodes.size() > 1){  // O(M)
            // sort the nodes by frequency
            nodes = mergeSort(nodes); // O(nlogn)

            Node left = nodes.remove(0); // lowest frequency
            Node right = nodes.remove(0); // next lowest frequency

            int freqSum = left.frequency + right.frequency; // calc frequency sum for parent node
            Node parent = new Node(freqSum, left, right); // create parent (internal node)

            nodes.add(parent);
        }

        return nodes.remove(0); // root

    }

    public static void genCode(Node node, String currentCode, Hashtable<Character, String> codes){ // generating huffman codes
        if(node == null){
            return; // base case
        }

        if(node.character != null){ // if node is a leaf
            codes.put(node.character, currentCode);
        }

        genCode(node.left, currentCode + "0", codes);// traverse left, and add 0 // both this and next line would be O(M)
        genCode(node.right, currentCode + "1", codes); // traver right and add 1
    }

    public static Hashtable<Character, String> huffEncoding(String text){ //use all methods to create codes

        List<Node> nodes = frequencyCount(text);
        Node root = buildHuffman(nodes); // O(M) * O(nlogn)

        Hashtable<Character, String> codes = new Hashtable<>();
        genCode(root, "", codes); // O(M)

        return codes;
    }

    public static String encode(String text){ // encode full string
        Hashtable<Character, String> codes = huffEncoding(text);

        String encoded = "";

        for(int i = 0; i < text.length(); i++){ //O(N)
            encoded += codes.get(text.charAt(i));
        }

        return encoded;
    }



    public static void printStats(String text){ // print all stats
        List<Node> nodes = frequencyCount(text);
        nodes = mergeSort(nodes); // sort nodes
        for (int i = 0; i < nodes.size() - 1; i++) { // sort alpha. like in example // O(M)
            for (int j = 0; j < nodes.size() - 1 - i; j++) { //O(M)
                Node a = nodes.get(j);
                Node b = nodes.get(j + 1);
                if (a.character != null && b.character != null && a.character > b.character) { // compare characters
                    nodes.set(j, b);
                    nodes.set(j + 1, a);
                }
            }
        }

        Hashtable<Character, String> codes = huffEncoding(text);
        String encoded = encode(text);

        System.out.println("Stats for " + text);

        System.out.println();


        System.out.println("Frequencies:");
        for (int i = 0; i < nodes.size(); i++) { //O(M)
            char c = nodes.get(i).character;
            if (c == ' ') {
                System.out.println("(space): " + nodes.get(i).frequency); // ensure space is printed as (space)
            } else {
                System.out.println(c + ": " + nodes.get(i).frequency);
            }
        }

        System.out.println("Huffman Codes:");
        for (int i = 0; i < nodes.size(); i++) { // O(M)
            char c = nodes.get(i).character;
            if (c == ' ') {
                System.out.println("(space): " + codes.get(c)); // ensure space is printed as (space)
            } else {
                System.out.println(c + ": " + codes.get(c));
            }
        }
        System.out.println();

        System.out.println("Encoded Binary String for " + text);
        System.out.println(encoded); //O(N)

    }

    public static String checkIfWorking(String encoded, Node root) { // ok so it wasnt really working, but according to my debugging check it is
        String decoded = "";
        Node current = root;

        for (int i = 0; i < encoded.length(); i++) {
            if (encoded.charAt(i) == '0') {
                current = current.left;
            } else {
                current = current.right;
            }

            if (current.character != null) { // leaf node
                decoded += current.character;
                current = root;
            }
        }

        return decoded;
    }



    public static List<Node> mergeSort(List<Node> nodes){ // o(nlogn)
        if(nodes.size() <= 1){
            return nodes;
        }

        int mid = nodes.size() / 2;
        List<Node> left  = new ArrayList<>(nodes.subList(0, mid));
        List<Node> right = new ArrayList<>(nodes.subList(mid, nodes.size()));

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left,right);
    }

    public static List<Node> merge(List<Node> left, List<Node> right) {
        List<Node> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            boolean takeLeft;

            if (left.get(i).frequency < right.get(j).frequency) {
                takeLeft = true;
            } else if (left.get(i).frequency > right.get(j).frequency) {
                takeLeft = false;
            } else {
                if (left.get(i).character != null && right.get(j).character != null) {
                    takeLeft = left.get(i).character < right.get(j).character;
                } else if (left.get(i).character == null && right.get(j).character == null) {
                    takeLeft = true; // both internal, keep left
                } else {
                    takeLeft = left.get(i).character == null; // internal nodes go left
                }
            }

            if (takeLeft) {
                result.add(left.get(i));
                i++;
            } else {
                result.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            result.add(left.get(i));
            i++;
        }
        while (j < right.size()) {
            result.add(right.get(j));
            j++;
        }

        return result;
    }

    public static void main(String[] args) {
        String text = "marcus fenix is a gear";

        // build tree and encode
        List<Node> nodes = frequencyCount(text);
        Node root = buildHuffman(nodes);
        String encoded = encode(text);

        // decode and verify
        String decoded = checkIfWorking(encoded, root);

        System.out.println("Original: " + text);
        System.out.println("Encoded:  " + encoded);
        System.out.println("Decoded:  " + decoded);
        System.out.println("Match:    " + text.equals(decoded));
    }



}
