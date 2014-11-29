package to2.dice.controllers;

public enum ControllerMessage {
    OBSERVER_ALREADY_JOINED("Dołączyłeś już jako obserwator do tej gry"),
    NO_SUCH_JOINED_OBSERVER("Nie możesz opuścić gry, której nie obserwujesz"),
    PLAYER_ALREADY_SAT_DOWN("Usiadłeś już przy stole"),
    PLAYER_IS_NOT_OBSERVER("Nie możesz usiąść do gry, której nie obserwujesz"),
    NO_EMPTY_PLACES("Nie ma już wolnych miejsc przy tym stole"),
    GAME_ALREADY_STARTED("Nie można usiąść do gry, która się już zaczęła"),
    PLAYER_IS_IN_GAME("Nie można opuścić gry siedząc przy stole")
    ;

    private final String text;

    private ControllerMessage(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
