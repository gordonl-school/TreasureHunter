import java.awt.*;

/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private boolean dug;
    private TreasureHunter treasureHunter;
    private boolean hasLost;
    private OutputWindow window;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, TreasureHunter th, OutputWindow window) {
        this.window = window;
        this.shop = shop;
        this.terrain = getNewTerrain();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        dug = false;
        treasureHunter = th;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getLatestNews() {
        return printMessage;
    }

    public boolean getHasLost() {
        return hasLost;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (treasureHunter.getCurrentMode() != treasureHunter.getEasyMode()) {
                if (checkItemBreak()) {
                    hunter.removeItemFromKit(item);
                    printMessage += "\nUnfortunately, you lost your " + item;
                }
            }
            return true;
        } else {
            printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
            return false;
        }
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        printMessage = shop.enter(hunter, choice);
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void huntForTreausre() {
        if (!hunter.getIsSearched()) {
            hunter.addTreasure();
        } else {
            window.addTextToWindow("\nYou have already searched this town.", Color.red);
        }

    }
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }
        if (Math.random() > noTroubleChance) {
            window.addTextToWindow("\nYou couldn't find any trouble", Color.gray);
        } else {
            window.addTextToWindow("\nYou want trouble, stranger! You got it!\nOof! Umph! Ow!\n", Color.red);
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (Math.random() > noTroubleChance || hunter.getHasSword()) {
                if (hunter.getHasSword()) {
                    window.addTextToWindow("\nThe brawler, seeing your sword, realizes he picked a losing fight and gives you his gold", Color.green);
                } else {
                    window.addTextToWindow("\nOkay, stranger! You proved yer mettle. Here, take my gold.", Color.green);
                }
                window.addTextToWindow("\nYou won the brawl and receive " + goldDiff + " gold.", Color.green);
                printMessage = "You won a brawl";
                hunter.changeGold(goldDiff);
            } else {
                window.addTextToWindow( "\nThat'll teach you to go lookin' fer trouble in MY town! Now pay up!", Color.red);
                window.addTextToWindow("\nYou lost the brawl and pay " + goldDiff + " gold.", Color.red);
                printMessage = "You lost a brawl";
                hunter.changeGold(-goldDiff);
                if (hunter.isGoldNegative()) {
                    window.addTextToWindow("\n" + printMessage, Color.red);
                    hasLost = true;
                    window.addTextToWindow("\nGame Over!", Color.red);
                }
            }
        }

    }

    public void digForGold() {
        if (dug) {
            printMessage = "You already dug for gold in this town.\n";
        } else {
            if (hunter.hasItemInKit("shovel")) {
                int chance = (int) (Math.random() * 2) + 1;
                if (chance == 1) {
                    int amountReceived = (int) (Math.random() * 20) + 1;
                    hunter.changeGold(amountReceived);
                    printMessage = "You dug up " + amountReceived + " gold!\n";
                    dug = true;
                } else {
                    printMessage = "You dug but only found dirt.\n";
                    dug = true;
                }
            } else {
                printMessage = "You can't dig for gold without a shovel.\n";
            }
        }
    }

    public String infoString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < .166) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < .333) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < .5) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < .666) {
            return new Terrain("Desert", "Water");
        } else if (rnd < .833){
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}