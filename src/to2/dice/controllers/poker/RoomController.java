package to2.dice.controllers.poker;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import to2.dice.ai.Bot;
import to2.dice.controllers.GameController;
import to2.dice.controllers.PokerGameController;
import to2.dice.game.GameSettings;
import to2.dice.game.GameState;
import to2.dice.game.Player;
import to2.dice.server.GameServer;

import java.io.NotActiveException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomController {

    private GameServer server;
    private PokerGameController controller;
    private final GameThread gameThread;
    private final GameSettings settings;
    private final GameState state;
    private List<String> observers = new ArrayList<String>();
    private Map<Player, Bot> bots;
    private final int ROOM_INACTIVITY_TIME = 5000;

    public RoomController(GameServer server, PokerGameController controller, GameThread gameThread, GameSettings settings, GameState state, Map<Player, Bot> bots) {
        this.server = server;
        this.controller = controller;
        this.gameThread = gameThread;
        this.settings = settings;
        this.state = state;
        this.bots = bots;
    }


    public void addObserver(String observerName) {
        observers.add(observerName);
    }

    public void removeObserver(String observerName) {
        observers.remove(observerName);
        if (isRoomEmpty()) {
            //TODO waiting some time and interrupt when addObserver
           server.finishGame(controller);
        }
    }

    public void addPlayer(String playerName) {
        state.addPlayer(new Player(playerName, false, settings.getDiceNumber()));

        if (isGameStartConditionMet()) {
            gameThread.start();
        }
    }

    public void removePlayer(String playerName) {
        //TODO implement
        throw new NotImplementedException();
    }

    public void addBot(String botName, Bot bot) {
        Player botPlayer = new Player((botName), true, settings.getDiceNumber());
        bots.put(botPlayer, bot);
        state.addPlayer(botPlayer);
    }

    public boolean isObserverWithName(String name) {
        return observers.contains(name);
    }

    public boolean isPlayerWithName(String name) {
        for (Player player : state.getPlayers()) {
            if (player.getName() == name) {
                return true;
            }
        }
        return false;
    }

    public boolean isRoomFull() {
        return (state.getPlayers().size() == settings.getMaxPlayers());
    }

    private boolean isRoomEmpty() {
        return (observers.isEmpty());
    }

    private boolean isGameStartConditionMet(){
        return (state.getPlayers().size() == settings.getMaxPlayers() && !state.isGameStarted());
    }

    public boolean isGameStarted() {
        return (state.isGameStarted());
    }
}
