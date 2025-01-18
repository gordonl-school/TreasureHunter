import java.awt.*;
import java.sql.SQLOutput;
import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class TreasureHunter {
    OutputWindow window = new OutputWindow(); // only want one OutputWindow object
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);
    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean samuraiMode;
    private boolean hardMode;
    private boolean easyMode;
    private boolean normalMode;
    private boolean currentMode;

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        easyMode = false;
        normalMode = false;
        samuraiMode = false;
        currentMode = false;
    }

    public boolean getIsSamuraiMode() {
        return samuraiMode;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        window.addTextToWindow("Welcome to TREASURE HUNTER!", Color.black);
        window.addTextToWindow("\nGoing hunting for the big treasure, eh?", Color.cyan);
        window.addTextToWindow("\nWhat's your name, Hunter?: ", Color.cyan);
        String name = SCANNER.nextLine().toLowerCase();
        Player player = new Player(name, window); // pass window to Player as parameter

        // set hunter instance variable
        hunter = new Hunter(name, 20, window);

        window.addTextToWindow("\nEasy Mode(e), Normal Mode(n), or Hard Mode(h): ", Color.cyan);
        String hard = SCANNER.nextLine().toLowerCase();
        window.clear();
        if (hard.equals("h")) {
            hardMode = true;
            currentMode = hardMode;
        } else if (hard.equals("e")) {
            easyMode = true;
            currentMode = easyMode;
        } else {
            normalMode = true;
            currentMode = normalMode;
        }
        if (hard.equalsIgnoreCase("test")) {
            hunter = new Hunter(name, 100, window);
        } else if (easyMode) {
            hunter = new Hunter(name, 40, window);
        } else if (hard.equalsIgnoreCase("s")){
            samuraiMode = true;
            hunter = new Hunter(name, 20, samuraiMode, window);
        } else {
            hunter = new Hunter(name, 20, window);
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;

            // and the town is "tougher"
            toughness = 0.75;
        } else if (easyMode) {
            markdown = 1;
            toughness = 0.1;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown, this, hunter, window);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, this, window);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";
        while (!choice.equals("x")) {
            window.addTextToWindow(currentTown.getLatestNews(), Color.green);
            window.addTextToWindow("\n***", Color.cyan);
            window.addTextToWindow("\n" + hunter.infoString(), Color.blue);
            window.addTextToWindow("\n" + hunter.infoTreasureList(), Color.magenta);
            window.addTextToWindow("\n" + currentTown.infoString(), Color.green);
            window.addTextToWindow("\n(B)uy something at the shop.", Color.black);
            window.addTextToWindow("\n(S)ell something at the shop.", Color.black);
            window.addTextToWindow("\n(E)xplore surrounding terrain.", Color.black);
            window.addTextToWindow("\n(M)ove on to a different town.", Color.black);
            window.addTextToWindow("\n(H)unt for treasure.", Color.black);
            window.addTextToWindow("\n(L)ook for trouble!", Color.black);
            window.addTextToWindow("\n(D)ig for gold.", Color.black);
            window.addTextToWindow("\nGive up the hunt and e(X)it.", Color.black);
            window.addTextToWindow("\nWhat's your next move?: ", Color.black);
            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice);
            if (currentTown.getHasLost()) {
                break;
            }
            if (hunter.getIsFoundTreasure()) {
                window.addTextToWindow("\nCongratulations, you have found the last of the three treasures, you win!", Color.green);
                break;
            }
            window.clear();
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            window.clear();
            currentTown.enterShop(choice);
            window.addTextToWindow("\nPress enter to continue", Color.black);
            SCANNER.nextLine();
            window.clear();
        } else if (choice.equals("e")) {
            window.addTextToWindow("\n"+ currentTown.getTerrain().infoString(), Color.green);
//            window.addTextToWindow("\nPress enter to continue", Color.black);
//            SCANNER.nextLine();
//            window.clear();
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                window.addTextToWindow("\n"+ currentTown.getLatestNews(), Color.green);
                enterTown();
                window.addTextToWindow("\nPress enter to continue", Color.black);
                SCANNER.nextLine();
                window.clear();
                hunter.setIsSearched(false);
            }
        } else if (choice.equals("h")) {
            currentTown.huntForTreausre();
            if (!hunter.getIsFoundTreasure()) {
                window.addTextToWindow("\nPress enter to continue", Color.black);
                SCANNER.nextLine();
                window.clear();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
            if (!hunter.isGoldNegative()) {
                window.addTextToWindow("\nPress enter to continue", Color.black);
                SCANNER.nextLine();
                window.clear();
            }
            System.out.println("made back to treasurehunt");
        } else if (choice.equals("x")) {
            window.addTextToWindow("\nFare thee well, " + hunter.getHunterName() + "!", Color.red);
        } else if (choice.equals("d")) {
            currentTown.digForGold();
//            window.addTextToWindow("\nPress enter to continue", Color.black);
//            SCANNER.nextLine();
//            window.clear();
        } else {
            window.addTextToWindow("\nYikes! That's an invalid option! Try again.", Color.red);
            window.addTextToWindow("\nPress enter to continue", Color.black);
            SCANNER.nextLine();
            window.clear();
        }
    }

    public boolean getCurrentMode() {
        return currentMode;
    }

    public boolean getEasyMode() {
        return easyMode;
    }
}