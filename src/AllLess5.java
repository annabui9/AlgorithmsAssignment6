public class AllLess5 {

    public static String allLess(String[] s, int x){
        String list = ""; // list to return

        if(s.length == 0){ // if string is empty
            return "The heap is empty.";
        }

        for(int i = 0; i < s.length; i++){ // O(N) where N is length of the array

            if(s[i].equals("") || s[i].equals(null)){ // don't include "" or null [as seen in example]

            }else if(s[i].length() < x){ // if length of word is less than x, add the word to list
                list = list + "\n" + "'" + s[i] + "'";
            }
        }
        return list;
    }

    public static void main(String args[]){
        String[] s = {"zero", " size", "nutella", "jojo", "luna", "isse", "astor", "as", "entretien", "", "cal"};
        int x = 3;
        int x2 = 5;

        String[] s0 = {};

        System.out.println("x = 3");
        System.out.println(allLess(s, x));
        System.out.println("x = 5");
        System.out.println(allLess(s, x2));
    }




}
