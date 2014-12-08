package to2.dice.controllers.poker;

import to2.dice.game.Dice;
import java.security.SecureRandom;

public class DiceRoller {
    private int diceNumber;
    private SecureRandom diceRoller;

    public DiceRoller(int diceNumber) {
        this.diceNumber = diceNumber;
        this.diceRoller = new SecureRandom();
    }

    public int rollSingleDice() {
        return (diceRoller.nextInt(6) + 1);
    }

    public Dice rollDice() {
        Dice dice = new Dice(this.diceNumber);

        int[] dice_tab = new int[this.diceNumber];
        for (int i = 0; i < this.diceNumber; i++) {
            dice_tab[i] = rollSingleDice();
        }

        dice.setDice(dice_tab);
        return dice;
    }
}
