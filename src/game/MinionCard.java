package game;

import java.util.List;

public class MinionCard extends Card {
    private int health;
    private int attackDamage;
    private boolean frozen;
    private int roundsUntilUnfreeze = 0;
    private boolean alreadyAttackedThisRound = false;

    public MinionCard(final int mana, final String description, final List<String> colors,
                      final String name, final int health, final int attackDamage) {
        super(mana, description, colors, name);
        this.health = health;
        this.attackDamage = attackDamage;
        this.frozen = false;
    }

    public final int getHealth() {
        return health;
    }

    public final void setHealth(final int health) {
        this.health = health;
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    public final void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public final boolean isFrozen() {
        return frozen;
    }

    /**
     *
     */
    public void checkDecrementRoundsUntilUnfreeze() {
        if (roundsUntilUnfreeze > 0) {
            roundsUntilUnfreeze--;
            if (roundsUntilUnfreeze == 0) {
                setFrozen(false);
            }
        }
    }

    /**
     *
     * @param frozen
     */
    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
        if (frozen) {
            roundsUntilUnfreeze = 2;
        }
    }

    public final boolean isAlreadyAttackedThisRound() {
        return alreadyAttackedThisRound;
    }

    public final void setAlreadyAttackedThisRound(final boolean alreadyAttackedThisRound) {
        this.alreadyAttackedThisRound = alreadyAttackedThisRound;
    }
}
