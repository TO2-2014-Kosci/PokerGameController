package to2.dice.controllers.poker;

import to2.dice.game.Dice;

import java.util.ArrayList;
import java.util.Arrays;

public class PokerHands {

    public static PokerHandType getHandType(Dice dice) {
        int[] dices = dice.getDice();
        if(isPoker(dices))return PokerHandType.POKER;
        if(isFour(dices))return PokerHandType.FOUR;
        if(isFull(dices))return PokerHandType.FULL;
        if(isLargeStraight(dices))return PokerHandType.LARGE_STRAIGHT;
        if(isSmallStraight(dices))return PokerHandType.SMALL_STRAIGHT;
        if(isThree(dices))return PokerHandType.THREE;
        if(isTwoPairs(dices))return PokerHandType.TWO_PAIRS;
        if(isPair(dices))return PokerHandType.PAIR;
        return PokerHandType.HIGH_CARD;
    }

    public static int compare(Dice dice1, Dice dice2) {
        PokerHandType type1 = getHandType(dice1);
        PokerHandType type2 = getHandType(dice2);
        if(type1.getValue()>type2.getValue())
            return 1;
        else {
            if(type1.getValue()<type2.getValue())
                return -1;
            else {
                return compareSameFigures(dice1.getDice(), dice2.getDice(), type1);
            }
        }
    }



    private static void sort(int[] dice) {
        for (int i = 0; i < dice.length; i++) {
            int min = i;

            for (int j = i+1; j < dice.length; j++) {
                if (dice[j] < dice[min]) {
                    min = j;
                }
            }

            int tmp = dice[i];
            dice[i] = dice[min];
            dice[min] = tmp;
        }
    }

    public static boolean isPoker(int[] dice) {
        if (dice.length != 5)
            return false;

        return ((dice[0] == dice[1]) && (dice[1] == dice[2]) && (dice[2] == dice[3]) && (dice[3] == dice[4]));
    }

    public static boolean isFour(int[] dice) {
        if (dice.length != 5)
            return false;

        if (isPoker(dice))
            return false;

        sort(dice);

        return (((dice[0] == dice[1]) && (dice[1] == dice[2]) && (dice[2] == dice[3]))
                || ((dice[1] == dice[2]) && (dice[2] == dice[3]) && (dice[3] == dice[4])));
    }

    public static boolean isFull(int[] dice) {
        if (dice.length != 5)
            return false;

        sort(dice);

        return (((dice[0] == dice[1]) && ((dice[2] == dice[3]) && (dice[3] == dice[4])))
                || ((dice[0] == dice[1]) && ((dice[1] == dice[2]) && (dice[3] == dice[4]))));
    }

    public static boolean isLargeStraight(int[] dice) {
        if (dice.length != 5)
            return false;

        sort(dice);

        return (Arrays.equals(dice, new int[]{2,3,4,5,6}));
    }

    public static boolean isSmallStraight(int[] dice) {
        if (dice.length != 5)
            return false;

        sort(dice);

        return (Arrays.equals(dice, new int[]{1,2,3,4,5}));
    }

    public static boolean isThree(int[] dice) {
        if (dice.length != 5)
            return false;

        if (isPoker(dice) || isFull(dice))
            return false;

        sort(dice);

        return ( (dice[0] == dice[1] && dice[1] == dice[2])
                || (dice[1] == dice[2] && dice[2] == dice[3])
                || (dice[2] == dice[3] && dice[3] == dice[4]) );
    }

    public static boolean isTwoPairs(int[] dice) {
        if (dice.length != 5)
            return false;

        if (isFour(dice) || isPoker(dice))
            return false;

        sort(dice);

        return ( (dice[0] == dice[1] && dice[2] == dice[3])
                || (dice[1] == dice[2] && dice[3] == dice[4])
                || (dice[0] == dice[1] && dice[3] == dice[4]) );
    }

    public static boolean isPair(int[] dice) {
        if (dice.length != 5)
            return false;

        if (isPoker(dice) || isFour(dice) || isFull(dice) || isThree(dice) || isTwoPairs(dice))
            return(false);

        sort(dice);

        return ( dice[0] == dice[1] || dice[1] == dice[2]
                || dice[2] == dice[3] || dice[3] == dice[4] );
    }

    public static int compareOnCardPriorities(ArrayList<Integer> d1, ArrayList<Integer> d2) {     //tak, wiem, przekombinowałem, mogły być tablice :)
        int i = d1.size()-1;
        while(i>=0) {
            if(d1.get(i)>d2.get(i)) return 1;
            if(d1.get(i)<d2.get(i)) return -1;
            i--;
        }
        return 0;
    }

    public static int compareSameFigures(int[] dices1, int[] dices2, PokerHandType type) {
        int d1a, d1b, d1c, d1d, d2a, d2b, d2c, d2d;
        switch (type) {
            case HIGH_CARD:
                return compareOnCardPriorities(new ArrayList<Integer>(Arrays.asList(dices1[4], dices1[3], dices1[2], dices1[1], dices1[0])),
                        new ArrayList<Integer>(Arrays.asList(dices2[4], dices2[3], dices2[2], dices2[1], dices2[0])));

            case PAIR:
                if(dices1[4]==dices1[3]) {
                    d1a = dices1[4];
                    d1b = dices1[2];
                    d1c = dices1[1];
                    d1d = dices1[0];
                }
                else {
                    if(dices1[3]==dices1[2]) {
                        d1a = dices1[3];
                        d1b = dices1[4];
                        d1c = dices1[1];
                        d1d = dices1[0];
                    }
                    else {
                        if(dices1[2]==dices1[1]) {
                            d1a = dices1[2];
                            d1b = dices1[4];
                            d1c = dices1[3];
                            d1d = dices1[0];
                        }
                        else {
                            d1a = dices1[1];
                            d1b = dices1[4];
                            d1c = dices1[3];
                            d1d = dices1[2];
                        }
                    }
                }
                if(dices2[4]==dices2[3]) {
                    d2a = dices2[4];
                    d2b = dices2[2];
                    d2c = dices2[1];
                    d2d = dices2[0];
                }
                else {
                    if(dices2[3]==dices2[2]) {
                        d2a = dices2[3];
                        d2b = dices2[4];
                        d2c = dices2[1];
                        d2d = dices2[0];
                    }
                    else {
                        if(dices2[2]==dices2[1]) {
                            d2a = dices2[2];
                            d2b = dices2[4];
                            d2c = dices2[3];
                            d2d = dices2[0];
                        }
                        else {
                            d2a = dices2[1];
                            d2b = dices2[4];
                            d2c = dices2[3];
                            d2d = dices2[2];
                        }
                    }
                }
                return compareOnCardPriorities(new ArrayList<Integer>(Arrays.asList(d1a, d1b, d1c, d1d)),
                        new ArrayList<Integer>(Arrays.asList(d2a, d2b, d2c, d2d)));

            case TWO_PAIRS:
                if((dices1[4]==dices1[3])&&(dices1[2]==dices1[1])) {
                    d1a = dices1[4];
                    d1b = dices1[2];
                    d1c = dices1[0];
                }
                else {
                    if((dices1[4]==dices1[3])&&(dices1[1]==dices1[0])) {
                        d1a = dices1[4];
                        d1b = dices1[1];
                        d1c = dices1[2];
                    }
                    else {
                        d1a = dices1[3];
                        d1b = dices1[1];
                        d1c = dices1[4];
                    }
                }
                if((dices2[4]==dices2[3])&&(dices2[2]==dices2[1])) {
                    d2a = dices2[4];
                    d2b = dices2[2];
                    d2c = dices2[0];
                }
                else {
                    if((dices2[4]==dices2[3])&&(dices2[1]==dices2[0])) {
                        d2a = dices2[4];
                        d2b = dices2[1];
                        d2c = dices2[2];
                    }
                    else {
                        d2a = dices2[3];
                        d2b = dices2[1];
                        d2c = dices2[4];
                    }
                }
                return compareOnCardPriorities(new ArrayList<Integer>(Arrays.asList(d1a, d1b, d1c)),
                        new ArrayList<Integer>(Arrays.asList(d2a, d2b, d2c)));

            case THREE:
                if((dices1[0]&dices1[1]&dices1[2])==dices1[0]) {
                    d1a = dices1[0];
                    d1b = dices1[4];
                    d1c = dices1[3];
                }
                else {
                    if((dices1[1]&dices1[2]&dices1[3])==dices1[1]) {
                        d1a = dices1[1];
                        d1b = dices1[4];
                        d1c = dices1[0];
                    }
                    else {
                        d1a = dices1[4];
                        d1b = dices1[1];
                        d1c = dices1[0];
                    }
                }
                if((dices2[0]&dices2[1]&dices2[2])==dices2[0]) {
                    d2a = dices2[0];
                    d2b = dices2[4];
                    d2c = dices2[3];
                }
                else {
                    if((dices2[1]&dices2[2]&dices2[3])==dices2[1]) {
                        d2a = dices2[1];
                        d2b = dices2[4];
                        d2c = dices2[0];
                    }
                    else {
                        d2a = dices2[4];
                        d2b = dices2[1];
                        d2c = dices2[0];
                    }
                }
                return compareOnCardPriorities(new ArrayList<Integer>(Arrays.asList(d1a, d1b, d1c)),
                        new ArrayList<Integer>(Arrays.asList(d2a, d2b, d2c)));

            case SMALL_STRAIGHT:
                return 0;

            case LARGE_STRAIGHT:
                return 0;

            case FULL:
                if((dices1[0]&dices1[1]&dices1[2])==dices1[0]) {
                    d1a = dices1[0];
                    d1b = dices1[4];
                }
                else {
                    d1a = dices1[4];
                    d1b = dices1[0];
                }
                if((dices2[0]&dices2[1]&dices2[2])==dices2[0]) {
                    d2a = dices2[0];
                    d2b = dices2[4];
                }
                else {
                    d2a = dices2[4];
                    d2b = dices2[0];
                }
                return compareOnCardPriorities(new ArrayList<Integer>(Arrays.asList(d1a, d1b)),
                        new ArrayList<Integer>(Arrays.asList(d2a, d2b)));

            case FOUR:
                if(dices1[0]==dices1[1]) {
                    d1a = dices1[0];
                    d1b = dices1[4];
                }
                else {
                    d1a = dices1[4];
                    d1b = dices1[0];
                }
                if(dices2[0]==dices2[1]) {
                    d2a = dices2[0];
                    d2b = dices2[4];
                }
                else {
                    d2a = dices2[4];
                    d2b = dices2[0];
                }
                return compareOnCardPriorities(new ArrayList<Integer>(Arrays.asList(d1a, d1b)),
                        new ArrayList<Integer>(Arrays.asList(d2a, d2b)));

            case POKER:
                if(dices1[0]>dices2[0])
                    return 1;
                else {
                    if (dices1[0] < dices2[0]) return -1;
                    else return 0;
                }
            default:
                return 0;
        }
    }

}