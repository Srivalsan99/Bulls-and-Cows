import java.util.Scanner;

public class Human extends Players {

    private final Scanner scanner;
    private String secretCodeValue;
    private String generatedValue;
    private static final String UNIQUE_4_DIGIT_REGEX = "^(?!.*(.).*\\1)\\d{4}$";

    public Human(Scanner scanner) {
        this.scanner = scanner;
        this.secretCodeValue = "";
        this.generatedValue = "";
    }

    private String readValidCode(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (input.matches(UNIQUE_4_DIGIT_REGEX)) {
                return input;
            } else {
                System.out.println("Invalid input. Must be 4 unique digits.");
            }
        }
    }

    @Override
    public String getSecretCodeValue() {
        return secretCodeValue;
    }

    @Override
    public void setSecretCodeValue() {
        secretCodeValue = readValidCode("Please enter your secret code (4 unique digits): ");
        System.out.println("Secret code set successfully.");
    }

    @Override
    public String getGuessCodeValue() {
        return generatedValue;
    }

    @Override
    public void setGuessCodeValue() {
        generatedValue = readValidCode("Please enter your guess code (4 unique digits): ");
    }
}
