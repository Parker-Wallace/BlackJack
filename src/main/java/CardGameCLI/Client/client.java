package CardGameCLI.Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException; 
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import CardGameCLI.Client.GUI.GUI;

/**
 * Represents a Player in a game of networked Blackjack.
 */
class Client {
	private int chips;

	/**
	 * Creates an instance of Client with 100 'chips' to bet with.
	 */
	public Client () {
		this.chips = 100;
	}

	/**
	 * Main method for this Client.
	 * Contains While loop to connect, listen to and play the Blackjack game(s).
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		Client client = new Client();
		try (
			Socket socket = new Socket("localhost", 1234);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);
			) {
				GUI.GAMEWelcome();
				TimeUnit.SECONDS.sleep(1);
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = new JSONObject();
				while (true) {
					String inboundMessage = in.readUTF();
					JSONObject inboundObject = (JSONObject) parser.parse(inboundMessage);

					GUI.clearConsole();
					handleObject(inboundObject, client);

					String command;
					while (true) {
						System.out.print("Enter command: ");
						command = scanner.nextLine().trim();
						if (!checkCommand(getOptions(inboundObject), command)) {
							System.out.println("command " + command + " not accepted please try again");
						}
						else {
							break;
						}						
					}
                if ("exit".equalsIgnoreCase(command)) {
                    break;
                }
				if ("bet".equalsIgnoreCase(command)) {		
					if (client.chips == 0) {
					System.out.println("looks like youre out of money...\n come back when you have more");
					System.exit(1);
					}
					while (true) { 
						System.out.println("how much would you like to bet?");
						int betAmount = -1;
						if (scanner.hasNextInt()) {
							betAmount = scanner.nextInt();
							scanner.nextLine(); // Consume the newline character left after nextInt()
						} else {
							System.out.println("Please enter a valid number.");
							scanner.next(); // Clear invalid input
						}
						
						if (betAmount < 0 || betAmount > client.chips) {
							System.out.println("You don't have enough chips to make that kind of bet.");
						} else {
							client.chips -= betAmount;
							jsonObject.put("pot", betAmount);
							break;
						}
					}
				}
                jsonObject.put("command", command);
                out.writeUTF(jsonObject.toString());
			} 
		} 
		catch (Exception e) { 
			e.printStackTrace(); 
		} 
	}

	/**
	 * Checks wether or not the command given exists within the options given provided by the connected Dealer object.
	 * @param options The objects provided by the Dealer object.
	 * @param command The command issued by the user.
	 * @return True if the command exists within the provided options, otherwise false.
	 */
	private static Boolean checkCommand(JSONArray options, String command) {
		boolean found = false;
        for (Object option : options) {
            if (option.toString().equalsIgnoreCase(command)) {
                found = true;
                break;
            }
        }
		return found;
	}

	/**
	 * gets the value of the "options" key from the inboundobject sent by the Dealer
	 * @param inboundObject the JSONObject which was sent this this Client by the Dealer.
	 * @return an Array of options.
	 * @throws IOException
	 * @throws ParseException
	 */
	private static JSONArray getOptions(JSONObject inboundObject) throws IOException, ParseException {
		return (JSONArray) inboundObject.get("options");
    }

	/**
	 * Handles the JSONObject sent by the Dealer to this Client.
	 * @param inboundObject The JSONObject sent to this Client by the Dealer
	 * @param client this Client for the purpose of changing the chip value
	 * @throws IOException
	 * @throws ParseException
	 */
	private static void handleObject(JSONObject inboundObject, Client client) throws IOException, ParseException {
		if (inboundObject.containsKey("status")) {
			if (inboundObject.get("status").equals("bust")) {
				GUI.PRINTBUST();
				GUI.clearConsole();
			}
			
		}
		if (inboundObject.containsKey("winnings")) {
			client.chips += ((Number) inboundObject.get("winnings")).intValue();
		}
		GUI.printChips(client.chips);
		if (inboundObject.containsKey("message")) {
			System.out.println(inboundObject.get("message"));
			System.out.println("**************************");
		}
		if (inboundObject.containsKey("cards")) {
			GUI.printCards((String) inboundObject.get("dealercards"));
			GUI.printCards((String) inboundObject.get("cards"));
			System.out.println("**************************");
		}
		if (inboundObject.containsKey("options")) {
			for (Object option : getOptions(inboundObject)) {
				System.out.println(option);
			}
		}

	}
	
}