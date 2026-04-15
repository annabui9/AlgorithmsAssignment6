import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BRTree6 {

    static BRNode root;

    public BRTree6(boolean value) {

    }

    // main to test... many more methods below
    public static void main(String[] args){

        //inRange(15, 20), output would be {10, 30, 42, 55, 77}
        buildTree();
        System.out.println("Before: ");
        printTree();
        BRTree6.inRange(15, 20);
        System.out.println("After inRange(15, 20): ");
        printTree();
        System.out.println();

        //inRange(0, 2), output would be  {10, 19, 20, 30, 42, 55, 77} (so no change)
        buildTree();
        System.out.println("Before: ");
        printTree();
        BRTree6.inRange(0, 2);
        System.out.println("After inRange(0, 2): ");
        printTree();
        System.out.println();

        //inRange(25, 60) output would be {10, 19, 20, 77}
        buildTree();
        System.out.println("Before: ");
        printTree();
        BRTree6.inRange(25, 60);
        System.out.println("After inRange(25, 60): ");
        printTree();

    }

    public static void inOrder(BRNode node) { // put nodes in order
        if (node == null){
            return;
        }
        inOrder(node.left);
        System.out.print(node.key + " ");
        inOrder(node.right);
    }

    public static void printTree() { // print tree keys
        System.out.print("Tree keys: { ");
        inOrder(BRTree6.root);
        System.out.println("}");
    }

    public static void buildTree(){ // testing tree
        BRTree6.root = null; // reset
        int[] keys = {10, 19, 20, 30, 42, 55, 77};
        for (int k : keys) {
            BRTree6.insert(k);
        }
    }


    public static void inRange(int a, int b){
        List<Integer> toDelete = new ArrayList<>(); // array to delete

        collectInRange(root, a, b, toDelete);
        for(int key: toDelete){ // remove nodes to be deleted (in range)
            remove(key);
        }

    }

    // finding nodes that are in range to be deleted
    public static void collectInRange(BRNode root, int a, int b, List<Integer> result){

        if (root == null) {
            return;
        }

        Stack<BRNode> stack = new Stack<>();
        stack.push(root); // start at root

        while (!stack.isEmpty()) {
            BRNode node = stack.pop();
            if (node == null){
                continue;
            }

            if (node.key >= a && node.key <= b) { // if in range add to result
                result.add(node.key);
            }
            if (node.key > a) { // if values are greater than a, add left to stack to be checked
                stack.push(node.left);
            }
            if (node.key < b) { // if values are less than b. add right to stack to be checked
                stack.push(node.right);
            }
        }

    }

    // INSERTING METHODS TO MAKE TREE
    public static void insert(int key) {
        BRNode newNode = new BRNode();
        newNode.key = key;
        newNode.color = false; // new nodes are red

        if (root == null) {
            root = newNode;
            root.color = true; // root is always black
            return;
        }

        BRNode current = root;
        while (true) {
            //searching for place to insert
            if (key < current.key) { // go left
                if (current.left == null) {
                    current.left = newNode; // add node if no child there
                    newNode.parent = current;
                    break;
                }
                current = current.left;
            } else { // go right
                if (current.right == null) {
                    current.right = newNode; // add node if no child there
                    newNode.parent = current;
                    break;
                }
                current = current.right;
            }
        }
        fixInsert(newNode);
    }

    public static void fixInsert(BRNode node){
        while (node.parent != null && !node.parent.color) { //if not root and parent is red
            BRNode parent = node.parent;
            BRNode grandparent = parent.parent;
            if (grandparent == null){ // root
                break;
            }

            if (parent == grandparent.left) { // parent is left child
                BRNode uncle = grandparent.right; // uncle right child
                if (uncle != null && !uncle.color) { // uncle red --recolor
                    parent.color = true; // parent is now black
                    uncle.color = true; // uncle is now black
                    grandparent.color = false; // grand is now red
                    node = grandparent; // move up
                } else {
                    if (node == parent.right) { // right child of parent
                        rotateLeft(parent); // rotate to be line
                        node = parent;
                    }

                    // now a line
                    node.parent.color = true; // parent now black
                    grandparent.color = false; // grandparent now red
                    rotateRight(grandparent); // rotate to be balanced
                }
            } else { // parent is right child
                BRNode uncle = grandparent.left; // uncle is left child
                if (uncle != null && uncle.color == false) { // uncle red -- recolor
                    parent.color = true; // black
                    uncle.color = true; // black
                    grandparent.color = false; // red
                    node = grandparent;
                } else {
                    if (node == parent.left) { // left child --  do rotation if not line
                        rotateRight(parent);
                        node = parent;
                    }

                    // recolor and rerotate re-rotate
                    node.parent.color = true;
                    grandparent.color = false;
                    rotateLeft(grandparent);
                }
            }
        }
        root.color = true; // always black root
    }

    // BST Search
    public static BRNode search(int key) {
        BRNode currentNode = root;

        while (currentNode != null) {
            if (currentNode.key == key) {
                return currentNode;
            } else if (key < currentNode.key) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }
        return null;
    }

    public static void remove(int key) {

        BRNode node = search(key); // remove if found
        if (node != null) {
            removeNode(node);
        }

    }

    public static void removeNode(BRNode node) {
        // if has 2 children
        if (node.left != null && node.right != null) {
            BRNode predecessorNode = getPredecessor(node); // largest value in left st
            int predecessorKey = predecessorNode.key;
            removeNode(predecessorNode); // recursively remove pred node
            node.key = predecessorKey; // update
            return;
        }

        //if a red leaf, so can just remove
        if (node.color) {
            detachNode(node);
            return;
        }

        // black node, must fix tree
        prepareForRemoval(node);
        detachNode(node);
    }

    public static void detachNode(BRNode node){ // removing single node
        BRNode child;
        if (node.left != null) {
            child = node.left;
        } else {
            child = node.right;
        }

        if(node.parent == null){
            root = child;
            if(root != null){
                root.parent = null;
                root.color = true;
            }
        }else{
            if(child != null){
                child.parent = node.parent;
                child.color = true;
            }
            node.parent.replaceChild(node, child);
        }

    }

    public static void prepareForRemoval(BRNode node) {
        if(tryCase1(node)){
            return;
        }

        BRNode sibling = getSibling(node);

        if(tryCase2(node, sibling)){
            sibling = getSibling(node);
        }
        if(tryCase3(node, sibling)){
            return;
        }
        if(tryCase4(node, sibling)){
            return;
        }
        if(tryCase5(node, sibling)){
            sibling = getSibling(node);
        }if(tryCase6(node, sibling)){
            sibling = getSibling(node);
        }

        sibling.color = node.parent.color;
        node.parent.color = true;
        if(node == node.parent.left){
            sibling.right.color = true;
            rotateLeft(node.parent);
        }else{
            sibling.left.color = true;
            rotateRight(node.parent);
        }

    }

    // CASES FOR REMOVAL PREP
    public static boolean tryCase1(BRNode node){
        if(!node.color || node.parent == null){
            return true;
        }else{
            return false;
        }
    }
    public static boolean tryCase2(BRNode node, BRNode sibling){
        if(!sibling.color){
            node.parent.color = false;
            sibling.color = true;
            if(node == node.parent.left) {
                rotateLeft(node.parent);
                return true;
            }else{
                rotateRight(node.parent);
                return true;
            }
        }
        return false;
    }
    public static boolean tryCase3(BRNode node, BRNode sibling){
        if(node.parent.color && bothBlackChildren(sibling)){
            sibling.color = false;
            prepareForRemoval(node.parent);
            return true;
        }
        return false;
    }
    public static boolean tryCase4(BRNode node, BRNode sibling){
        if(!node.parent.color && bothBlackChildren(sibling)){
            node.parent.color = true;
            sibling.color = false;
            return true;
        }
        return false;
    }
    public static boolean tryCase5(BRNode node, BRNode sibling){
        if(nonNullAndRed(sibling.left) && nullOrBlack(sibling.right) && node == node.parent.left){
            sibling.color = false;
            sibling.left.color = true;
            rotateRight(sibling);
            return true;
        }
        return false;
    }
    public static boolean tryCase6(BRNode node, BRNode sibling){
        if(nullOrBlack(sibling.left) && nonNullAndRed(sibling.right) && node == node.parent.right){
            sibling.color = false;
            sibling.right.color = true;
            rotateLeft(sibling);
            return true;
        }
        return false;
    }


    // CODE FOR ROTATION
    public static void rotateLeft(BRNode node){
        BRNode newParent = node.right;
        BRNode rightLeftChild = newParent.left;

        if (node.parent != null) {
            node.parent.replaceChild(node, newParent);
        } else {
            root = newParent;
            root.parent = null;
        }

        newParent.left = node;
        node.parent = newParent;
        node.right = rightLeftChild;
        if (rightLeftChild != null) {
            rightLeftChild.parent = node;
        }
    }
    public static void rotateRight(BRNode node){
        BRNode newParent = node.left;
        BRNode leftRightChild = newParent.right;

        if (node.parent != null) {
            node.parent.replaceChild(node, newParent);
        } else {
            root = newParent;
            root.parent = null;
        }

        newParent.right = node;
        node.parent = newParent;
        node.left = leftRightChild;
        if (leftRightChild != null) {
            leftRightChild.parent = node;
        }

    }

// HELPER METHODS
    public static BRNode getSibling(BRNode node) { // retrieve siblings
        if (node.parent != null) {
            if (node == node.parent.left) {
                return node.parent.right;
            }
            return node.parent.left;
        }
        return null;
    }

    public static BRNode getPredecessor(BRNode node) { // largest value in left sub
        BRNode pred = node.left;
        while (pred.right != null) {
            pred = pred.right;
        }
        return pred;
    }

    public static boolean bothBlackChildren(BRNode node) { // if both children black , null is black

        if (node.left != null && !node.left.color) {
            return false;
        }
        if (node.right != null && !node.right.color) {
            return false;
        }
        return true;
    }

    public static boolean nonNullAndRed(BRNode node) { // check if red node
        if (node == null || node.color != false) {
            return false;
        }
        return true;
    }

    public static boolean nullOrBlack(BRNode node) { // check if black or null
        if (node == null) {
            return true;
        }
        return (node.color);
    }


}