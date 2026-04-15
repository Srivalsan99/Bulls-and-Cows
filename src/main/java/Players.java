public abstract class Players {

    // abstract attributes
    protected String secretCodeValue;
    protected String generatedValue;

    // Secret code methods
    public abstract String getSecretCodeValue();

    public abstract void setSecretCodeValue();

    // Guess code methods
    public abstract String getGuessCodeValue();

    public void setGuessCodeValue() {
    }

}
