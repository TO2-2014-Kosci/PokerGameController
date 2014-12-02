package to2.dice.controllers;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import to2.dice.ai.Bot;
import to2.dice.ai.BotFactory;
import to2.dice.controllers.poker.MoveTimer;
import to2.dice.game.*;
import to2.dice.messaging.GameAction;
import to2.dice.messaging.RerollAction;
import to2.dice.messaging.Response;
import to2.dice.server.GameServer;

import java.util.*;

import static java.lang.Thread.sleep;


public class PokerGameController extends GameController {

    //TODO synchronize GameState

    private List<String> observers = new ArrayList<String>();
    private Map<Player, Integer> numberOfAbsences = new HashMap<Player, Integer>();
    private Map<Player, Bot> bots = new HashMap<Player, Bot>();
    private int currentTurn=0;

    public PokerGameController(GameServer server, GameSettings settings, String creator) {
        super(server, settings, creator);
        joinRoom(creator);
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
                response = reroll(gameAction.getSender(), ((RerollAction)gameAction).getChosenDice());
                break;
        }
        return response;
    }

    private Response joinRoom(String playerName) {
        if (!observers.contains(playerName)) {
            observers.add(playerName);
            return new Response(Response.Type.SUCCESS);
        }
        else {
            return new Response(Response.Type.FAILURE, ControllerMessage.OBSERVER_ALREADY_JOINED.toString());
        }
    }

    private Response leaveRoom(String playerName) {
        if (!observers.contains(playerName)) {
            return new Response(Response.Type.FAILURE, ControllerMessage.NO_SUCH_JOINED_OBSERVER.toString());
        }
        else if (state.getPlayers().contains(new Player(playerName, false, settings.getDiceNumber()))) {  // ugly
            return new Response(Response.Type.FAILURE, ControllerMessage.PLAYER_IS_IN_GAME.toString());
        }
        else {
            observers.remove(playerName);

            if (isRoomEmpty())
            {
                //TODO wait some time
                            try {
                                sleep(2);
                            } catch (InterruptedException e) { }
                server.finishGame(this); // ??
            }

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
        else if (state.getPlayers().contains(new Player(playerName, false, settings.getDiceNumber()))) {  // very ugly
            return new Response(Response.Type.FAILURE, ControllerMessage.PLAYER_ALREADY_SAT_DOWN.toString());
        }
        else {
            state.addPlayer(new Player(playerName, false, settings.getDiceNumber()));

            if (isGameStartConditionMet()) {
                startGame();
            }

            server.sendToAll(this, state);
            return new Response(Response.Type.SUCCESS);
        }
    }

    private Response standUp(String playerName) {
        throw new NotImplementedException();
    }

    private Response reroll(String playerName, boolean[] chosenDices) {
        Player player = null;
        for (Player p : state.getPlayers()) {
            if(p.getName() == playerName) player = p;
        }
        if (player!=null) {
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
            return new Response(Response.Type.SUCCESS);
            }
            return new Response(Response.Type.FAILURE, ControllerMessage.WRONG_DICE_NUMBER.toString());
        }
        return new Response(Response.Type.FAILURE, ControllerMessage.NO_SUCH_PLAYER.toString());
    }

    public void addPenaltyToPlayer(Player player) {
        int currentAbsences = numberOfAbsences.get(player);
        currentAbsences++;
        if (currentAbsences == settings.getMaxInactiveTurns())
            standUp(player.getName());
        else
            numberOfAbsences.put(player, currentAbsences);
    }

    private void createBots() {
        int num = 0;
        for (Map.Entry<BotLevel, Integer> entry : settings.getBotsNumbers().entrySet()) {

            BotLevel botLevel = entry.getKey();
            int botsNumber = entry.getValue();

            for (int i = 0; i < botsNumber; i++) {
                Bot bot = BotFactory.createBot(botLevel, settings.getGameType(), settings.getTimeForMove());

                Player botPlayer = new Player(("bot#" + (++num)), true, settings.getDiceNumber());
                state.addPlayer(botPlayer);

                bots.put(botPlayer, bot);
            }

        }
    }

    private Dice getRandomDice() {
        Dice dice = new Dice(settings.getDiceNumber());
        int[] dices = new int[settings.getDiceNumber()];
        Random generator = new Random();
        for(int i = 0; i < settings.getDiceNumber(); i++) {
            dices[i] = generator.nextInt(6)+1;
        }
        dice.setDice(dices);
        return dice;
    }

    private void startGame() {

        createBots();

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

    private boolean isGameStartConditionMet(){
        return (state.getPlayers().size() == settings.getMaxPlayers() && !state.isGameStarted());
    }

    private boolean isRoomEmpty() {
        return (observers.isEmpty());
    }
}