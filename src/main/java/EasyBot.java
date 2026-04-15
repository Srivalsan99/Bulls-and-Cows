import java.util.*;

public class EasyBot extends Computer {

    // Constructor
    public EasyBot() {
        this.secretCodeValue = "";
        this.generatedValue = "";
    }

    @Override
    public void setSecretCodeValue(){
        generatedValue = randomGenerator();
    }

    @Override
    public String getSecretCodeValue() {
        return secretCodeValue;
    }

    @Override
    public String getGuessCodeValue() {
        return generatedValue;
    }

    @Override
    public void setGuessCodeValue() {
        // Generate random guess
        generatedValue = randomGenerator();
    }
}
