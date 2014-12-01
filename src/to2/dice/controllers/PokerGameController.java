package to2.dice.controllers;

import to2.dice.game.*;
import to2.dice.messaging.GameAction;
import to2.dice.messaging.GameActionType;
import to2.dice.messaging.RerollAction;
import to2.dice.messaging.Response;
import to2.dice.server.GameServer;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;


public class PokerGameController extends GameController {

    //TODO synchronize GameState
    private GameState state;
    private List<String> observers = new ArrayList<String>();
    private int currentTurn=0;

    // TODO Map<Player, NumberOfAbsences>

    public PokerGameController(GameServer server, GameSettings settings, String creator) {
        super(server, settings, creator);
        state = new GameState();

        joinGame(creator);
    }

    @Override
    public GameInfo getGameInfo() {
        return new GameInfo(settings, state);
    }

    GameState getGameState() {
        return state;
    }

    @Override
    public synchronized Response handleGameAction(GameAction gameAction) {
        Response response = null;
        switch (gameAction.getType()) {
            case JOIN_GAME:
                response = joinGame(gameAction.getSender());
                break;

            case LEAVE_GAME:
                response = leaveGame(gameAction.getSender());
                break;

            case SIT_DOWN:
                response = sitDown(gameAction.getSender());
                break;

            case STAND_UP:
                response = standUp(gameAction.getSender());
                break;

            case REROLL:
                response = reroll(gameAction.getSender(), ((RerollAction)gameAction).getChosenDice());
                break;
        }

        return response;
    }

    private Response joinGame(String playerName) {
        if (!observers.contains(playerName)) {
            observers.add(playerName);
            return new Response(Response.Type.SUCCESS);
        }
        else {
            return new Response(Response.Type.FAILURE, ControllerMessage.OBSERVER_ALREADY_JOINED.toString());
        }
    }

    private Response leaveGame(String playerName) {
        if (!observers.contains(playerName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.NO_SUCH_JOINED_OBSERVER.toString());
        }
        else if (state.isPlayerWithName(playerName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.PLAYER_IS_IN_GAME.toString());
        }
        else {
            observers.remove(playerName);

            //TODO check if the room is empty, wait some time and ask server for destruction

            return new Response(Response.Type.SUCCESS);
        }
    }

    private Response sitDown(String playerName) {
        if (state.isGameStarted()) {
            return new Response(Response.Type.FAILURE, ControllerMessage.GAME_ALREADY_STARTED.toString());
        }
        else if (state.getPlayersNumber() >= settings.getMaxPlayers()) {
            return new Response(Response.Type.FAILURE, ControllerMessage.NO_EMPTY_PLACES.toString());
        }
        else if (!observers.contains(playerName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.PLAYER_IS_NOT_OBSERVER.toString());
        }
        else if (state.isPlayerWithName(playerName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.PLAYER_ALREADY_SAT_DOWN.toString());
        }
        else {
            state.addPlayer(new Player(playerName, false, settings.getDiceNumber()));

            if (state.getPlayers().size() == settings.getMaxHumanPlayers()) {
                startGame();
            }

            server.sendToAll(this, state);
            return new Response(Response.Type.SUCCESS);
        }
    }


    private Response standUp(String playerName) {

        return null;
    }

    private Response reroll(String playerName, boolean[] chosenDices) {



        nextPlayer();
        return null;
    }

    private void startGame() {
        state.setGameStarted(true);
        state.setCurrentRound(1);
        state.setCurrentPlayer(state.getPlayers().get(0));

        for (Player player : state.getPlayers()) {

        }
        currentTurn = 2;
        server.sendToAll(this, state);
        Timer timer = new Timer(state.getCurrentPlayer());
        (new Thread(timer)).start();
    }

    private void nextPlayer() {




        server.sendToAll(this, state);
    }

    private class Timer implements Runnable {

        private Player player;

        public Timer(Player player) {
            this.player = player;
        }

        @Override
        public void run() {
            boolean[] chosenDice;
            if(player.isBot()) {
                /* make bot move */
                Dice dice = player.getDice();
                List<Dice> otherDices = new ArrayList<Dice>();
                for (Player p : state.getPlayers()) {
                    if (p != player) otherDices.add(p.getDice());
                }
                chosenDice = new boolean[settings.getDiceNumber()]; // temporary: bot does nothing
                //    TODO  choosenDice = bot.makemove()

                RerollAction rerollAction = new RerollAction(GameActionType.REROLL, player.getName(), chosenDice);
                handleGameAction(rerollAction);
            }
            else {
                /* sleep max time, that player can wait and then reroll nothing */
                int startTurn = currentTurn;
                try {
                    sleep(settings.getTimeForMove());
                } catch (InterruptedException e) { }

                if(currentTurn == startTurn) {
                    chosenDice = new boolean[settings.getDiceNumber()];
                    RerollAction rerollAction = new RerollAction(GameActionType.REROLL, player.getName(), chosenDice);
                    Response response = handleGameAction(rerollAction);
                    // TODO if SUCCESS increment player absence number
                }
            }
        }
    }

}
