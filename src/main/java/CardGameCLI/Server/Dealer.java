package CardGameCLI.Server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import CardGameCLI.BlackJack;
import CardGameCLI.Hand;
import CardGameCLI.PlayingCard;
import CardGameCLI.Shoe;

/**
 * Represents a Dealer for a networked game of Blackjack.
 * contains all the necessary objects and methods to run a networked game of Blackjack.
 */
public class Dealer implements Runnable {
    private Socket clientSocket;
    private Shoe shoe;
    private gamestates action;
    private BlackJack game;
   

    /**
     * Creates an instanced object of type Dealer
     * @param socket the client socket in which this dealer will communicate and listen for commands
     */
    public Dealer(Socket socket) {
        this.clientSocket = socket;
        this.shoe = new Shoe(6);
        this.action = gamestates.START;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
         try (
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            ) {
                JSONParser parser = new JSONParser();
                JSONObject outputObject = new JSONObject();
                outputObject.put("options", CommandList(this.action));
                while (true) {
                    out.writeUTF(outputObject.toJSONString());
                    outputObject = new JSONObject();
                    String clientCommand = in.readUTF();
                    handleRequest(clientCommand, parser, outputObject);
    
                    }
        } catch (Exception e) {
        }
        
    }

    /**
     * Handles the command as given bby the Client over this Dealers Socket.
     * Builds up a JSONObject to send over the Output object for the Client to recieve
     * @param command The command which the Client has sent for this Dealer to interperet.
     *                Must be one of the options previoulsy provided by this Dealer
     * @param parser A JSON Parser which will be used to grab JSON objects as sent by the Client.
     * @param outputObject The JSON object which the method will add to to be sent to the client.
     * @throws ParseException
     * @throws InterruptedException
     */
    @SuppressWarnings("unchecked")
    private void handleRequest(String command, JSONParser parser, JSONObject outputObject) throws ParseException, InterruptedException {
        JSONObject commandAsJsonObject = (JSONObject) parser.parse(command);
        String request = ((String) commandAsJsonObject.get("command")).trim().toLowerCase();
        switch (request) {
            case "bet" -> {
                int userbet = ((Number) commandAsJsonObject.get("pot")).intValue();
                this.game = new BlackJack(new ArrayList<>(Arrays.asList(deal(), deal())), new ArrayList<>(Arrays.asList(deal(), deal())), userbet );
                outputObject.put("cards",  game.playerHand.toString());
                outputObject.put("dealercards", game.dealerHand.cards.get(0).toString());
                this.action = gamestates.INPLAY;
            }
            case "hit" -> {
                game.hit(deal(), game.playerHand);
                if (game.gameState == false) {
                    outputObject.put("status", "bust");
                    this.action = gamestates.END;
                } else {
                    outputObject.put("cards",  game.playerHand.toString());
                    outputObject.put("dealercards", game.dealerHand.cards.get(0).toString()); 
                }
            }
            case "stay" -> {
                while (willHit(game.dealerHand)) {
                    game.hit(deal(), game.dealerHand);
                }
                if (game.checkWinner() == game.playerHand) {
                    outputObject.put("cards",  game.playerHand.toString());
                    outputObject.put("dealercards", game.dealerHand.toString());
                    outputObject.put("message", "player wins");
                    outputObject.put("winnings", game.bet * 2);
                } else {
                    outputObject.put("cards",  game.playerHand.toString());
                    outputObject.put("dealercards", game.dealerHand.toString());
                    outputObject.put("message","house wins");
                }
                this.action = gamestates.END;
            }
            default -> {
                outputObject.put("message", "sorry i didnt quit understand " + "\"" + request + "\"" + '\n' + outputObject.get("message"));}
        }
        outputObject.put("options", CommandList(this.action));
    }

    /**
     * GameStates for this Dealer to decide which commands and actions this Dealer should take
     */
    public enum gamestates {
        START,
        INPLAY,
        DEALER,
        END
    }

    /**
     * Checks wether or not the dealer will 'hit' on their hand at any given point.
     * @param hand this Dealer's hand as part of the running Blackjack game.
     * @return Wether or not this Dealer will 'hit' on their current hand, based on score.
     */
    public boolean willHit(Hand hand) {
        return hand.getScore() < 17;
    }

    public List<String> CommandList(gamestates currentState) {
        List<String> options = new ArrayList<String>();
        if (currentState == gamestates.START || currentState == gamestates.END) {
            options.add("Bet");
        }
        else if (currentState == gamestates.INPLAY) {
            options.add("Hit");
            options.add("stay");
        }
        options.add("Exit");
        return options;
    }

    /**
     * Deals a card from this Dealer's Shoe.
     * @return a PlayingCard from this Dealer's Shoe
     */
    public PlayingCard deal() {
        return shoe.drawCard();
    }
}
