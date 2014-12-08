package to2.dice.controllers.poker;

import to2.dice.controllers.PokerGameController;
import to2.dice.game.GameSettings;
import to2.dice.game.GameState;
import to2.dice.game.Player;
import to2.dice.server.GameServer;

import java.util.HashMap;
import java.util.Map;

public class GameThread {

    private PokerGameController pokerGameController;
    private GameServer server;
    private GameSettings settings;
    private GameState state;
    private DiceRoller diceRoller;
    Map<Player, Integer> numberOfAbsences = new HashMap<Player, Integer>();
    int currentTurn = 0;

    public GameThread(PokerGameController pokerGameController, GameServer server, GameSettings settings, GameState state) {
        this.pokerGameController = pokerGameController;
        this.server = server;
        this.settings = settings;
        this.state = state;
        diceRoller = new DiceRoller(settings.getDiceNumber());
    }


    public void addPenaltyToPlayer(Player player) {
        int currentAbsences = numberOfAbsences.get(player);
        currentAbsences++;
        if (currentAbsences == settings.getMaxInactiveTurns())
            removePlayer(player.getName());
        else
            numberOfAbsences.put(player, currentAbsences);
    }

    public void startGame() {

        state.setCurrentRound(1);
        state.setCurrentPlayer(state.getPlayers().get(0));
        state.setGameStarted(true);

        for (Player player : state.getPlayers()) {
            player.setDice(diceRoller.rollDice());
        }

        currentTurn = 2;
        server.sendToAll(pokerGameController, state);
//        MoveTimer moveTimer = new MoveTimer(this, settings, state, bots, state.getCurrentPlayer(), currentTurn);
//        (new Thread(moveTimer)).start();
    }

    private void nextPlayer() {
        Player newPlayer = null;
        for (Player p : state.getPlayers()) {
            if (p == state.getCurrentPlayer()) {
                // TODO switch Player
            }
        }

        server.sendToAll(pokerGameController, state);
    }

    private void handleRerollRequest() {

//        Dice dice = new Dice(settings.getDiceNumber());
//        int[] dices = new int[settings.getDiceNumber()];
//        int[] oldDices = player.getDice().getDice();
//        if (chosenDices.length == oldDices.length) {
//            Random generator = new Random();
//            for (int i = 0; i < chosenDices.length; i++) {
//                if (chosenDices[i]) {
//                    dices[i] = generator.nextInt(6) + 1;
//                } else {
//                    dices[i] = oldDices[i];
//                }
//                dice.setDice(dices);
//                player.setDice(dice);
//            }
//            nextPlayer();
//        }
    }

    public void handleRerollRequest(boolean[] chosenDices) {

    }

    public void removePlayer(String senderName) {

    }

    public void start() {
    }
}