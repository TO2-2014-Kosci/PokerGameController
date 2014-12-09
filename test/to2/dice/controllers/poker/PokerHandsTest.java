package to2.dice.controllers.poker;

import to2.dice.game.Dice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PokerHandsTest {

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testIsPoker() throws Exception {
        assertTrue(PokerHands.isPoker(new int[]{5, 5, 5, 5, 5}));
        assertFalse(PokerHands.isPoker(new int[]{5, 5, 5, 5, 1}));
        assertFalse(PokerHands.isPoker(new int[]{6, 2, 4, 1, 3}));
    }

    @org.junit.Test
    public void testIsFour() throws Exception {
        assertTrue(PokerHands.isFour(new int[]{1, 2, 1, 1, 1}));
        assertFalse(PokerHands.isFour(new int[]{5, 5, 5, 5, 5}));
        assertFalse(PokerHands.isFour(new int[]{1, 5, 4, 3, 2}));
    }

    @org.junit.Test
    public void testIsFull() throws Exception {
        assertTrue(PokerHands.isFull(new int[]{3, 2, 3, 2, 3}));
        assertFalse(PokerHands.isFull(new int[]{3, 3, 3, 2, 1}));
        assertFalse(PokerHands.isFull(new int[]{5, 5, 1, 4, 3}));
        assertFalse(PokerHands.isFull(new int[]{3, 4, 2, 1, 5}));
    }

    @org.junit.Test
    public void testIsLargeStraight() throws Exception {
        assertTrue(PokerHands.isLargeStraight(new int[]{2, 3, 4, 5, 6}));
        assertFalse(PokerHands.isLargeStraight(new int[]{1, 2, 3, 4, 5}));
        assertFalse(PokerHands.isLargeStraight(new int[]{1, 3, 4, 5, 6}));
    }

    @org.junit.Test
    public void testIsSmallStraight() throws Exception {
        assertTrue(PokerHands.isSmallStraight(new int[]{1, 2, 3, 4, 5}));
        assertFalse(PokerHands.isSmallStraight(new int[]{2, 3, 4, 5, 6}));
        assertFalse(PokerHands.isSmallStraight(new int[]{1, 3, 4, 5, 6}));
    }

    @org.junit.Test
    public void testIsThree() throws Exception {
        assertTrue(PokerHands.isThree(new int[]{3, 3, 3, 2, 1}));
        assertFalse(PokerHands.isThree(new int[]{3, 3, 3, 2, 2}));
        assertFalse(PokerHands.isThree(new int[]{5, 5, 5, 5, 5}));
    }

    @org.junit.Test
    public void testIsTwoPairs() throws Exception {
        assertTrue(PokerHands.isTwoPairs(new int[]{2, 2, 4, 5, 5}));
        assertTrue(PokerHands.isTwoPairs(new int[]{5, 4, 2, 5, 4}));
        assertFalse(PokerHands.isTwoPairs(new int[]{1, 2, 3, 4, 5}));
        assertFalse(PokerHands.isTwoPairs(new int[]{3, 3, 3, 3, 1}));
        assertFalse(PokerHands.isTwoPairs(new int[]{5, 5, 5, 5, 5}));
    }

    @org.junit.Test
    public void testIsPair() throws Exception {
        assertTrue(PokerHands.isPair(new int[]{3, 4, 2, 1, 4}));
        assertFalse(PokerHands.isPair(new int[]{5, 5, 5, 5, 5}));
        assertFalse(PokerHands.isPair(new int[]{5, 5, 5, 5, 1}));
        assertFalse(PokerHands.isPair(new int[]{5, 5, 3, 3, 5}));
        assertFalse(PokerHands.isPair(new int[]{1, 2, 3, 4, 5}));
        assertFalse(PokerHands.isPair(new int[]{2, 3, 2, 1, 3}));
    }

    @org.junit.Test
    public void testGetHandType() throws Exception {
        Dice dice1 = new Dice(5);

        dice1.setDice(new int[]{1, 2, 3, 5, 6});
        assertEquals(PokerHands.getHandType(dice1), PokerHandType.HIGH_CARD);

        dice1.setDice(new int[]{2, 3, 2, 5, 6});
        assertEquals(PokerHands.getHandType(dice1), PokerHandType.PAIR);

        dice1.setDice(new int[]{1, 2, 2, 6, 2});
        assertEquals(PokerHands.getHandType(dice1), PokerHandType.THREE);

        dice1.setDice(new int[]{1, 2, 3, 4, 5});
        assertEquals(PokerHands.getHandType(dice1), PokerHandType.SMALL_STRAIGHT);

        dice1.setDice(new int[]{3, 4, 3, 3, 3});
        assertEquals(PokerHands.getHandType(dice1), PokerHandType.FOUR);

        dice1.setDice(new int[]{4, 4, 4, 4, 4});
        assertEquals(PokerHands.getHandType(dice1), PokerHandType.POKER);


    }
}