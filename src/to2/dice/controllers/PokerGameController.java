package to2.dice.controllers;

import to2.dice.ai.Bot;
import to2.dice.ai.BotFactory;
import to2.dice.controllers.poker.GameThread;
import to2.dice.controllers.poker.MoveTimer;
import to2.dice.controllers.poker.RoomController;
import to2.dice.game.*;
import to2.dice.messaging.GameAction;
import to2.dice.messaging.RerollAction;
import to2.dice.messaging.Response;
import to2.dice.server.GameServer;

import java.util.*;

import static java.lang.Thread.sleep;


public class PokerGameController extends GameController {

    private Map<Player, Bot> bots = new HashMap<Player, Bot>();
    private RoomController roomController;
//    private MoveTimer moveTimer;
    private GameThread gameThread;

    public PokerGameController(GameServer server, GameSettings settings, String creator) {
        super(server, settings, creator);

        gameThread = new GameThread(server, this, settings, state, bots);
        roomController = new RoomController(server, this, gameThread, settings, state, bots);

        initializeRoom(creator);
    }

    @Override
    public GameInfo getGameInfo() {
        return new GameInfo(settings, state);
    }

    @Override
    public synchronized Response handleGameAction(GameAction gameAction) {
        Response response = null;
        switch (gameAction.getType()) {
            case JOIN_ROOM:
                response = joinRoom(gameAction.getSender());
                break;

            case LEAVE_ROOM:
                response = leaveRoom(gameAction.getSender());
                break;

            case SIT_DOWN:
                response = sitDown(gameAction.getSender());
                break;

            case STAND_UP:
                response = standUp(gameAction.getSender());
                break;

            case REROLL:
                response = reroll(gameAction.getSender(), ((RerollAction) gameAction).getChosenDice());
                break;
        }
        return response;
    }


    private Response joinRoom(String senderName) {
        if (roomController.isObserverWithName(senderName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.OBSERVER_ALREADY_JOINED.toString());
        } else {
            roomController.addObserver(senderName);
            return new Response(Response.Type.SUCCESS);
        }
    }

    private Response leaveRoom(String senderName) {
        if (!roomController.isObserverWithName(senderName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.NO_SUCH_JOINED_OBSERVER.toString());
        } else {
            roomController.removeObserver(senderName);
            return new Response(Response.Type.SUCCESS);
        }
    }

    private Response sitDown(String senderName) {
        if (roomController.isGameStarted()) {
            return new Response(Response.Type.FAILURE, ControllerMessage.GAME_ALREADY_STARTED.toString());
        } else if (roomController.isRoomFull()) {
            return new Response(Response.Type.FAILURE, ControllerMessage.NO_EMPTY_PLACES.toString());
        } else if (!roomController.isObserverWithName(senderName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.SENDER_IS_NOT_OBSERVER.toString());
        } else if (!roomController.isPlayerWithName(senderName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.PLAYER_ALREADY_SAT_DOWN.toString());
        } else {
            roomController.addPlayer(senderName);
            return new Response(Response.Type.SUCCESS);
        }
    }

    private Response standUp(String senderName) {
        if (!roomController.isObserverWithName(senderName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.SENDER_IS_NOT_OBSERVER.toString());
        } else if (!roomController.isPlayerWithName(senderName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.PLAYER_ALREADY_STAND_UP.toString());
        } else {
            if (roomController.isGameStarted()) {
                gameThread.removePlayer(senderName);
            }
            roomController.removePlayer(senderName);
            return new Response(Response.Type.SUCCESS);
        }
    }

    private Response reroll(String senderName, boolean[] chosenDices) {
        if (!state.isGameStarted()) {
            return new Response(Response.Type.FAILURE, ControllerMessage.GAME_IS_NOT_STARTED.toString());
        } else if (chosenDices.length != settings.getDiceNumber()) {
            return new Response(Response.Type.FAILURE, ControllerMessage.NO_SUCH_PLAYER.toString());
        } else if (!roomController.isPlayerWithName(senderName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.WRONG_DICE_NUMBER.toString());
        } else if (state.getCurrentPlayer().getName().equals(senderName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.OTHER_PLAYERS_TURN.toString());
        } else {
            /* boolean notTooLate = moveTimer.tryStop();
            if (notTooLate) { */
            gameThread.handleRerollRequest(chosenDices);
            return new Response(Response.Type.SUCCESS);
            /*  }
            else
                return new Response(Response.Type.FAILURE, ControllerMessage.OTHER_PLAYERS_TURN.toString()); */
        }
    }

    private void initializeRoom(String creator) {
        createBots();
        roomController.addObserver(creator);
    }

    private void createBots() {
        int botId = 0;

        for (Map.Entry<BotLevel, Integer> entry : settings.getBotsNumbers().entrySet()) {

            BotLevel botLevel = entry.getKey();
            int botsNumber = entry.getValue();

            for (int i = 0; i < botsNumber; i++) {
                Bot bot = BotFactory.createBot(botLevel, settings.getGameType(), settings.getTimeForMove());
                String botName = "bot#" + botId++;
                roomController.addBot(botName, bot);
            }
        }
    }
}