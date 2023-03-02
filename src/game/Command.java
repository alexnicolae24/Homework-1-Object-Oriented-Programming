package game;

public class Command {
    public static final String COMMAND_GET_PLAYER_DECK = "getPlayerDeck";
    public static final String COMMAND_GET_PLAYER_HERO = "getPlayerHero";
    public static final String COMMAND_GET_PLAYER_TURN = "getPlayerTurn";
    public static final String COMMAND_END_PLAYER_TURN = "endPlayerTurn";
    public static final String COMMAND_PLACE_CARD = "placeCard";
    public static final String COMMAND_GET_CARDS_IN_HAND = "getCardsInHand";
    public static final String COMMAND_GET_PLAYER_MANA = "getPlayerMana";
    public static final String COMMAND_GET_CARDS_ON_TABLE = "getCardsOnTable";
    public static final String COMMAND_GET_ENVIRONMENT_CARDS_IN_HAND = "getEnvironmentCardsInHand";
    public static final String COMMAND_GET_CARD_AT_POSITION = "getCardAtPosition";
    public static final String COMMAND_USE_ENVIRONMENT_CARD = "useEnvironmentCard";
    public static final String COMMAND_CARD_USES_ABILITY = "cardUsesAbility";
    public static final String COMMAND_ATTACK_HERO = "useAttackHero";
    public static final String COMMAND_USE_HERO_ABILITY = "useHeroAbility";
    public static final int COMMAND_PLACE_CARD_ERROR_ENVIRONMENT_CODE = 1;
    public static final int COMMAND_PLACE_CARD_ERROR_NOT_ENOUGH_MANA_CODE = 2;
    public static final int COMMAND_PLACE_CARD_ERROR_FULL_ROW_CODE = 3;
    public static final String COMMAND_PLACE_CARD_ERROR_ENVIRONMENT_MESSAGE =
                                    "Cannot place environment card on table.";
    public static final String COMMAND_PLACE_CARD_ERROR_NOT_ENOUGH_MANA_MESSAGE =
                                    "Not enough mana to place card on table.";
    public static final String COMMAND_PLACE_CARD_ERROR_FULL_ROW_MESSAGE =
                                    "Cannot place card on table since row is full.";
    public static final String COMMAND_GET_FROZEN_CARDS_ON_TABLE = "getFrozenCardsOnTable";
    public static final String COMMAND_CARD_USES_ATTACK = "cardUsesAttack";
    public static final int COMMAND_USE_ENV_CARD_NOT_ENV_CARD_ERROR_CODE = 1;
    public static final String COMMAND_USE_ENV_CARD_NOT_ENV_CARD_ERROR_MESSAGE =
                                      "Chosen card is not of type environment.";
    public static final int COMMAND_USE_ENV_CARD_NOT_ENOUGH_MANA_ERROR_CODE = 2;
    public static final String COMMAND_USE_ENV_CARD_NOT_ENOUGH_MANA_ERROR_MESSAGE =
                                          "Not enough mana to use environment card.";
    public static final int COMMAND_USE_ENV_CARD_CHOSEN_ROW_ERROR_CODE = 3;
    public static final String COMMAND_USE_ENV_CARD_CHOSEN_ROW_ERROR_MESSAGE =
                                  "Chosen row does not belong to the enemy.";
    public static final int COMMAND_USE_ENV_CARD_HEART_HOUND_ERROR_CODE = 4;
    public static final String COMMAND_USE_ENV_CARD_HEART_HOUND_ERROR_MESSAGE =
                      "Cannot steal enemy card since the player's row is full.";

    public static final int COMMAND_CARD_USES_ATTACK_NOT_ENEMY_CARD_ERROR_CODE = 1;
    public static final String COMMAND_CARD_USES_ATTACK_NOT_ENEMY_CARD_ERROR_MESSAGE =
                                          "Attacked card does not belong to the enemy.";

    public static final int COMMAND_CARD_USES_ATTACK_ALREADY_ATTACKED_ERROR_CODE = 2;
    public static final String COMMAND_CARD_USES_ATTACK_ALREADY_ATTACKED_ERROR_MESSAGE =
                                          "Attacker card has already attacked this turn.";

    public static final int COMMAND_CARD_USES_ATTACK_FROZEN_CARD_ERROR_CODE = 3;
    public static final String COMMAND_CARD_USES_ATTACK_FROZEN_CARD_ERROR_MESSAGE =
                                                        "Attacker card is frozen.";

    public static final int COMMAND_CARD_USES_ATTACK_TANK_CARD_ERROR_CODE = 4;
    public static final String COMMAND_CARD_USES_ATTACK_TANK_CARD_ERROR_MESSAGE =
                                            "Attacked card is not of type 'Tank'.";

    public static final int COMMAND_CARD_USES_ABILITY_FROZEN_CARD_ERROR_CODE = 1;
    public static final String COMMAND_CARD_USES_ABILITY_FROZEN_CARD_ERROR_MESSAGE =
                                                        "Attacker card is frozen.";
    public static final int COMMAND_CARD_USES_ABILITY_ALREADY_ATTACKED_ERROR_CODE = 2;
    public static final String COMMAND_CARD_USES_ABILITY_ALREADY_ATTACKED_ERROR_MESSAGE =
                                         "Attacker card has already attacked this turn.";

    public static final int COMMAND_CARD_USES_ABILITY_DISCIPLE_ERROR_CODE = 3;
    public static final String COMMAND_CARD_USES_ABILITY_DISCIPLE_ERROR_MESSAGE =
                          "Attacked card does not belong to the current player.";

    public static final int COMMAND_CARD_USES_ABILITY_NOT_ENEMY_ERROR_CODE = 4;
    public static final String COMMAND_CARD_USES_ABILITY_NOT_ENEMY_ERROR_MESSAGE =
                                   "Attacked card does not belong to the enemy.";

    public static final int COMMAND_CARD_USES_ABILITY_TANK_CARD_ERROR_CODE = 5;
    public static final String COMMAND_CARD_USES_ABILITY_TANK_CARD_ERROR_MESSAGE =
                                          "Attacked card is not of type 'Tank'.";

    public static final int CODE_PLAYER_ONE_WON = 101;
    public static final int CODE_PLAYER_TWO_WON = 102;

    public static final int ERROR_CODE_NOT_ENOUGH_MANA_HERO_ABILITY = 1;
    public static final int ERROR_CODE_HERO_ALREADY_ATTACKED = 2;
    public static final int ERROR_CODE_NOT_ENEMY_ROW = 3;
    public static final int ERROR_CODE_NOT_OWN_ROW = 4;
}
