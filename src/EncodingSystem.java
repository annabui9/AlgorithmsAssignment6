import java.util.Random;

public class EncodingSystem {

    static Huffman huffman = new Huffman();
    static String[] texts = {"marcus fenix is a gear"};

    public static String[] encodeInput(String[] texts) {
        String[] encodeStrings = new String[texts.length];

        for (int i = 0; i < texts.length; i++) { // encode each string in the array
            encodeStrings[i] = huffman.encode(texts[i]);
        }

        return encodeStrings;
    }

    // returns the highest code string
    public static String highestCode(String[] texts) {
        String[] encoded = encodeInput(texts);
        String highest = encoded[0];

        for (int i = 1; i < encoded.length; i++) { // compare each encoded string w current highest
            if (encoded[i].compareTo(highest) > 0) {
                highest = encoded[i]; //update id higher
            }
        }

        return highest;
    }

    // Fisher-Yates shuffle (i googled this)
    public static String[] shuffleCodes(String[] texts) {
        String[] encoded = encodeInput(texts);
        Random rand = new Random();

        // start w last element and swap (randomly) with an element that is earlier in the array
        for (int i = encoded.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);

            String temp = encoded[i];
            encoded[i] = encoded[j];
            encoded[j] = temp;
        }

        return encoded;
    }

    public static void printAllCodes(String[] texts) { //print codes for each string
        String[] encoded = encodeInput(texts);

        for (int i = 0; i < encoded.length; i++) {
            System.out.println(encoded[i]);
        }
    }

    public static void stats(String[] texts) { // print stats for each string
        for (int i = 0; i < texts.length; i++) {
            Huffman.printStats(texts[i]);
        }
    }

    public static void printStringArray(String[] array) { // just here so i don't have to type in main
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }



    public static void main(String[] args) {
        System.out.println("Highest Code: ");
        System.out.println(highestCode(texts));

        System.out.println();

        System.out.println("Shuffled Codes: ");
        printStringArray(shuffleCodes(texts));

        System.out.println();

        System.out.println("All codes for inputted array: ");
        printAllCodes(texts);

        System.out.println();

        System.out.println("Stats for inputted array: ");
        stats(texts);

    }
}