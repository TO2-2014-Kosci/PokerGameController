package to2.dice.controllers.poker;

import to2.dice.game.Dice;
import to2.dice.game.Player;

import java.util.Random;

public class GameThread {

    public void addPenaltyToPlayer(Player player) {
        int currentAbsences = numberOfAbsences.get(player);
        currentAbsences++;
        if (currentAbsences == settings.getMaxInactiveTurns())
            standUp(player.getName());
        else
            numberOfAbsences.put(player, currentAbsences);
    }

    private void startGame() {

        state.setCurrentRound(1);
        state.setCurrentPlayer(state.getPlayers().get(0));
        state.setGameStarted(true);

        for (Player player : state.getPlayers()) {
            player.setDice(getRandomDice());
        }

        currentTurn = 2;
        server.sendToAll(this, state);
        MoveTimer moveTimer = new MoveTimer(this, settings, state, bots, state.getCurrentPlayer(), currentTurn);
        (new Thread(moveTimer)).start();
    }

    private void nextPlayer() {
        Player newPlayer = null;
        for (Player p : state.getPlayers()) {
            if (p == state.getCurrentPlayer()) {
                // TODO switch Player
            }
        }

        server.sendToAll(this, state);
    }

    private handleRerollRequest() {
        Dice dice = new Dice(settings.getDiceNumber());
        int[] dices = new int[settings.getDiceNumber()];
        int[] oldDices = player.getDice().getDice();
        if(chosenDices.length == oldDices.length) {
            Random generator = new Random();
            for(int i =0; i<chosenDices.length; i++ ) {
                if(chosenDices[i]) {
                    dices[i] = generator.nextInt(6)+1;
                }
                else {
                    dices[i] = oldDices[i];
                }
                dice.setDice(dices);
                player.setDice(dice);
            }
            nextPlayer();
    }
}
