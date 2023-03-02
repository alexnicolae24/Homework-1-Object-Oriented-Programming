package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.GameInput;
import fileio.Input;

public class MultipleGamesManager {
    private Input input;
    private int playerOneWins = 0;
    private int playerTwoWins = 0;
    private int totalGamesPlayed = 0;

    public MultipleGamesManager(final Input input) {
        this.input = input;
    }

    /**
     *
     * @param objectMapper
     * @param output
     */
    public void solve(final ObjectMapper objectMapper, final ArrayNode output) {
        for (GameInput gameInput : input.getGames()) {
            GameManager gameManager = new GameManager(input, gameInput.getStartGame(), this);
            gameManager.doActions(objectMapper, output, gameInput.getActions());
        }
    }

    public final int getPlayerOneWins() {
        return playerOneWins;
    }

    public final int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public final int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    /**
     *
     */
    public void playerOneWon() {
        playerOneWins++;
        totalGamesPlayed++;
    }

    /**
     *
     */
    public void playerTwoWon() {
        playerTwoWins++;
        totalGamesPlayed++;
    }
}
