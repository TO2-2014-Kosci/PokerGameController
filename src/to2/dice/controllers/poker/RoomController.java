package to2.dice.controllers.poker;

import to2.dice.ai.Bot;
import to2.dice.controllers.PokerGameController;
import to2.dice.game.GameSettings;
import to2.dice.game.GameState;
import to2.dice.game.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomController {

    private final PokerGameController pokerGameController;
    private GameThread gameThread;
    private final GameSettings settings;
    private final GameState state;
    private List<String> observers = new ArrayList<String>();
    private Map<Player, Bot> bots;

    public RoomController(PokerGameController pokerGameController, GameThread gameThread, GameSettings settings, GameState state, Map<Player, Bot> bots) {
        this.pokerGameController = pokerGameController;
        this.gameThread = gameThread;
        this.settings = settings;
        this.state = state;
        this.bots = bots;
    }


    public void addObserver(String playerName) {
        observers.add(playerName);
    }

    public boolean isPlayerWithName(String name) {
        for (Player player : state.getPlayers()) {
            if (player.getName() == name) {
                return true;
            }
        }
        return false;
    }

    public void removeObserver(String observerName) {
        observers.remove(observerName);
//
//            if (isRoomEmpty())
//            {
//                TODO wait some time
//                            try {
//                                sleep(2);
//                            } catch (InterruptedException e) { }
//                server.finishGame(this); // ??
//            }
    }

    public void addPlayer(String playerName) {
        state.addPlayer(new Player(playerName, false, settings.getDiceNumber()));

        if (isGameStartConditionMet()) {
            gameThread.start();
        }
    }

    public void addBot(String botName, Bot bot) {
        Player botPlayer = new Player((botName), true, settings.getDiceNumber());
        state.addPlayer(botPlayer);
        bots.put(botPlayer, bot);
    }

    private boolean isGameStartConditionMet(){
        return (state.getPlayers().size() == settings.getMaxPlayers() && !state.isGameStarted());
    }

    private boolean isRoomEmpty() {
        return (observers.isEmpty());
    }

    public boolean isRoomFull() {return (state.getPlayers().size() == settings.getMaxPlayers());}

    public boolean isObserverWithName(String name) {
        return observers.contains(name);
    }

    public void removePlayer(String playerName) {

    }
}
