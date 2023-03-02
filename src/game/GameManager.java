package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.Input;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static final int PLAYER_ONE_ID = 0;
    private static final int PLAYER_TWO_ID = 1;
    private Game game;
    private MultipleGamesManager multipleGamesManager;
    private Input input;
    private StartGameInput startGameInput;
    private int manaIncrementValue = 1;
    private int eachPlayerEndedThisRound = 0;

    public GameManager(final Input input, final StartGameInput startGameInput,
                       final MultipleGamesManager multipleGamesManager) {
        try {
            this.input = input;
            this.game = new Game();
            fillPlayerDecks(input);
            this.startGameInput = startGameInput;
            this.multipleGamesManager = multipleGamesManager;
            game.setWhoseTurn(startGameInput.getStartingPlayer() - 1);
            game.setShuffleSeed(startGameInput.getShuffleSeed());
            setPlayersHeroCards(startGameInput);
            setPlayersInitialDeck(startGameInput);
            shuffleDecks();
            takeFirstCardFromDeckInHand();
            setInitialManaForPlayers();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void setInitialManaForPlayers() {
        game.incrementMana(manaIncrementValue);
    }

    /**
     *
     * @param objectMapper
     * @param output
     * @param actions
     */
    public void doActions(final ObjectMapper objectMapper, final ArrayNode output,
                          final ArrayList<ActionsInput> actions) {
        for (ActionsInput action : actions) {
            ObjectNode actionNode = objectMapper.createObjectNode();
            actionNode.put("command", action.getCommand());
            if (action.getCommand().equals(Command.COMMAND_GET_PLAYER_DECK)) {
                actionNode.put("playerIdx", action.getPlayerIdx());
                actionNode.put("output", solveGetPlayerDeck(action.getPlayerIdx()));
                output.add(actionNode);
            } else if (action.getCommand().equals(Command.COMMAND_GET_PLAYER_HERO)) {
                actionNode.put("playerIdx", action.getPlayerIdx());
                actionNode.put("output", solveGetPlayerHero(action.getPlayerIdx()));
                output.add(actionNode);
            } else if (action.getCommand().equals(Command.COMMAND_GET_PLAYER_TURN)) {
                actionNode.put("output", game.getWhoseTurn() + 1);
                output.add(actionNode);
            } else if (action.getCommand().equals(Command.COMMAND_END_PLAYER_TURN)) {
                if (game.getWhoseTurn() == 0) {
                    for (int i = 2; i < GameTable.TABLE_ROWS; i++) {
                        for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                            if (game.getTable().getTable()[i][j] instanceof MinionCard) {
                                if (((MinionCard) game.getTable()
                                        .getTable()[i][j]).isFrozen()) {
                                    ((MinionCard) game.getTable()
                                            .getTable()[i][j]).setFrozen(false);
                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                            if (game.getTable().getTable()[i][j] instanceof MinionCard) {
                                if (((MinionCard) game.getTable()
                                        .getTable()[i][j]).isFrozen()) {
                                    ((MinionCard) game.getTable()
                                            .getTable()[i][j]).setFrozen(false);
                                }
                            }
                        }
                    }
                }
                game.endPlayerTurn();
                game.setCurrentPlayerAlreadyAttackedCardsToFalse();

                eachPlayerEndedThisRound++;
                if (eachPlayerEndedThisRound == 2) {
                    eachPlayerEndedThisRound = 0;
                    manaIncrementValue++;
                    if (manaIncrementValue > 2 * 2 * 2 + 2) {
                        manaIncrementValue = 2 * 2 * 2 + 2;
                    }
                    game.incrementMana(manaIncrementValue);

                    takeFirstCardFromDeckInHand();
                }
            } else if (action.getCommand().equals(Command.COMMAND_PLACE_CARD)) {
                try {
                    actionNode.put("handIdx", action.getHandIdx());
                    int returnCode = game.placeCard(action.getHandIdx());
                    switch (returnCode) {
                        case Command.COMMAND_PLACE_CARD_ERROR_ENVIRONMENT_CODE: actionNode
                                .put("error",
                                        Command.COMMAND_PLACE_CARD_ERROR_ENVIRONMENT_MESSAGE);
                        break;
                        case Command.COMMAND_PLACE_CARD_ERROR_NOT_ENOUGH_MANA_CODE: actionNode
                                .put("error",
                                        Command.COMMAND_PLACE_CARD_ERROR_NOT_ENOUGH_MANA_MESSAGE);
                        break;
                        case Command.COMMAND_PLACE_CARD_ERROR_FULL_ROW_CODE: actionNode
                                .put("error",
                                        Command.COMMAND_PLACE_CARD_ERROR_FULL_ROW_MESSAGE);
                        break;
                        default: break;
                    }
                    if (returnCode > 0) {
                        output.add(actionNode);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (action.getCommand().equals(Command.COMMAND_GET_CARDS_IN_HAND)) {
                actionNode.put("playerIdx", action.getPlayerIdx());
                actionNode.put("output", solveGetPlayerCardsInHand(action.getPlayerIdx()));
                output.add(actionNode);
            } else if (action.getCommand().equals(Command.COMMAND_GET_PLAYER_MANA)) {
                actionNode.put("playerIdx", action.getPlayerIdx());
                actionNode.put("output", getPlayerMana(action.getPlayerIdx()));
                output.add(actionNode);
            } else if (action.getCommand().equals(Command.COMMAND_GET_CARDS_ON_TABLE)) {
                actionNode.put("output", solveGetCardsOnTable());
                output.add(actionNode);
            } else if (action.getCommand().equals(Command.COMMAND_GET_ENVIRONMENT_CARDS_IN_HAND)) {
                actionNode.put("playerIdx", action.getPlayerIdx());
                actionNode.put("output", solveGetEnvironmentCardsInHand(action.getPlayerIdx()));
                output.add(actionNode);
            } else if (action.getCommand().equals(Command.COMMAND_GET_CARD_AT_POSITION)) {
                actionNode.put("x", action.getX());
                actionNode.put("y", action.getY());
                if (game.getTable().getTable()[action.getX()][action.getY()] == null) {
                    actionNode.put("output", "No card available at that position.");
                } else {
                    actionNode.put("output", getCardNodeFromCard(game.getTable()
                            .getTable()[action.getX()][action.getY()]));
                }
                output.add(actionNode);
            } else if (action.getCommand().equals(Command.COMMAND_USE_ENVIRONMENT_CARD)) {
                actionNode.put("handIdx", action.getHandIdx());
                actionNode.put("affectedRow", action.getAffectedRow());

                int returnCode = game.useEnvironmentCard(action.getHandIdx(),
                        action.getAffectedRow());
                String errorMsg = null;
                switch (returnCode) {
                    case Command.COMMAND_USE_ENV_CARD_NOT_ENV_CARD_ERROR_CODE:
                        errorMsg = Command.COMMAND_USE_ENV_CARD_NOT_ENV_CARD_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_USE_ENV_CARD_NOT_ENOUGH_MANA_ERROR_CODE:
                        errorMsg = Command.COMMAND_USE_ENV_CARD_NOT_ENOUGH_MANA_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_USE_ENV_CARD_CHOSEN_ROW_ERROR_CODE:
                        errorMsg = Command.COMMAND_USE_ENV_CARD_CHOSEN_ROW_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_USE_ENV_CARD_HEART_HOUND_ERROR_CODE:
                        errorMsg = Command.COMMAND_USE_ENV_CARD_HEART_HOUND_ERROR_MESSAGE;
                        break;
                    default: break;
                }
                if (returnCode > 0) {
                    actionNode.put("error", errorMsg);
                    output.add(actionNode);
                }
            } else if (action.getCommand().equals(Command.COMMAND_GET_FROZEN_CARDS_ON_TABLE)) {
                actionNode.put("output", solveGetFrozenCardsOnTable());
                output.add(actionNode);
            } else if (action.getCommand().equals(Command.COMMAND_CARD_USES_ATTACK)) {
                ObjectNode attackerNode = objectMapper.createObjectNode();
                attackerNode.put("x", action.getCardAttacker().getX());
                attackerNode.put("y", action.getCardAttacker().getY());

                ObjectNode attackedNode = objectMapper.createObjectNode();
                attackedNode.put("x", action.getCardAttacked().getX());
                attackedNode.put("y", action.getCardAttacked().getY());

                actionNode.put("cardAttacker", attackerNode);
                actionNode.put("cardAttacked", attackedNode);

                int resultCode = game.cardUsesAttack(action.getCardAttacker().getX(),
                                                    action.getCardAttacker().getY(),
                                                    action.getCardAttacked().getX(),
                                                    action.getCardAttacked().getY());
                String errorMessage = null;
                switch (resultCode) {
                    case Command.COMMAND_CARD_USES_ATTACK_NOT_ENEMY_CARD_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ATTACK_NOT_ENEMY_CARD_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_CARD_USES_ATTACK_ALREADY_ATTACKED_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ATTACK_ALREADY_ATTACKED_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_CARD_USES_ATTACK_FROZEN_CARD_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ATTACK_FROZEN_CARD_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_CARD_USES_ATTACK_TANK_CARD_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ATTACK_TANK_CARD_ERROR_MESSAGE;
                        break;
                    default: break;
                }

                if (resultCode > 0) {
                    actionNode.put("error", errorMessage);
                    output.add(actionNode);
                }
            } else if (action.getCommand().equals(Command.COMMAND_CARD_USES_ABILITY)) {
                ObjectNode attackerNode = objectMapper.createObjectNode();
                attackerNode.put("x", action.getCardAttacker().getX());
                attackerNode.put("y", action.getCardAttacker().getY());

                ObjectNode attackedNode = objectMapper.createObjectNode();
                attackedNode.put("x", action.getCardAttacked().getX());
                attackedNode.put("y", action.getCardAttacked().getY());

                actionNode.put("cardAttacker", attackerNode);
                actionNode.put("cardAttacked", attackedNode);

                int resultCode = game.cardUsesAbility(action.getCardAttacker().getX(),
                                                      action.getCardAttacker().getY(),
                        action.getCardAttacked().getX(), action.getCardAttacked().getY());
                String errorMessage = null;
                switch (resultCode) {
                    case Command.COMMAND_CARD_USES_ABILITY_FROZEN_CARD_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ABILITY_FROZEN_CARD_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_CARD_USES_ABILITY_ALREADY_ATTACKED_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ABILITY_ALREADY_ATTACKED_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_CARD_USES_ABILITY_DISCIPLE_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ABILITY_DISCIPLE_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_CARD_USES_ABILITY_NOT_ENEMY_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ABILITY_NOT_ENEMY_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_CARD_USES_ABILITY_TANK_CARD_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ABILITY_TANK_CARD_ERROR_MESSAGE;
                        break;
                    default: break;
                }

                if (resultCode > 0) {
                    actionNode.put("error", errorMessage);
                    output.add(actionNode);
                }
            } else if (action.getCommand().equals(Command.COMMAND_ATTACK_HERO)) {
                ObjectNode attackerNode = objectMapper.createObjectNode();
                attackerNode.put("x", action.getCardAttacker().getX());
                attackerNode.put("y", action.getCardAttacker().getY());


                int resultCode = game.cardAttackHero(action.getCardAttacker().getX(),
                                                     action.getCardAttacker().getY());
                String errorMessage = null;
                String wonMessage = null;
                switch (resultCode) {
                    case Command.COMMAND_CARD_USES_ABILITY_FROZEN_CARD_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ABILITY_FROZEN_CARD_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_CARD_USES_ABILITY_ALREADY_ATTACKED_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ABILITY_ALREADY_ATTACKED_ERROR_MESSAGE;
                        break;
                    case Command.COMMAND_CARD_USES_ABILITY_TANK_CARD_ERROR_CODE:
                        errorMessage = Command
                                .COMMAND_CARD_USES_ABILITY_TANK_CARD_ERROR_MESSAGE;
                        break;
                    case Command.CODE_PLAYER_ONE_WON:
                        wonMessage = "Player one killed the enemy hero."; break;
                    case Command.CODE_PLAYER_TWO_WON:
                        wonMessage = "Player two killed the enemy hero."; break;
                    default: break;
                }

                if (errorMessage != null) {
                    actionNode.put("cardAttacker", attackerNode);
                    actionNode.put("error", errorMessage);
                    output.add(actionNode);
                } else if (wonMessage != null) {
                    output.add(objectMapper.createObjectNode().put("gameEnded", wonMessage));
                    if (wonMessage.contains("one")) {
                        multipleGamesManager.playerOneWon();
                    } else if (wonMessage.contains("two")) {
                        multipleGamesManager.playerTwoWon();
                    }
                }
            } else if (action.getCommand().equals(Command.COMMAND_USE_HERO_ABILITY)) {
                actionNode.put("affectedRow", action.getAffectedRow());
                int returnCode = game.useHeroAbility(action.getAffectedRow());
                String errorMessage = null;
                switch (returnCode) {
                    case Command.ERROR_CODE_NOT_ENOUGH_MANA_HERO_ABILITY:
                        errorMessage = "Not enough mana to use hero's ability."; break;
                    case Command.ERROR_CODE_HERO_ALREADY_ATTACKED:
                        errorMessage = "Hero has already attacked this turn."; break;
                    case Command.ERROR_CODE_NOT_ENEMY_ROW:
                        errorMessage = "Selected row does not belong to the enemy."; break;
                    case Command.ERROR_CODE_NOT_OWN_ROW:
                        errorMessage = "Selected row does not belong to the current player."; break;
                    default: break;
                }

                if (errorMessage != null) {
                    actionNode.put("error", errorMessage);
                    output.add(actionNode);
                }
            } else if (action.getCommand().equals("getPlayerOneWins")) {
                actionNode.put("output", multipleGamesManager.getPlayerOneWins());
                output.add(actionNode);
            } else if (action.getCommand().equals("getPlayerTwoWins")) {
                actionNode.put("output", multipleGamesManager.getPlayerTwoWins());
                output.add(actionNode);
            } else if (action.getCommand().equals("getTotalGamesPlayed")) {
                actionNode.put("output", multipleGamesManager.getTotalGamesPlayed());
                output.add(actionNode);
            }
        }
    }

    /**
     *
     * @return
     */
    private ArrayNode solveGetFrozenCardsOnTable() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode result = objectMapper.createArrayNode();
        Card[][] currentTable = game.getTable().getTable();
        for (int i = 0; i < GameTable.TABLE_ROWS; i++) {
            for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                Card card = currentTable[i][j];
                if (card instanceof MinionCard) {
                    if (((MinionCard) card).isFrozen()) {
                        result.add(getCardNodeFromCard(card));
                    }
                }
            }
        }
        return result;
    }

    /**
     *
     * @param playerIdx
     * @return
     */
    private ArrayNode solveGetEnvironmentCardsInHand(final int playerIdx) {
        Player player = game.getPlayer(playerIdx - 1);
        List<Card> cards = player.getCardsInHand();
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode output = objectMapper.createArrayNode();

        for (Card card : cards) {
            if (card instanceof EnvironmentCard) {
                output.add(getCardNodeFromCard(card));
            }
        }
        return output;
    }

    /**
     *
     * @return
     */
    private ArrayNode solveGetCardsOnTable() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode result = objectMapper.createArrayNode();
        GameTable table = game.getTable();
        for (int i = 0; i < GameTable.TABLE_ROWS; i++) {
            ArrayNode rowNode = objectMapper.createArrayNode();
            for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                if (table.getTable()[i][j] != null) {
                    rowNode.add(getCardNodeFromCard(table.getTable()[i][j]));
                }
            }
            result.add(rowNode);
        }
        return result;
    }

    /**
     *
     * @param card
     * @return
     */
    private ObjectNode getCardNodeFromCard(final Card card) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode cardNode = objectMapper.createObjectNode();
        // mana
        cardNode.put("mana", card.getMana());
        // description
        cardNode.put("description", card.getDescription());
        // colors
        ArrayNode colorsNode = objectMapper.createArrayNode();
        for (String color : card.getColors()) {
            colorsNode.add(color);
        }
        cardNode.put("colors", colorsNode);
        //name
        cardNode.put("name", card.getName());

        if (card instanceof MinionCard || card instanceof HeroCard) {
            // health
            int healthValue = 0;
            if (card instanceof MinionCard) {
                healthValue = ((MinionCard) card).getHealth();
                int attackDamageValue = ((MinionCard) card).getAttackDamage();
                cardNode.put("attackDamage", attackDamageValue);
            } else {
                healthValue = ((HeroCard) card).getHealth();
            }
            cardNode.put("health", healthValue);
        }
        return cardNode;
    }

    /**
     *
     * @param playerIdx
     * @return
     */
    private ArrayNode solveGetPlayerDeck(final int playerIdx) {
        Player player = game.getPlayer(playerIdx - 1);
        List<Card> cards = player.getCurrentPickedDeck().getCards();
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode output = objectMapper.createArrayNode();

        for (Card card : cards) {
            output.add(getCardNodeFromCard(card));
        }
        return output;
    }

    /**
     *
     * @param playerIdx
     * @return
     */
    private ArrayNode solveGetPlayerCardsInHand(final int playerIdx) {
        Player player = game.getPlayer(playerIdx - 1);
        List<Card> cards = player.getCardsInHand();
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode output = objectMapper.createArrayNode();

        for (Card card : cards) {
            output.add(getCardNodeFromCard(card));
        }
        return output;
    }

    /**
     *
     * @param playerIdx
     * @return
     */
    private ObjectNode solveGetPlayerHero(final int playerIdx) {
        Player player = game.getPlayer(playerIdx - 1);
        Card heroCard = player.getHero();
        return getCardNodeFromCard(heroCard);
    }

    /**
     *
     * @param playerIdx
     * @return
     */
    private int getPlayerMana(final int playerIdx) {
        Player player = game.getPlayer(playerIdx - 1);
        return player.getCurrentMana();
    }

    /**
     *
     * @param input
     * @throws Exception
     */
    private void fillPlayerDecks(final Input input) throws Exception {
        game.setPlayerDecks(PLAYER_ONE_ID, input.getPlayerOneDecks().getNrDecks(),
                buildDecksFromInput(input.getPlayerOneDecks().getDecks()));
        game.setPlayerDecks(PLAYER_TWO_ID, input.getPlayerTwoDecks().getNrDecks(),
                buildDecksFromInput(input.getPlayerTwoDecks().getDecks()));
    }

    /**
     *
     * @param startGameInput
     * @throws Exception
     */
    private void setPlayersHeroCards(final StartGameInput startGameInput) throws Exception {
        game.setPlayersHeros(PLAYER_ONE_ID, (HeroCard) CardFactory.
                createCardFromInput(startGameInput.getPlayerOneHero()));
        game.setPlayersHeros(PLAYER_TWO_ID, (HeroCard) CardFactory.
                createCardFromInput(startGameInput.getPlayerTwoHero()));
    }

    /**
     *
     * @param startGameInput
     */
    private void setPlayersInitialDeck(final StartGameInput startGameInput) {
        game.setPlayersInitialDeck(PLAYER_ONE_ID, startGameInput.getPlayerOneDeckIdx());
        game.setPlayersInitialDeck(PLAYER_TWO_ID, startGameInput.getPlayerTwoDeckIdx());
    }

    /**
     *
     */
    private void shuffleDecks() {
        game.shufflePlayersPickedDeck();
    }

    /**
     *
     */
    private void takeFirstCardFromDeckInHand() {
        game.takeFirstCardFromDeck(PLAYER_ONE_ID);
        game.takeFirstCardFromDeck(PLAYER_TWO_ID);
    }

    /**
     *
     * @param decks
     * @return
     * @throws Exception
     */
    private List<Deck> buildDecksFromInput(final ArrayList<ArrayList<CardInput>> decks)
            throws Exception {
        int totalDecks = decks.size();
        List<Deck> decksList = new ArrayList<>();
        for (int i = 0; i < totalDecks; i++) {
            int totalCardsInDeck = decks.get(i).size();
            Deck ithDeck = new Deck(totalCardsInDeck);
            for (int j = 0; j < totalCardsInDeck; j++) {
                CardInput cardInput = decks.get(i).get(j);
                ithDeck.addCard(CardFactory.createCardFromInput(cardInput));
            }
            decksList.add(ithDeck);
        }
        return decksList;
    }
}
