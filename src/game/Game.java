package game;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Game {
    private static final int NUMBER_OF_PLAYERS = 2;
    private Player[] players;
    private GameTable table;
    private int whoseTurn;
    private int shuffleSeed;


    public Game() {
        players = new Player[NUMBER_OF_PLAYERS];
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            players[i] = new Player();
        }
        table = new GameTable();
    }

    /**
     *
     * @param whichPlayer
     * @param nrDecks
     * @param decks
     */
    public void setPlayerDecks(final int whichPlayer, final int nrDecks, final List<Deck> decks) {
        players[whichPlayer].initDecks(nrDecks, decks);
    }

    /**
     *
     * @param whichPlayer
     * @param heroCard
     */
    public void setPlayersHeros(final int whichPlayer, final HeroCard heroCard) {
        players[whichPlayer].setHero(heroCard);
    }

    /**
     *
     * @param whichPlayer
     * @param whichDeck
     */
    public void setPlayersInitialDeck(final int whichPlayer, final int whichDeck) {
        players[whichPlayer].setPickedDeck(whichDeck);
    }

    /**
     *
     * @param whichPlayer
     */
    public void takeFirstCardFromDeck(final int whichPlayer) {
        players[whichPlayer].pickFirstCard();
    }

    public final int getWhoseTurn() {
        return whoseTurn;
    }

    public final void setWhoseTurn(final int whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public final int getShuffleSeed() {
        return shuffleSeed;
    }

    public final void setShuffleSeed(final int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    /**
     *
     * @param whichPlayer
     * @return
     */
    public final Player getPlayer(final int whichPlayer) {
        return players[whichPlayer];
    }

    /**
     *
     */
    public void shufflePlayersPickedDeck() {
        List<Card> playerOneDeck = players[0].getCurrentPickedDeck().getCards();
        Collections.shuffle(playerOneDeck, new Random(shuffleSeed));
        List<Card> playerTwoDeck = players[1].getCurrentPickedDeck().getCards();
        Collections.shuffle(playerTwoDeck, new Random(shuffleSeed));
    }

    /**
     *
     */
    public final void endPlayerTurn() {
        if (whoseTurn == 0) {
            whoseTurn = 1;
        } else {
            whoseTurn = 0;
        }
    }

    /**
     *
     * @param value
     */
    public void incrementMana(final int value) {
        players[0].setCurrentMana(players[0].getCurrentMana() + value);
        players[1].setCurrentMana(players[1].getCurrentMana() + value);
    }

    /**
     *
     * @param handIdx
     * @return
     * @throws Exception
     */
    public int placeCard(final int handIdx) throws Exception {
        try {
            Card selectedCard = players[whoseTurn].getCardsInHand().get(handIdx);
            if (selectedCard instanceof EnvironmentCard) {
                return Command.COMMAND_PLACE_CARD_ERROR_ENVIRONMENT_CODE;
            }
            if (selectedCard.getMana() > players[whoseTurn].getCurrentMana()) {
                return Command.COMMAND_PLACE_CARD_ERROR_NOT_ENOUGH_MANA_CODE;
            }
            if (table.fullRow(whoseTurn, selectedCard)) {
                return Command.COMMAND_PLACE_CARD_ERROR_FULL_ROW_CODE;
            }
            players[whoseTurn].getCardsInHand().remove(selectedCard);
            table.putOnTable(whoseTurn, selectedCard);
            players[whoseTurn].setCurrentMana(players[whoseTurn]
                              .getCurrentMana() - selectedCard.getMana());
            return 0;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid card idx!");
            return -1;
        }
    }

    public final GameTable getTable() {
        return table;
    }

    public final void setTable(final GameTable table) {
        this.table = table;
    }

    /**
     *
     * @param handIdx
     * @param affectedRow
     * @return
     */
    public final int useEnvironmentCard(final int handIdx, final int affectedRow) {
        int three = 1 + 2;
        try {
            Card selectedCard = players[whoseTurn].getCardsInHand().get(handIdx);
            if (!(selectedCard instanceof EnvironmentCard)) {
                return Command.COMMAND_USE_ENV_CARD_NOT_ENV_CARD_ERROR_CODE;
            }
            if (players[whoseTurn].getCurrentMana() < selectedCard.getMana()) {
                return Command.COMMAND_USE_ENV_CARD_NOT_ENOUGH_MANA_ERROR_CODE;
            }
            if ((whoseTurn == 0 && !(affectedRow == 0
                    || affectedRow == 1))
                    || (whoseTurn == 1 && !(affectedRow == 2
                    || affectedRow == three))) {
                return Command.COMMAND_USE_ENV_CARD_CHOSEN_ROW_ERROR_CODE;
            }
            if (selectedCard.getName().equals(CardFactory.CARD_HEART_HOUND)) {
                // check mirrored row
                int mirroredRow = -1;
                if (whoseTurn == 0) {
                    if (affectedRow == 0) {
                        mirroredRow = three;
                    } else {
                        mirroredRow = 2;
                    }
                } else {
                    if (affectedRow == 2) {
                        mirroredRow = 1;
                    } else {
                        mirroredRow = 0;
                    }
                }
                if (table.isRowFull(mirroredRow)) {
                    return Command.COMMAND_USE_ENV_CARD_HEART_HOUND_ERROR_CODE;
                } else {
                    // steal card
                    Card cardToBeStolen = null;
                    int maxHealth = -1;
                    for (Card card : table.getTable()[affectedRow]) {
                        if (card instanceof MinionCard) {
                            if (((MinionCard) card).getHealth() > maxHealth) {
                                maxHealth = ((MinionCard) card).getHealth();
                                cardToBeStolen = card;
                            }
                        }
                    }
                    table.removeCardFromRow(affectedRow, cardToBeStolen);
                    table.addCardOnRow(mirroredRow, cardToBeStolen);
                }
            }

            if (selectedCard.getName().equals(CardFactory.CARD_FIRESTORM)) {
                for (Card card : table.getTable()[affectedRow]) {
                    if (card instanceof MinionCard) {
                        ((MinionCard) card).setHealth(((MinionCard) card).getHealth() - 1);
                    }
                }
                Card[][] currentTable = table.getTable();
                for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                    Card card = currentTable[affectedRow][j];
                    if (card instanceof MinionCard) {
                        if (((MinionCard) card).getHealth() == 0) {
                            table.removeCardFromRow(affectedRow, card);
                            j--;
                        }
                    }
                }
            } else if (selectedCard.getName().equals(CardFactory.CARD_WINTERFELL)) {
                for (Card card : table.getTable()[affectedRow]) {
                    if (card instanceof MinionCard) {
                        ((MinionCard) card).setFrozen(true);
                    }
                }
            }
            players[whoseTurn].getCardsInHand().remove(selectedCard);
            players[whoseTurn].setCurrentMana(players[whoseTurn]
                              .getCurrentMana() - selectedCard.getMana());
            return 0;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid card idx!");
            return -1;
        }
    }

    /**
     *
     * @param attackerX
     * @param attackerY
     * @param attackedX
     * @param attackedY
     * @return
     */
    public int cardUsesAttack(final int attackerX, final int attackerY,
                              final int attackedX, final int attackedY) {
        int tankCards = 0;
        int three = 1 + 2;
        if (whoseTurn == 0) {
            if (!(attackedX == 0 || attackedX == 1)) {
                return Command.COMMAND_CARD_USES_ATTACK_NOT_ENEMY_CARD_ERROR_CODE;
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                    if (table.getTable()[i][j] != null && table.getTable()[i][j].isCardTank()) {
                        tankCards++;
                    }
                }
            }
        } else {
            if (!(attackedX == 2 || attackedX == three)) {
                return Command.COMMAND_CARD_USES_ATTACK_NOT_ENEMY_CARD_ERROR_CODE;
            }
            for (int i = 2; i < GameTable.TABLE_ROWS; i++) {
                for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                    if (table.getTable()[i][j] != null && table.getTable()[i][j].isCardTank()) {
                        tankCards++;
                    }
                }
            }
        }
        Card attackerCard = table.getTable()[attackerX][attackerY];
        if (attackerCard instanceof MinionCard) {
            if (((MinionCard) attackerCard).isAlreadyAttackedThisRound()) {
                return Command.COMMAND_CARD_USES_ATTACK_ALREADY_ATTACKED_ERROR_CODE;
            }
            if (((MinionCard) attackerCard).isFrozen()) {
                return Command.COMMAND_CARD_USES_ATTACK_FROZEN_CARD_ERROR_CODE;
            }
        }
        Card attackedCard = table.getTable()[attackedX][attackedY];
        if (tankCards > 0) {
            if (!attackedCard.isCardTank()) {
                return Command.COMMAND_CARD_USES_ATTACK_TANK_CARD_ERROR_CODE;
            }
        }

        MinionCard attackerCardMinion = (MinionCard) attackerCard;
        MinionCard attackedCardMinion = (MinionCard) attackedCard;
        attackerCardMinion.setAlreadyAttackedThisRound(true);
        attackedCardMinion.setHealth(attackedCardMinion.getHealth()
                           - attackerCardMinion.getAttackDamage());
        if (attackedCardMinion.getHealth() <= 0) {
            table.removeCardFromRow(attackedX, attackedCardMinion);
        }
        return 0;
    }

    /**
     *
     */
    public void setCurrentPlayerAlreadyAttackedCardsToFalse() {
        if (whoseTurn == 0) {
            for (int i = 2; i < GameTable.TABLE_ROWS; i++) {
                for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                    if (table.getTable()[i][j] instanceof MinionCard) {
                        ((MinionCard) table.getTable()[i][j]).setAlreadyAttackedThisRound(false);
                    }
                }
            }
            players[0].getHero().setAlreadyAttackedThisRound(false);
        } else {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                    if (table.getTable()[i][j] instanceof MinionCard) {
                        ((MinionCard) table.getTable()[i][j]).setAlreadyAttackedThisRound(false);
                    }
                }
            }
            players[1].getHero().setAlreadyAttackedThisRound(false);
        }
    }

    /**
     *
     * @param attackerX
     * @param attackerY
     * @param attackedX
     * @param attackedY
     * @return
     */
    public int cardUsesAbility(final int attackerX, final int attackerY,
                               final int attackedX, final int attackedY) {
        MinionCard attackerCard = (MinionCard) table.getTable()[attackerX][attackerY];
        MinionCard attackedCard = (MinionCard) table.getTable()[attackedX][attackedY];
        int three = 1 + 2;

        if (attackerCard.isFrozen()) {
            return Command.COMMAND_CARD_USES_ABILITY_FROZEN_CARD_ERROR_CODE;
        }
        if (attackerCard.isAlreadyAttackedThisRound()) {
            return Command.COMMAND_CARD_USES_ABILITY_ALREADY_ATTACKED_ERROR_CODE;
        }
        if (attackerCard.getName().equals(CardFactory.CARD_DISCIPLE)) {
            if (whoseTurn == 0) {
                if (attackedX == 0 || attackedX == 1) {
                    return Command.COMMAND_CARD_USES_ABILITY_DISCIPLE_ERROR_CODE;
                }
            } else {
                if (attackedX == 2 || attackedX == three) {
                    return Command.COMMAND_CARD_USES_ABILITY_DISCIPLE_ERROR_CODE;
                }
            }
            attackedCard.setHealth(attackedCard.getHealth() + 2);
        } else if (attackerCard.getName().equals(CardFactory.CARD_THE_RIPPER)
                    || attackerCard.getName().equals(CardFactory.CARD_MIRAJ)
                    || attackerCard.getName().equals(CardFactory.CARD_THE_CURSED_ONE)) {
            int tankCards = 0;
            if (whoseTurn == 0) {
                if (attackedX == 2 || attackedX == three) {
                    return Command.COMMAND_CARD_USES_ABILITY_NOT_ENEMY_ERROR_CODE;
                }
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                        if (table.getTable()[i][j] != null && table.getTable()[i][j].isCardTank()) {
                            tankCards++;
                        }
                    }
                }
            } else {
                if (attackedX == 0 || attackedX == 1) {
                    return Command.COMMAND_CARD_USES_ABILITY_NOT_ENEMY_ERROR_CODE;
                }
                for (int i = 2; i < GameTable.TABLE_ROWS; i++) {
                    for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                        if (table.getTable()[i][j] != null && table.getTable()[i][j].isCardTank()) {
                            tankCards++;
                        }
                    }
                }
            }

            if (tankCards > 0) {
                if (!attackedCard.isCardTank()) {
                    return Command.COMMAND_CARD_USES_ABILITY_TANK_CARD_ERROR_CODE;
                }
            }

            if (attackerCard.getName().equals(CardFactory.CARD_THE_RIPPER)) {
                attackedCard.setAttackDamage(attackedCard.getAttackDamage() - 2);
            } else if (attackerCard.getName().equals(CardFactory.CARD_MIRAJ)) {
                int currentHealth = attackerCard.getHealth();
                int enemyHealth = attackedCard.getHealth();
                attackerCard.setHealth(enemyHealth);
                attackedCard.setHealth(currentHealth);
            } else if (attackerCard.getName().equals(CardFactory.CARD_THE_CURSED_ONE)) {
                int enemyHealth = attackedCard.getHealth();
                int enemyAttackDamage = attackedCard.getAttackDamage();
                attackedCard.setHealth(enemyAttackDamage);
                attackedCard.setAttackDamage(enemyHealth);
            }
            if (attackedCard.getHealth() <= 0) {
                table.removeCardFromRow(attackedX, attackedCard);
            }
            if (attackedCard.getAttackDamage() < 0) {
                attackedCard.setAttackDamage(0);
            }
        }

        attackerCard.setAlreadyAttackedThisRound(true);
        return 0;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public int cardAttackHero(final int x, final int y) {
        MinionCard attackerCard = (MinionCard) table.getTable()[x][y];
        if (attackerCard.isFrozen()) {
            return Command.COMMAND_CARD_USES_ABILITY_FROZEN_CARD_ERROR_CODE;
        }
        if (attackerCard.isAlreadyAttackedThisRound()) {
            return Command.COMMAND_CARD_USES_ABILITY_ALREADY_ATTACKED_ERROR_CODE;
        }
        int tankCards = 0;
        if (whoseTurn == 0) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                    if (table.getTable()[i][j] != null && table.getTable()[i][j].isCardTank()) {
                        tankCards++;
                    }
                }
            }
        } else {
            for (int i = 2; i < GameTable.TABLE_ROWS; i++) {
                for (int j = 0; j < GameTable.TABLE_COLUMNS; j++) {
                    if (table.getTable()[i][j] != null && table.getTable()[i][j].isCardTank()) {
                        tankCards++;
                    }
                }
            }
        }
        if (tankCards > 0) {
            return Command.COMMAND_CARD_USES_ABILITY_TANK_CARD_ERROR_CODE;
        }

        HeroCard enemyHero = null;
        if (whoseTurn == 0) {
            enemyHero = players[1].getHero();
        } else {
            enemyHero = players[0].getHero();

        }
        attackerCard.setAlreadyAttackedThisRound(true);
        int currentHealth = enemyHero.getHealth();
        enemyHero.setHealth(currentHealth - attackerCard.getAttackDamage());
        if (enemyHero.getHealth() <= 0) {
            if (whoseTurn == 0) {
                return Command.CODE_PLAYER_ONE_WON;
            } else {
                return Command.CODE_PLAYER_TWO_WON;
            }
        }
        return 0;
    }

    /**
     *
     * @param affectedRow
     * @return
     */
    public int useHeroAbility(final int affectedRow) {
        HeroCard hero = players[whoseTurn].getHero();
        if (players[whoseTurn].getCurrentMana() < hero.getMana()) {
            return Command.ERROR_CODE_NOT_ENOUGH_MANA_HERO_ABILITY;
        }
        if (hero.isAlreadyAttackedThisRound()) {
            return Command.ERROR_CODE_HERO_ALREADY_ATTACKED;
        }
        if (hero.getName().equals(CardFactory.CARD_LORD_ROYCE)
            || hero.getName().equals(CardFactory.CARD_EMPRESS_THORINA)) {
            if (whoseTurn == 0) {
                if (affectedRow == 2 || affectedRow == 1 + 2) {
                    return Command.ERROR_CODE_NOT_ENEMY_ROW;
                }
            } else {
                if (affectedRow == 0 || affectedRow == 1) {
                    return Command.ERROR_CODE_NOT_ENEMY_ROW;
                }
            }
        } else if (hero.getName().equals(CardFactory.CARD_GENERAL_KOCIORAW)
                    || hero.getName().equals(CardFactory.CARD_KING_MUDFACE)) {
            if (whoseTurn == 0) {
                if (affectedRow == 0 || affectedRow == 1) {
                    return Command.ERROR_CODE_NOT_OWN_ROW;
                }
            } else {
                if (affectedRow == 2 || affectedRow == 1 + 2) {
                    return Command.ERROR_CODE_NOT_OWN_ROW;
                }
            }
        }

        players[whoseTurn].setCurrentMana(players[whoseTurn].getCurrentMana() - hero.getMana());
        hero.setAlreadyAttackedThisRound(true);

        if (hero.getName().equals(CardFactory.CARD_LORD_ROYCE)) {
            int maxDamage = -1;
            Card cardToBeFrozen = null;
            for (Card card : table.getTable()[affectedRow]) {
                if (card instanceof MinionCard) {
                    if (((MinionCard) card).getAttackDamage() >= maxDamage) {
                        maxDamage = ((MinionCard) card).getAttackDamage();
                        cardToBeFrozen = card;
                    }
                }
            }
            if (cardToBeFrozen != null) {
                ((MinionCard) cardToBeFrozen).setFrozen(true);
            }
        } else if (hero.getName().equals(CardFactory.CARD_EMPRESS_THORINA)) {
                int maxHealth = -1;
                Card cardToBeDestroyed = null;
                for (Card card : table.getTable()[affectedRow]) {
                    if (card instanceof MinionCard) {
                        if (((MinionCard) card).getHealth() > maxHealth) {
                            maxHealth = ((MinionCard) card).getHealth();
                            cardToBeDestroyed = card;
                        }
                    }
                }
                if (cardToBeDestroyed != null) {
                    table.removeCardFromRow(affectedRow, cardToBeDestroyed);
                }
        } else if (hero.getName().equals(CardFactory.CARD_KING_MUDFACE)) {
            for (Card card : table.getTable()[affectedRow]) {
                if (card != null) {
                    ((MinionCard) card).setHealth(((MinionCard) card).getHealth() + 1);
                }
            }
        } else if (hero.getName().equals(CardFactory.CARD_GENERAL_KOCIORAW)) {
            for (Card card : table.getTable()[affectedRow]) {
                if (card != null) {
                    ((MinionCard) card).setAttackDamage(((MinionCard) card).getAttackDamage() + 1);
                }
            }
        }
        return 0;
    }
}
