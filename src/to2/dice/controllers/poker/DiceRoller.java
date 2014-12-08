package to2.dice.controllers.poker;

import to2.dice.game.Dice;
import java.util.Random;

public class DiceRoller {

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

}
