import java.util.*;

public abstract class Computer extends Players {

    public String randomGenerator() {
        // Generate random 4-digit code with unique digits
        List<String> digits = new ArrayList<>(Arrays.asList("0","1","2","3","4","5","6","7","8","9"));
        Collections.shuffle(digits);
        List<String> selected = digits.subList(0, 4);
        return String.join("", selected);
    }

}
