
public class temp2 {
    public static void main(String[] args) {
        String s = "Mew,,New";
        String[] p = s.split(",");
        for(String t:p){
            System.out.println(t);
            System.out.println(t.equals(""));
        }
    }
}
