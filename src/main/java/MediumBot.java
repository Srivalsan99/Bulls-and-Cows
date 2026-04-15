import java.util.*;

public class MediumBot extends EasyBot {
    protected Set<String> previousGuessList;

    public MediumBot() {
        this.secretCodeValue = "";
        this.generatedValue = "";
        this.previousGuessList = new HashSet<>();
    }

    @Override
    public void setSecretCodeValue() {
        // Generate random secret code
        secretCodeValue = randomGenerator();
    }

    public void setGuessCodeValue(List<List<List<String>>> gameHistory, int player) {
        // Populate previous guesses from history
        if (!gameHistory.isEmpty() && player < gameHistory.size() && !gameHistory.get(player).isEmpty()) {
            List<List<String>> gameLoop = gameHistory.get(player);
            for (List<String> playerList : gameLoop) {
                if (playerList.size() > 2) {
                    previousGuessList.add(playerList.get(2));
                }
            }
        }
        do {
            generatedValue = randomGenerator();
        } while (previousGuessList.contains(generatedValue));

        previousGuessList.add(generatedValue); // Remember this guess
    }
}
