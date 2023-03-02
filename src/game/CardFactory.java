package game;

import fileio.CardInput;

public class CardFactory {
    // Minion cards
    public static final String CARD_THE_RIPPER = "The Ripper";
    public static final String CARD_MIRAJ = "Miraj";
    public static final String CARD_THE_CURSED_ONE = "The Cursed One";
    public static final String CARD_DISCIPLE = "Disciple";
    public static final String CARD_SENTINEL = "Sentinel";
    public static final String CARD_BERSERKER = "Berserker";
    public static final String CARD_GOLIATH = "Goliath";
    public static final String CARD_WARDEN = "Warden";
    // Environment cards
    public static final String CARD_FIRESTORM = "Firestorm";
    public static final String CARD_WINTERFELL = "Winterfell";
    public static final String CARD_HEART_HOUND = "Heart Hound";
    // Hero cards
    public static final String CARD_LORD_ROYCE = "Lord Royce";
    public static final String CARD_EMPRESS_THORINA = "Empress Thorina";
    public static final String CARD_KING_MUDFACE = "King Mudface";
    public static final String CARD_GENERAL_KOCIORAW = "General Kocioraw";

    /**
     *
     * @param cardInput
     * @return
     * @throws Exception
     */

    public static Card createCardFromInput(final CardInput cardInput) throws Exception {
        String name = cardInput.getName();
        if (name.equals(CARD_THE_RIPPER)
             || name.equals(CARD_MIRAJ)
             || name.equals(CARD_THE_CURSED_ONE)
             || name.equals(CARD_DISCIPLE)
             || name.equals(CARD_SENTINEL)
             || name.equals(CARD_BERSERKER)
             || name.equals(CARD_GOLIATH)
             || name.equals(CARD_WARDEN)) {
            return new MinionCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(), cardInput.getName(), cardInput.getHealth(),
                    cardInput.getAttackDamage());
        } else if (name.equals(CARD_FIRESTORM)
                    || name.equals(CARD_WINTERFELL)
                    || name.equals(CARD_HEART_HOUND)) {
            return new EnvironmentCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(), cardInput.getName());
        } else if (name.equals(CARD_LORD_ROYCE)
                    || name.equals(CARD_EMPRESS_THORINA)
                    || name.equals(CARD_KING_MUDFACE)
                    || name.equals(CARD_GENERAL_KOCIORAW)) {
            return new HeroCard(cardInput.getMana(), cardInput.getDescription(),
                    cardInput.getColors(), cardInput.getName());
        } else {
            throw new Exception("Invalid card");
        }
    }
}
