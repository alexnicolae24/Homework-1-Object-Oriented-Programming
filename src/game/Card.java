package game;

import java.util.List;

public abstract class Card {
    private final int mana;
    private final String description;
    private final List<String> colors;
    private final String name;

    public Card(final int mana, final String description,
                final List<String> colors, final String name) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }

    public final int getMana() {
        return mana;
    }

    public final String getDescription() {
        return description;
    }

    public final List<String> getColors() {
        return colors;
    }

    public final String getName() {
        return name;
    }

    public final boolean isCardTank() {
        return name.equals(CardFactory.CARD_GOLIATH) || name.equals(CardFactory.CARD_WARDEN);
    }
}
