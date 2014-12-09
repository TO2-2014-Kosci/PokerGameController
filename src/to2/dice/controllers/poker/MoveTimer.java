package to2.dice.controllers.poker;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import to2.dice.ai.Bot;
import to2.dice.controllers.GameController;
import to2.dice.controllers.PokerGameController;
import to2.dice.game.*;
import to2.dice.messaging.RerollAction;
import to2.dice.messaging.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class MoveTimer extends Thread {

    private int time;
    private GameThread gameThread;

    public MoveTimer(int time, GameThread gameThread) {
        this.time = time;
        this.gameThread = gameThread;
    }


    public void start() {
        throw new NotImplementedException();
    }

    public boolean tryStop() {
        throw new NotImplementedException();
    }

    @Override
    public void run() {
       /* boolean[] chosenDice;

        if (player.isBot()) {
            Dice dice = player.getDice();

            List<Dice> otherDices = new ArrayList<Dice>();
            for (Player p : state.getPlayers()) {
                if (p.equals(player))
                    otherDices.add(p.getDice());
            }

            Bot bot = bots.get(player);
            chosenDice = bot.makeMove(dice, otherDices);

            RerollAction rerollAction = new RerollAction(player.getName(), chosenDice);
            controller.handleGameAction(rerollAction);
        } else {
            // sleep max time, that player can wait and then reroll nothing
            int startTurn = currentTurn;
            try {
                sleep(settings.getTimeForMove());
            } catch (InterruptedException e) {
            }

            if (currentTurn == startTurn) {
                chosenDice = new boolean[settings.getDiceNumber()];
                RerollAction rerollAction = new RerollAction(player.getName(), chosenDice);
                Response response = controller.handleGameAction(rerollAction);
                if (response.type == Response.Type.SUCCESS)
                    ((PokerGameController) controller).addPenaltyToPlayer(player);
            }
        */
    }
}
