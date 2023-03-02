package game;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private int nrDecks;
    private List<Deck> decks;
    private int pickedDeck;
    private HeroCard hero;
    private List<Card> cardsInHand;
    private int currentMana;

    public Player() {
        cardsInHand = new ArrayList<>();
    }

    /**
     *
     * @param nrDecks
     * @param decks
     */
    public final void initDecks(final int nrDecks, final List<Deck> decks) {
        this.nrDecks = nrDecks;
        this.decks = decks;
    }

    public final int getNrDecks() {
        return nrDecks;
    }

    public final void setNrDecks(final int nrDecks) {
        this.nrDecks = nrDecks;
    }

    public final List<Deck> getDecks() {
        return decks;
    }

    public final void setDecks(final List<Deck> decks) {
        this.decks = decks;
    }

    public final int getPickedDeck() {
        return pickedDeck;
    }

    public final void setPickedDeck(final int pickedDeck) {
        this.pickedDeck = pickedDeck;
    }

    public final HeroCard getHero() {
        return hero;
    }

    public final void setHero(final HeroCard hero) {
        this.hero = hero;
    }

    public final Deck getCurrentPickedDeck() {
        return decks.get(pickedDeck);
    }

    /**
     *
     */
    public final void pickFirstCard() {
        if (decks.get(pickedDeck).getCards().size() > 0) {
            Card firstCard = decks.get(pickedDeck).getCards().get(0);
            getCurrentPickedDeck().getCards().remove(0);
            cardsInHand.add(firstCard);
        }
    }

    public final List<Card> getCardsInHand() {
        return cardsInHand;
    }

    public final void setCardsInHand(final List<Card> cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public final int getCurrentMana() {
        return currentMana;
    }

    public final void setCurrentMana(final int currentMana) {
        this.currentMana = currentMana;
    }
}
