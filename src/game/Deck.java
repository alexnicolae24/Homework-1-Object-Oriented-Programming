package game;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private final int nrCardsInDeck;
    private final List<Card> cards;

    public Deck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
        this.cards = new ArrayList<>();
    }

    public final List<Card> getCards() {
        return cards;
    }

    /**
     *
     * @param card
     */
    public void addCard(final Card card) {
        cards.add(card);
    }

}
