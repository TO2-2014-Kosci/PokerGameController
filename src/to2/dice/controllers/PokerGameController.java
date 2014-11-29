package to2.dice.controllers;

import to2.dice.game.GameInfo;
import to2.dice.game.GameSettings;
import to2.dice.game.GameState;
import to2.dice.messaging.GameAction;
import to2.dice.messaging.Response;
import to2.dice.server.GameServer;

public class PokerGameController extends GameController {

    private GameState state;

    public PokerGameController(GameServer server, GameSettings settings, String creator) {
        super(server, settings, creator);
        state = new GameState();
    }

    @Override
    public Response handleGameAction(GameAction gameAction) {
        return null;
    }

    @Override
    public GameInfo getGameInfo() {
        return new GameInfo(settings, state);
    }
}
