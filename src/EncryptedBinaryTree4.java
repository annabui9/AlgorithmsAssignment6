public class EncryptedBinaryTree4 {

    public static boolean isTarget(int[] bt, int t){

        int[] actual = new int[bt.length]; // store "actual" values

        // root is always 1, so I'm just gonna change that
        if (bt[0] != 1) {
            actual[0] = 1;
        }

        for (int i = 0; i < bt.length; i++) {
            if (bt[i] == -1) { // empty node, skip
                continue;
            }

            if (actual[i] == t) { // check if current value is target (starting with root of 1)
                return true;
            }

            // calculate left child
            int left = 2 * i + 1;
            if (left < bt.length && bt[left] == -2) {
                actual[left] = 3 * actual[i] + 1; // add actual value
            }

            // calculate right child
            int right = 2 * i + 2;
            if (right < bt.length && bt[right] == -2) {
                actual[right] = 2 * actual[i] + 5; // add actual value
            }
        }

        return false;
    }

    public static void main(String[] args){
        int[] bt = {-2, -2, -1, -2, -1};
        System.out.println(isTarget(bt, 4));

    }





}
