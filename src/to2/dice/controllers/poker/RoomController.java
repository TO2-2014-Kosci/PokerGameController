package to2.dice.controllers.poker;

import to2.dice.game.Player;

public class RoomController {

    void addObserver() {
        observers.add(playerName);
    }

    void removeObserver() {
        observers.remove(playerName);
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

    void addPlayer() {
        state.addPlayer(new Player(playerName, false, settings.getDiceNumber()));

        if (isGameStartConditionMet()) {
            startGame();
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
