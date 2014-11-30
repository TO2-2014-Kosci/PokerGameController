package to2.dice.controllers.poker;

import java.util.Arrays;

public class PokerHands {

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

}