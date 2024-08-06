package CardGameCLI;

import java.util.ArrayList;

/**
 * Represents a game of Blackjack contains methods to hit and check the winner of a game between dealer and player.
 */
public class BlackJack {
    public Boolean gameState;
    public Hand dealerHand;
    public Hand playerHand;
    public Integer bet;

    /**
     * Created an instance of Blackjack.
     * @param playerCards The initial 2 cards dealt to the player as part of this Blackjack game.
     * @param dealerCards The initial 2 cards dealt to the dealer as part of this Blackjack game.
     * @param bet The intial bet of the player needed to instatiate this Blackjack game.
     */
    public BlackJack(ArrayList<PlayingCard> playerCards , ArrayList<PlayingCard> dealerCards, Integer bet) {
        this.gameState = true;
        this.dealerHand = new Hand(dealerCards);
        this.playerHand = new Hand(playerCards);
        this.bet = bet;
    }

    /**
     * Adds a Playingcard to one of this Blackjack games hands.
     * @param card The PlayingCard being added to one of this Blackjack game's hands.
     * @param hand The Hand of this Backjack game to add the card parameter too.
     */
    public void hit(PlayingCard card, Hand hand) {
        hand.addCard(card);
        if (hand.getScore() > 21) {
            gameState = false;
        }
    }

    /**
     * Checks the winner of this blackjack game
     * @return the Hand object belonging to the winneer.
     *         either playerHand or dealerHand 
     */
    public Hand checkWinner() {
        if (this.playerHand.getScore() > this.dealerHand.getScore() && this.playerHand.getScore() < 22 || this.dealerHand.getScore() > 21) {
            return this.playerHand;
        }
        else return this.dealerHand;
    }
}
