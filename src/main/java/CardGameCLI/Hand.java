package CardGameCLI;

import java.util.ArrayList;

/**
 * Represents a Hand in a game of Blackjack.
 * 
 * Contains methods for calculating score and printing the hands cards.
 */
public class Hand {
    public ArrayList<PlayingCard> cards;

    /**
     * Creates an instance of Hand.
     * 
     * @param cards an ArrayList containing the first 2 cards dealt in a game of blackjack.
     */
    public Hand(ArrayList<PlayingCard> cards) {
        this.cards = cards;

    }

    /**
     * Adds a PlayingCard to this Hand.
     * 
     * @param card the card being dealt to this Hand.
     */
    public void addCard(PlayingCard card){
        this.cards.add(card);
    }

    /**
     * Gets the score of this Hand according to blackjack rules.
     * 
     * @return The current score of this Hand according to blackjack rules.
     */
    public int getScore() {
        int score = 0;
        int aceCount = 0;
        for (PlayingCard card: this.cards) {
            if (card.getValue() > 10) {
                score += 10;
            }
            else if (card.getValue() == 1) {
                aceCount += 1;
            }
            else {
                score += card.getValue();
            }
        }
        for (int i = aceCount; i > 0; i--) {
            score += 11;
            if (score > 21) {
                score -= 10;
            }
        }
        return score;
    }

    @Override
    public String toString() {
        StringBuilder cardsasString = new StringBuilder();
        for (PlayingCard card : this.cards) {
            cardsasString.append(card.toString());
            cardsasString.append(",");
        }
        return cardsasString.toString();
    }

}


