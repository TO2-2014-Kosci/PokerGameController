package to2.dice.controllers.poker;

import to2.dice.ai.Bot;
import to2.dice.controllers.PokerGameController;
import to2.dice.game.Dice;
import to2.dice.game.GameSettings;
import to2.dice.game.GameState;
import to2.dice.game.Player;
import to2.dice.server.GameServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameThread{

    private PokerGameController pokerGameController;
    private GameServer server;
    private GameSettings settings;
    private GameState state;
    private Map<Player, Bot> bots;
    private DiceRoller diceRoller;
    Map<Player, Integer> numberOfAbsences = new HashMap<Player, Integer>();
    private final int REROLLS_NUMBER = 2;
    boolean[] chosenDice;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private class GameRoutine implements Runnable {
        @Override
        public void run() {
            try {
                synchronized (pokerGameController) {
                    state.setGameStarted(true);

                    while (state.getCurrentRound() < settings.getRoundsToWin()) {
                        startNewRound();

                        for (int rerollNumber = 1; rerollNumber <= REROLLS_NUMBER; rerollNumber++) {

                            for (Player currentPlayer : state.getPlayers()) {
                                state.setCurrentPlayer(currentPlayer);
                                server.sendToAll(pokerGameController, state);

                                if (currentPlayer.isBot()) {
                                    chosenDice = bots.get(currentPlayer).makeMove(currentPlayer.getDice(), getOtherDice(currentPlayer));
                                }
                                else {
                                    while (chosenDice == null) {
                                        pokerGameController.wait();
                                    }
                                }

                                Dice currentPlayerDice = currentPlayer.getDice();
                                int[] dice_tab = currentPlayerDice.getDice();

                                for (int i = 0; i < settings.getDiceNumber(); i++) {
                                    if (chosenDice[i]) {
                                        dice_tab[i] = diceRoller.rollSingleDice();
                                    }
                                }
                                chosenDice = null;
                                currentPlayerDice.setDice(dice_tab);
                                state.getCurrentPlayer().setDice(currentPlayerDice);
                            }

                            Player winner = getWinner();
                            addPointToPlayer(winner);
                        }
                    }
                    state.setGameStarted(false);
                    server.sendToAll(pokerGameController, state);
                }
            } catch (InterruptedException e) {
                System.out.println("Fatal Error: GameThread interrupted!");
            }
        }

        //TODO WRONG! winner PokerHands checking
        private Player getWinner() {
            Player winner = state.getPlayers().get(0);
            for (Player player : state.getPlayers()) {
//                if (PokerHands.compare(player.getDice(), winner.getDice()) > 1) {
//                    winner = player;
//                }
            }
            return winner;
        }
    }

    private List<Dice> getOtherDice(Player player) {
        List<Dice> otherDice = new ArrayList<Dice>();
        for (Player p : state.getPlayers()) {
            if (p != player) {
                otherDice.add(p.getDice());
            }
        }
        return otherDice;
    }

    public GameThread(GameServer server, PokerGameController pokerGameController, GameSettings settings, GameState state, Map<Player, Bot> bots) {
        this.pokerGameController = pokerGameController;
        this.server = server;
        this.settings = settings;
        this.state = state;
        this.bots = bots;
        diceRoller = new DiceRoller(settings.getDiceNumber());
    }


    public void start() {
        executor.execute(new GameRoutine());
    }

    public synchronized void handleRerollRequest(boolean[] chosenDice) {

//        while ()
        this.chosenDice = chosenDice;
        pokerGameController.notify();
    }

    public void removePlayer(String senderName) {

    }

    private void startNewRound() {
        state.setCurrentRound(state.getCurrentRound() + 1);

        for (Player player : state.getPlayers()) {
            player.setDice(diceRoller.rollDice());
        }
    }

    private void addPenaltyToPlayer(Player player) {
        int currentAbsences = numberOfAbsences.get(player);
        currentAbsences++;
        if (currentAbsences == settings.getMaxInactiveTurns())
            removePlayer(player.getName());
        else
            numberOfAbsences.put(player, currentAbsences);
    }

    private void addPointToPlayer(Player player) {

    }
}