package to2.dice.controllers.poker;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
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
        return diceRoller.nextInt(6)+1;
    }

    public Dice rollDice() {
        Dice dice = new Dice(this.diceNumber);
        int[] dices = new int[this.diceNumber];
        for(int i = 0; i < this.diceNumber; i++) {
            dices[i] = diceRoller.nextInt(6)+1;
    }

    public int rollSingleDice() {
        throw new NotImplementedException();
    }

    public Dice rollDice() {
        throw new NotImplementedException();
    }

}
