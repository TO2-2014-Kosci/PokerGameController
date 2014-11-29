package to2.dice.controllers;

import to2.dice.game.GameInfo;
import to2.dice.game.GameSettings;
import to2.dice.game.GameState;
import to2.dice.game.Player;
import to2.dice.messaging.GameAction;
import to2.dice.messaging.RerollAction;
import to2.dice.messaging.Response;
import to2.dice.server.GameServer;

import java.util.ArrayList;
import java.util.List;


public class PokerGameController extends GameController {

    private GameState state;
    private List<String> observers = new ArrayList<String>();

    public PokerGameController(GameServer server, GameSettings settings, String creator) {
        super(server, settings, creator);
        state = new GameState();

        joinGame(creator);
    }

    @Override
    public GameInfo getGameInfo() {
        return new GameInfo(settings, state);
    }

    @Override
    public Response handleGameAction(GameAction gameAction) {
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
        if (observers.contains(playerName)) {
            observers.remove(playerName);
            return new Response(Response.Type.SUCCESS);
        }
        else
            return new Response(Response.Type.FAILURE, ControllerMessage.NO_SUCH_JOINED_OBSERVER.toString());
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

            server.sendToAll(this, state);
            return new Response(Response.Type.SUCCESS);
        }
    }


    private Response standUp(String playerName) {
        return null;
    }

    private Response reroll(String playerName, boolean[] chosenDices) {
        return null;
    }

}
