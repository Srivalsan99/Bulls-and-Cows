import java.util.*;

public class Game {
    private Scanner scanner = new Scanner(System.in);
    private GameUtils gameUtils;
    private boolean status;

    public Game() {
        this.gameUtils = new GameUtils();
    }

    public void start() {
        gameUtils.setPlayerInfo();
        gameUtils.playerSecretNumberInput();
//        System.out.println(gameUtils.getPlayerInfo());

        for (int i = 1; i <= 7; i++) {
            System.out.printf("-----------------Round %d--------------------",i);
            status = gameUtils.guessArena(i);
            if (status) break;
            if (!status && i==7) System.out.println("No Body Wins. It is a Draw.");
        }

        System.out.println("Do you wish to save the file? (Enter y or yes to save the file)");
        String fileOutput = scanner.nextLine().trim();
        if (fileOutput.equalsIgnoreCase("y") || fileOutput.equalsIgnoreCase("yes")) {
            gameUtils.saveGameToFile();
        }
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
