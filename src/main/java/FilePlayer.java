import java.io.*;
import java.util.*;

public class FilePlayer extends Players {

    private final EasyBot easybot = new EasyBot();
    private final Scanner scanner = new Scanner(System.in);
    private final List<String> guessList = new ArrayList<>();

    public void loadFile() {
        File pDir = new File("fileInput");
        System.out.println("Please enter the file name:");

        while (true) {
            String filename = scanner.nextLine();
            File path = new File(pDir, filename);

            if (!path.exists()) {
                System.out.println("Invalid input. Please try again:");
                continue;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                // Read valid 4-digit non-repeating lines
                reader.lines()
                        .map(String::trim)
                        .filter(line -> line.matches("^(?!.*(.).*\\1)\\d{4}$"))
                        .forEach(guessList::add);

                // Fill missing entries up to 8
                while (guessList.size() < 8) {
                    easybot.setSecretCodeValue();
                    guessList.add(easybot.getSecretCodeValue());
                }
                break;
            } catch (IOException e) {
                System.out.println("File error: " + e.getMessage());
            }
        }
    }

    @Override
    public String getSecretCodeValue() {
        return secretCodeValue;
    }

    @Override
    public void setSecretCodeValue() {
        loadFile();
        secretCodeValue = guessList.removeFirst();
    }

    @Override
    public String getGuessCodeValue() {
        return generatedValue;
    }

    public void setGuessCodeValue(int count) {
        generatedValue = guessList.get(count - 1);
    }
}
