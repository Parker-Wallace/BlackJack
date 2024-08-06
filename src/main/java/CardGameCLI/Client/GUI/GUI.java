package CardGameCLI.Client.GUI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * A class for handling animations and graphics for a Blackjack game
 */
public class GUI {

    /**
     * Grabs the Welcome animation and plays its animation frames.
     */
    final public static void GAMEWelcome() {
        String[] frames = {
            "src/main/java/CardGameCLI/Client/GUI/Welcome/frame1.txt",
            "src/main/java/CardGameCLI/Client/GUI/Welcome/frame2.txt",
            "src/main/java/CardGameCLI/Client/GUI/Welcome/frame3.txt",
            "src/main/java/CardGameCLI/Client/GUI/Welcome/frame4.txt",
            "src/main/java/CardGameCLI/Client/GUI/Welcome/frame5.txt",
            "src/main/java/CardGameCLI/Client/GUI/Welcome/frame6.txt",
            "src/main/java/CardGameCLI/Client/GUI/Welcome/frame7.txt"
            };
            try {
                    playAnimation(frames, 400);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

    }

    /**
     * Grabs the Bust animation and plays its animation frames.
     */
    final public static void PRINTBUST () {        
        String[] frames = {
        "src/main/java/CardGameCLI/Client/GUI/BustAnimation/frame1.txt",
        "src/main/java/CardGameCLI/Client/GUI/BustAnimation/frame2.txt",
        "src/main/java/CardGameCLI/Client/GUI/BustAnimation/frame3.txt",
        "src/main/java/CardGameCLI/Client/GUI/BustAnimation/frame4.txt"
        };

        try {
            for (int i = 0; i < 3; i++) {
                playAnimation(frames, 400);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }}

    /**
     * A method for printing a series of .txt files to create an animation.
     * @param frames A simple String array containing a list of file paths to all the necessary .txt files as part of this animation.
     * @param delay The amount of time in milliseconds to wait between each frame print.
     * @throws IOException
     * @throws InterruptedException
     */
    public static void playAnimation(String[] frames, int delay) throws IOException, InterruptedException {
            for (String frame : frames) {
                clearConsole();
                printFrame(frame);
                Thread.sleep(delay);
            }
        }

    /**
     * Prints a .txt file to the console for the purposes of printing large ascii art animations.
     * @param framePath The file path of the .txt file to print to the console.
     * @throws IOException
     */
    private static void printFrame(String framePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(framePath));
        for (String line : lines) {
            System.out.println(line);
        }
    }

    /**
     * Seperates a stringifies array of cards on the comma ',' character..
     * Must be in the format "10v,9o,k$".
     * @param cards The Stringified array of cards delimmited by the comma ','.
     * @return A String array of all the cards passed into this method.
     */
    private static String[] getcards(String cards) {
            String[] result = cards.split(",");
            return result;
        }
        
    /**
     * Prints ascii art representation of the cards passed into this array on the same lines.
     * @param cards The stringified array of cards or card delimmited by the ',' comma character .
     */
    public static void printCards(String cards) {
        String[] cardsList = getcards(cards);
        String[] cardLines = new String[7];

        for (int i = 0; i < 7; i++) {
            StringBuilder lineBuilder = new StringBuilder();
            for (String card : cardsList) {
                char face = card.charAt(1);
                char value = card.charAt(0);
                
                if (value == '1') {
                String tenvalue = card.substring(0, 2);
                face = card.charAt(2);
                String[] lines = new String[7];
                lines[0] = "┌─────────┐";
                lines[1] = String.format("│ %-2s      │", tenvalue);
                lines[2] = "│         │";
                lines[3] = String.format("│    %s    │", face);
                lines[4] = "│         │";
                lines[5] = String.format("│      %-2s │", tenvalue);
                lines[6] = "└─────────┘";

                lineBuilder.append(lines[i]).append("");
                cardLines[i] = lineBuilder.toString();
            }
            else {
                String[] lines = new String[7];
                lines[0] = "┌─────────┐";
                lines[1] = String.format("│ %-2s      │", value);
                lines[2] = "│         │";
                lines[3] = String.format("│    %s    │", face);
                lines[4] = "│         │";
                lines[5] = String.format("│      %-2s │", value);
                lines[6] = "└─────────┘";

                lineBuilder.append(lines[i]).append("");
                cardLines[i] = lineBuilder.toString();}
                if (cardsList.length == 1) {
                    String[] lines = new String[7];
                    lines[0] = "┌─────────┐";
                    lines[1] = String.format("│         │");
                    lines[2] = "│         │";
                    lines[3] = String.format("│         │");
                    lines[4] = "│         │";
                    lines[5] = String.format("│         │");
                    lines[6] = "└─────────┘";
                    lineBuilder.append(lines[i]).append("");
                    cardLines[i] = lineBuilder.toString();
                }
            }
        }
        for (String line : cardLines) {
            System.out.println(line);
        }
    }

    /**
     * Prints a header to display the number of 'chips' available to the method caller.
     * @param chips
     */
    public static void printChips(int chips) {
        System.out.println("**********************");
        System.out.println("chips available: " + chips);
        System.out.println("**********************");
    }

    /**
     * Prints the chars to clear the text from the console for a clean GUI.
     */
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
