public class BRNode {
    // node class for BR Tree

    boolean color; // true = black, false = red
    BRNode left;
    BRNode right;
    BRNode parent;
    int key;
    int height = 0;

    // getters and setters

    public boolean isColor() {
        return color;
    }
    public void setColor(boolean color) {
        this.color = color;
    }

    public int getKey() {
        return key;
    }
    public void setKey(int value) {
        this.key = value;
    }

    public BRNode getLeft() {
        return left;
    }
    public void setLeft(BRNode left) {
        this.left = left;
    }

    public BRNode getRight() {
        return right;
    }
    public void setRight(BRNode right) {
        this.right = right;
    }

    public BRNode getParent() {
        return parent;
    }
    public void setParent(BRNode parent) {
        this.parent = parent;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }


    // method to replace child if needed
    public void replaceChild(BRNode current, BRNode newChild) {
        if (this.left == current) {
            this.left = newChild; // replace if  nodeto be replaced is left
        }else if (this.right == current) {
            this.right = newChild; // replace if node to be replaces is right node
        }

        if (newChild != null) {
            newChild.parent = this; // if have no child
        }
    }
}

