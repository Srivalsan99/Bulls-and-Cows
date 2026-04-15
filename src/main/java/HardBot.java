import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HardBot extends MediumBot {

    private List<String> possibleCodes;    // All 5040 valid codes
    private List<String> consistentCodes;  // Codes matching ALL history

    public HardBot() {
        this.secretCodeValue = "";
        this.generatedValue = "";
        this.previousGuessList = new HashSet<>();
        this.possibleCodes = new ArrayList<>();  // Add this
        this.consistentCodes = new ArrayList<>(); // Add this
        initializePossibleCodes();
    }

    // Note: I have not used generating secret code parts, for Covering method Look up scenario.

    // Generating all random numbers
    private void initializePossibleCodes() {
        possibleCodes  = IntStream.range(123, 9876)
                .mapToObj(i -> String.format("%04d", i))
                .filter(s -> s.matches("^(?!.*(\\d)\\1)\\d{4}$"))
                .collect(Collectors.toList());
    }

    @Override
    public void setGuessCodeValue(List<List<List<String>>> gameHistory, int player) {
        // First guess: random from all possibilities
        if (gameHistory.isEmpty() || player >= gameHistory.size() || gameHistory.get(player).isEmpty()) {
            Collections.shuffle(possibleCodes);
            generatedValue = possibleCodes.getFirst();
        } else {
            pruneConsistentCodes(gameHistory, player);
            if (!consistentCodes.isEmpty()) {
                Collections.shuffle(consistentCodes);
                generatedValue = consistentCodes.getFirst();
            } else {
                Collections.shuffle(possibleCodes);
                generatedValue = possibleCodes.getFirst();
            }
        }
        previousGuessList.add(generatedValue);
    }

    // Elimation of unwanted possiblities
    private void pruneConsistentCodes(List<List<List<String>>> gameHistory, int player) {
        List<List<String>> history = gameHistory.get(player);
        if (consistentCodes == null) {
            consistentCodes = new ArrayList<>();  // Null check + init
        } else {
            consistentCodes.clear();
        }

        for (String candidate : possibleCodes) {
            boolean matchesAll = true;
            for (List<String> round : history) {
                if (round.size() < 4) continue;
                String pastGuess = round.get(2);
                int expectedBulls = Integer.parseInt(round.get(0));
                int expectedCows = Integer.parseInt(round.get(1));

                int[] feedback = computeBullsCows(pastGuess, candidate);
                if (feedback[0] != expectedBulls || feedback[1] != expectedCows) {
                    matchesAll = false;
                    break;
                }
            }
            if (matchesAll) consistentCodes.add(candidate);
        }
    }

    private int[] computeBullsCows(String guess, String candidateSecret) {
        int bulls = 0, cows = 0;
        for (int i = 0; i < 4; i++) {
            if (guess.charAt(i) == candidateSecret.charAt(i)) bulls++;
            else if (candidateSecret.contains(String.valueOf(guess.charAt(i)))) cows++;
        }
        return new int[]{bulls, cows};
    }

}
