import java.awt.*;
import java.util.Scanner;

/**
 * The Shop class controls the cost of the items in the Treasure Hunt game. <p>
 * The Shop class also acts as a go between for the Hunter's buyItem() method. <p>
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Shop {
    // constants
    private static final int WATER_COST = 2;
    private static final int ROPE_COST = 4;
    private static final int MACHETE_COST = 6;
    private static final int SHOVEL_COST = 8;
    private static final int BOOTS_COST = 10;
    private static final int HORSE_COST = 12;
    private static final int BOAT_COST = 20;
    private static final int SWORD_COST = 0;
    private final String[] itemsList;
    private Hunter hunter;
    private int count;

    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private double markdown;
    private Hunter customer;
    private TreasureHunter treasureHunter;
    private OutputWindow window;

    /**
     * The Shop constructor takes in a markdown value and leaves customer null until one enters the shop.
     *
     * @param markdown Percentage of markdown for selling items in decimal format.
     */
    public Shop(double markdown, TreasureHunter th, Hunter hunter, OutputWindow window) {
        this.window = window;
        treasureHunter = th;
        this.hunter = hunter;
        this.markdown = markdown;
        customer = null; // customer is set in the enter method
        if (treasureHunter.getIsSamuraiMode()) {
            itemsList = new String[]{"water", "rope", "machete", "shovel", "boots", "horse", "boat", "sword"};
        } else {
            itemsList = new String[]{"water", "rope", "machete", "shovel", "boots", "horse", "boat"};
        }

    }

    /**
     * Method for entering the shop.
     *
     * @param hunter the Hunter entering the shop
     * @param buyOrSell String that determines if hunter is "B"uying or "S"elling
     * @return a String to be used for printing in the latest news
     */

    public String enter(Hunter hunter, String buyOrSell) {
        customer = hunter;
        if (buyOrSell.equals("b")) {
            window.clear();
            window.addTextToWindow("\nWelcome to the shop! We have the finest wares in town.", Color.black);
            window.addTextToWindow("\nCurrently we have the following items: ", Color.black);
            window.addTextToWindow("\n" + inventory(), Color.gray);
            window.addTextToWindow("\nWhat're you lookin' to buy? ", Color.green);
            String item = SCANNER.nextLine().toLowerCase();
            int cost = checkMarketPrice(item, true);
            if (treasureHunter.getIsSamuraiMode() & validItem(item)) {
                if (hunter.getHasSword()) {
                    buyItem(item);
                } else {
                    window.addTextToWindow("\nIt'll cost you " + cost + " gold. Buy it (y/n)? ", Color.cyan);
                    String option = SCANNER.nextLine().toLowerCase();
                    if (option.equals("y")) {
                        buyItem(item);
                    }
                }
            } else if (cost == 0 && !validItem(item)) {
                window.addTextToWindow("\nWe ain't got none of those.", Color.red);
            } else {
                window.addTextToWindow("\nIt'll cost you " + cost + " gold. Buy it (y/n)? ", Color.cyan);
                String option = SCANNER.nextLine().toLowerCase();
                if (option.equals("y")) {
                    buyItem(item);
                }
            }
        } else {
            window.addTextToWindow("\nWhat're you lookin' to sell? ", Color.green);
            window.addTextToWindow("\nYou currently have the following items: " + customer.getInventory(), Color.gray);
            String item = SCANNER.nextLine().toLowerCase();
            int cost = checkMarketPrice(item, false);
            if (cost == 0) {
                window.addTextToWindow("\nWe don't want none of those.", Color.red);
            } else {
                window.addTextToWindow("\nIt'll get you " + cost + " gold. Sell it (y/n)? ", Color.green);
                String option = SCANNER.nextLine().toLowerCase();
                if (option.equals("y")) {
                    sellItem(item);
                }
            }
        }
        return "You left the shop";
    }

    /**
     * A method that returns a string showing the items available in the shop
     * (all shops sell the same items).
     *
     * @return the string representing the shop's items available for purchase and their prices.
     */
    public String inventory() {
        String str = "Water: " + WATER_COST + " gold\n";
        str += "Rope: " + ROPE_COST + " gold\n";
        str += "Machete: " + MACHETE_COST + " gold\n";
        str += "Shovel: " + SHOVEL_COST + " gold\n";
        str += "Boots: " + BOOTS_COST + " gold\n";
        str += "Horse: " + HORSE_COST + " gold\n";
        str += "Boat: " + BOAT_COST + " gold\n";
        if (treasureHunter.getIsSamuraiMode()) {
            str += "Sword: " + SWORD_COST + " gold\n";
        }
        return str;
    }

    /**
     * A method that lets the customer (a Hunter) buy an item.
     *
     * @param item The item being bought.
     */
    public void buyItem(String item) {
        int costOfItem = checkMarketPrice(item, true);
        if (customer.buyItem(item, costOfItem)) {
            if (hunter.getHasSword()) {
                if (count >= 1) {
                    window.addTextToWindow("\nThe sword intimidates the shopkeeper and he gives you the item freely", Color.green);
                } else {
                    count++;
                    window.addTextToWindow("\nYe' got yerself a " + item + ". Come again soon.", Color.green);
                }
            } else {
                window.addTextToWindow("\nYe' got yerself a " + item + ". Come again soon.", Color.green);
            }
        } else {
            window.addTextToWindow("\nHmm, either you don't have enough gold or you've already got one of those!", Color.red);
        }
    }

    /**
     * A pathway method that lets the Hunter sell an item.
     *
     * @param item The item being sold.
     */
    public void sellItem(String item) {
        int buyBackPrice = checkMarketPrice(item, false);
        if (customer.sellItem(item, buyBackPrice)) {
            window.addTextToWindow("\nPleasure doin' business with you.", Color.green);
        } else {
            window.addTextToWindow("\nStop stringin' me along!", Color.red);
        }
    }

    /**
     * Determines and returns the cost of buying or selling an item.
     *
     * @param item The item in question.
     * @param isBuying Whether the item is being bought or sold.
     * @return The cost of buying or selling the item based on the isBuying parameter.
     */
    public int checkMarketPrice(String item, boolean isBuying) {
        if (isBuying) {
            return getCostOfItem(item);
        } else {
            return getBuyBackCost(item);
        }
    }

    /**
     * Checks the item entered against the costs listed in the static variables.
     *
     * @param item The item being checked for cost.
     * @return The cost of the item or 0 if the item is not found.
     */
    public int getCostOfItem(String item) {
        if (item.equals("water")) {
            return WATER_COST;
        } else if (item.equals("rope")) {
            return ROPE_COST;
        } else if (item.equals("machete")) {
            return MACHETE_COST;
        } else if (item.equals("shovel")) {
            return SHOVEL_COST;
        } else if (item.equals("boots")) {
            return BOOTS_COST;
        } else if (item.equals("horse")) {
            return HORSE_COST;
        } else if (item.equals("boat")) {
            return BOAT_COST;
        } else if (item.equalsIgnoreCase("sword")) {
            return SWORD_COST;
        } else {
            return 0;
        }
    }
    private boolean validItem(String item) {
        for (int i = 0; i < itemsList.length; i++) {
            if (item.equalsIgnoreCase(itemsList[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the cost of an item and applies the markdown.
     *
     * @param item The item being sold.
     * @return The sell price of the item.
     */
    public int getBuyBackCost(String item) {
        int cost = (int) (getCostOfItem(item) * markdown);
        return cost;
    }
}