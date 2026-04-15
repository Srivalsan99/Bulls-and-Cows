import java.util.*;
import java.util.stream.*;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GameUtils {
    private static final List<String> VALID_TYPES = List.of("easybot", "mediumbot", "hardbot", "human", "fileplayer", "1", "2", "3", "4", "5");
    private static final int NO_OF_PLAYERS=2;
    private final List<List<String>> playerInfo = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);
    private final List<List<List<String>>> playerHistory = new ArrayList<>();
    private final Players[] players = {new EasyBot(), new MediumBot(), new HardBot(), new Human(scanner), new FilePlayer()};
    private boolean humanFlag=false;


    public void saveGameToFile() {
        System.out.print("Please Enter the file name to save. Dont have to include extension like .txt, which will be already be added.");
        String fileName;
        while (true) {
            fileName = scanner.nextLine();
            if (!fileName.isEmpty()){
                break;
            }
            System.out.println("Please enter file name correctly");
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName+".txt"));
             PrintWriter console = new PrintWriter(System.out, true)) {

            printBoth(writer, console, "Bulls & Cows game result");
            printBoth(writer, console, "Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            printBoth(writer, console, "");
            printBoth(writer, console, "Player Type Info:");
            printBoth(writer, console, " 1 or EasyBot");
            printBoth(writer, console, " 2 or MediumBot");
            printBoth(writer, console, " 3 or HardBot");
            printBoth(writer, console, " 4 or Human");
            printBoth(writer, console, " 5 or File Player");
            printBoth(writer, console, "");

            printBoth(writer, console, "----------------------------");
            for (int i = 0; i < playerInfo.size(); i++) {
                printBoth(writer, console, String.format("Player %d: %s (%s) - Secret: %s",
                        i + 1, playerInfo.get(i).get(0), playerInfo.get(i).get(1), playerInfo.get(i).get(2)));
            }
            printBoth(writer, console, "---");
            printBoth(writer, console, "");

            int maxTurns = playerHistory.stream()
                    .mapToInt(List::size)
                    .max()
                    .orElse(0);

            for (int turn = 0; turn < maxTurns; turn++) {
                printBoth(writer, console, String.format("--- Turn %d:", turn + 1));

                // Loop through ALL players dynamically
                for (int player = 0; player < playerInfo.size(); player++) {
                    List<List<String>> history = playerHistory.get(player);
                    if (turn < history.size()) {
                        List<String> round = history.get(turn);
                        if (round.size() >= 3) {
                            String playerName = round.get(3);
                            String guess = round.get(2);
                            String bulls = round.get(0);
                            String cows = round.get(1);
                            String line = String.format("%s guessed %s, scoring %s bull%s and %s cow%s",
                                    playerName, guess, bulls, bulls.equals("1") ? "" : "s",
                                    cows, cows.equals("1") ? "" : "s");
                            printBoth(writer, console, line);
                        }
                    }
                }
                printBoth(writer, console, "");
            }

            boolean gameWon = false;
            for (int player = 0; player < playerInfo.size(); player++) {
                for (List<String> round : playerHistory.get(player)) {
                    if (round.size() >= 3 && round.get(0).equals("4")) {
                        String winner = round.get(3);
                        printBoth(writer, console, "");
                        printBoth(writer, console, winner + " wins! :)");
                        gameWon = true;
                        break;
                    }
                }
                if (gameWon) break;
            }
            if (!gameWon) {
                printBoth(writer, console, "");
                printBoth(writer, console, "Game incomplete");
            }
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
        System.out.println("\nGame saved to game_result.txt");
    }

    // Helper method to print to both file and console
    private void printBoth(PrintWriter fileWriter, PrintWriter consoleWriter, String line) {
        fileWriter.println(line);
        consoleWriter.println(line);
    }


    public void setPlayerInfo() {
        for (int i = 1; i <= NO_OF_PLAYERS; i++) {
            while (true) {
                System.out.println("Enter player "+i+" name: ");
                String name = scanner.nextLine().trim();
                System.out.println("Note: There can't be 2 human players.");
                System.out.println("Enter player "+i+" type \n 1 or EasyBot \n 2 or MediumBot \n 3 or HardBot \n 4 or Human \n 5 or File Player");
                String type = scanner.nextLine().trim();

                if (isValidPlayer(name, type, i)) {
                    playerInfo.add(new ArrayList<>(List.of(name, type)));  // ← FIXED: Mutable ArrayList
                    break;
                }
            }
        }
    }

    private boolean isValidPlayer(String name, String type, int i) {
        if (isHuman(type) || humanFlag){
            humanFlag=true;
        }
        if (name.isEmpty()) return false;
        boolean isValidType = VALID_TYPES.stream()
                .anyMatch(valid -> valid.equalsIgnoreCase(type));

        if (!isValidType) {
            System.out.println("Enter the correct Option");
            return false;
        }

        if (isHuman(type) && playerInfo.stream().anyMatch(p -> isHuman(p.get(1)))) {
            System.out.println("There can't be 2 human players.");
            return false;
        }

        System.out.println("Player " + (i == 1 ? "Name" : "Type") + " Input");
        return true;
    }

    private boolean isHuman(String type) {
        return "human".equalsIgnoreCase(type) || "4".equalsIgnoreCase(type);
    }

    public void playerSecretNumberInput() {
        Random rand = new Random();
        for (int i = 0; i < NO_OF_PLAYERS; i++) {
            Players p = players[getPlayerIndex(playerInfo.get(i).get(1))];
            p.setSecretCodeValue();
            String secret = p.getSecretCodeValue();
            if (!isValidCode(secret)) {
                System.out.println("Invalid secret for " + playerInfo.get(i).getFirst() + ". Generating fallback.");
                secret = generateValidSecret(rand);
            }
            playerInfo.get(i).add(secret);
//            System.out.println()
            // Note: (Please disable this if condition check , if you want to debug it). Because we can't allow human player to view other player code for fair game.
            if (!humanFlag){
                System.out.println("Player " + playerInfo.get(i).getFirst() + " secret: " + secret);
            }
        }
        playerHistory.clear();
        playerHistory.addAll(List.of(new ArrayList<>(), new ArrayList<>()));
    }

    private int getPlayerIndex(String type) {
        return switch (type.toLowerCase()) {
            case "1", "easybot" -> 0; case "2", "mediumbot" -> 1; case "3", "hardbot" -> 2;
            case "4", "human" -> 3; case "5", "fileplayer" -> 4; default -> -1;
        };
    }

    private boolean isValidCode(String code) {
        return code != null && code.length() == 4 && code.chars().allMatch(Character::isDigit)
                && code.chars().distinct().count() == 4;
    }

    private String generateValidSecret(Random rand) {
        return IntStream.range(0, 4)
                .mapToObj(i -> String.valueOf(rand.nextInt(10)))
                .collect(Collectors.collectingAndThen(Collectors.toSet(), s -> {
                    while (s.size() < 4) s.add(String.valueOf(rand.nextInt(10)));
                    return String.join("", s.stream().limit(4).toList());
                }));
    }

    public boolean guessArena(int count) {
        for (int i = 0; i < NO_OF_PLAYERS; i++) {
            Players attacker = players[getPlayerIndex(playerInfo.get(i).get(1))];
            if (attacker instanceof MediumBot mb) mb.setGuessCodeValue(playerHistory, i);
            else if (attacker instanceof HardBot hb) hb.setGuessCodeValue(playerHistory, i);
            else if (attacker instanceof FilePlayer fp) fp.setGuessCodeValue(count);
            else attacker.setGuessCodeValue();

            String guess = attacker.getGuessCodeValue();
            System.out.println("Guess Value by " + playerInfo.get(i).get(0) + " (" + playerInfo.get(i).get(1) + ") => " + guess);

            ensureHistorySize(i);
            List<List<String>> history = playerHistory.get(i);

            for (int j = 0; j < 2; j++) {
                if (i == j) continue;
                String secret = playerInfo.get(j).get(2);
                if (guess == null || secret == null || guess.length() != 4 || secret.length() != 4) continue;

                int[] result = computeBullsAndCows(guess, secret);
                history.add(List.of(result[0] + "", result[1] + "", guess, playerInfo.get(i).get(0)));

                if (guess.equals(secret)) {
                    System.out.println("Player " + playerInfo.get(i).getFirst() + " wins against " + playerInfo.get(j).getFirst() + "!");
                    return true;
                }
                System.out.println("Player " + playerInfo.get(i).getFirst() + " => bulls:" + result[0] + ", cows: " + result[1]);
            }
        }
        return false;
    }

    private void ensureHistorySize(int i) {
        while (playerHistory.size() <= i) playerHistory.add(new ArrayList<>());
    }

    private int[] computeBullsAndCows(String guess, String secret) {
        int bulls = 0, cows = 0;
        for (int k = 0; k < 4; k++) {
            if (guess.charAt(k) == secret.charAt(k)) bulls++;
            else if (secret.contains(String.valueOf(guess.charAt(k)))) cows++;
        }
        return new int[]{bulls, cows};
    }

    public List<List<String>> getPlayerInfo() { return playerInfo; }
    public List<List<List<String>>> getGameHistory() { return playerHistory; }


}
