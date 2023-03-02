package game;

public class GameTable {
    public static final int TABLE_ROWS = 4;
    public static final int TABLE_COLUMNS = 5;
    private Card[][] table;

    public GameTable() {
        table = new Card[TABLE_ROWS][TABLE_COLUMNS];
    }

    public final Card[][] getTable() {
        return table;
    }

    public final void setTable(final Card[][] table) {
        this.table = table;
    }

    /**
     *
     * @param whoseTurn
     * @param selectedCard
     * @return
     * @throws Exception
     */
    public boolean fullRow(final int whoseTurn, final Card selectedCard) throws Exception {
        int three = 2 + 1;
        if (!(selectedCard instanceof MinionCard)) {
            throw new Exception("Invalid card");
        }
        if (selectedCard.getName().equals(CardFactory.CARD_THE_RIPPER)
            || selectedCard.getName().equals(CardFactory.CARD_MIRAJ)
            || selectedCard.getName().equals(CardFactory.CARD_GOLIATH)
            || selectedCard.getName().equals(CardFactory.CARD_WARDEN)) {
            if (whoseTurn == 0) {
                // row 2
                return isRowFull(2);
            } else {
                // row 1
                return isRowFull(1);
            }
        } else if (selectedCard.getName().equals(CardFactory.CARD_SENTINEL)
                    || selectedCard.getName().equals(CardFactory.CARD_BERSERKER)
                    || selectedCard.getName().equals(CardFactory.CARD_THE_CURSED_ONE)
                    || selectedCard.getName().equals(CardFactory.CARD_DISCIPLE)) {
            if (whoseTurn == 0) {
                // row 3
                return isRowFull(three);
            } else {
                // row 0
                return isRowFull(0);
            }
        }
        throw new Exception("Invalid card");
    }

    /**
     *
     * @param row
     * @return
     */
    public boolean isRowFull(final int row) {
        int cardsOnRow = 0;
        for (int i = 0; i < TABLE_COLUMNS; i++) {
            if (table[row][i] != null) {
                cardsOnRow++;
            }
        }
        return cardsOnRow == TABLE_COLUMNS;
    }

    /**
     *
     * @param row
     * @param card
     */
    public void addCardOnRow(final int row, final Card card) {
        for (int i = 0; i < TABLE_COLUMNS; i++) {
            if (table[row][i] == null) {
                table[row][i] = card;
                return;
            }
        }
    }

    /**
     *
     * @param row
     * @param card
     */
    public void removeCardFromRow(final int row, final Card card) {
        for (int i = 0; i < TABLE_COLUMNS; i++) {
            if (table[row][i] == card) {
                for (int j = i + 1; j < TABLE_COLUMNS; j++) {
                    table[row][j - 1] = table[row][j];
                }
                table[row][TABLE_COLUMNS - 1] = null;
            }
        }
    }

    /**
     *
     * @param whoseTurn
     * @param selectedCard
     */
    public void putOnTable(final int whoseTurn, final Card selectedCard) {
        int three = 2 + 1;
        if (selectedCard.getName().equals(CardFactory.CARD_THE_RIPPER)
                || selectedCard.getName().equals(CardFactory.CARD_MIRAJ)
                || selectedCard.getName().equals(CardFactory.CARD_GOLIATH)
                || selectedCard.getName().equals(CardFactory.CARD_WARDEN)) {
            if (whoseTurn == 0) {
                // row 2
                addCardOnRow(2, selectedCard);
            } else {
                // row 1
                addCardOnRow(1, selectedCard);
            }
        } else if (selectedCard.getName().equals(CardFactory.CARD_SENTINEL)
                || selectedCard.getName().equals(CardFactory.CARD_BERSERKER)
                || selectedCard.getName().equals(CardFactory.CARD_THE_CURSED_ONE)
                || selectedCard.getName().equals(CardFactory.CARD_DISCIPLE)) {
            if (whoseTurn == 0) {
                // row 3
                addCardOnRow(three, selectedCard);
            } else {
                // row 0
                addCardOnRow(0, selectedCard);
            }
        }
    }
}
