package game;

import java.util.List;

public class HeroCard extends Card {
    private int health;
    private boolean alreadyAttackedThisRound = false;

    public HeroCard(final int mana, final String description,
                    final List<String> colors, final String name) {
        super(mana, description, colors, name);

        int thirty = 2 * 2 * 2 * 2 * 2 - 2; //30
        this.health = thirty;
    }

    public final int getHealth() {
        return health;
    }

    public final void setHealth(final int health) {
        this.health = health;
    }

    /**
     *
     * @return
     */
    public final boolean isAlreadyAttackedThisRound() {
        return alreadyAttackedThisRound;
    }

    /**
     *
     * @param alreadyAttackedThisRound
     */
    public final void setAlreadyAttackedThisRound(final boolean alreadyAttackedThisRound) {
        this.alreadyAttackedThisRound = alreadyAttackedThisRound;
    }
}
