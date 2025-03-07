import java.awt.*;

/**
 * Hunter Class<br /><br />
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Hunter {
    //instance variables
    private String hunterName;
    private String[] kit;
    private String[] treasureCollection;
    private int gold;
    private boolean goldNegative;
    private boolean isSearched;
    private boolean foundTreasure;
    private boolean samuraiMode;
    private boolean hasSword;
    private OutputWindow window;

    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param hunterName The hunter's name.
     * @param startingGold The gold the hunter starts with.
     */
    public Hunter(String hunterName, int startingGold, OutputWindow window) {
        this.window = window;
        this.hunterName = hunterName;
        kit = new String[7]; // only 7 possible items can be stored in kit
        treasureCollection = new String[3]; // 3 possible items
        gold = startingGold;
        if (gold == 100) {
            addItem("water");
            addItem("rope");
            addItem("machete");
            addItem("shovel");
            addItem("boots");
            addItem("horse");
            addItem("boat");
        }
    }
    public Hunter(String hunterName, int startingGold, boolean startingMode, OutputWindow window) {
        this.window = window;
        this.hunterName = hunterName;
        kit = new String[8]; // only 8 possible items can be stored in kit
        treasureCollection = new String[3]; // 3 possible items
        gold = startingGold;
        samuraiMode = startingMode;
    }


    //Accessors
    public String getHunterName() {
        return hunterName;
    }

    public boolean isGoldNegative() {
        return goldNegative;
    }

    public boolean getIsFoundTreasure() {
        return foundTreasure;
    }

    public boolean getHasSword() {
        return hasSword;
    }

    // Checks for treasure
    public boolean getIsSearched() {
        return isSearched;
    }

    public void setIsSearched(boolean newValue) {
        isSearched = newValue;
    }

    /**
     * Updates the amount of gold the hunter has.
     *
     * @param modifier Amount to modify gold by.
     */
    public void changeGold(int modifier) {
        gold += modifier;
        if (gold < 0) {
            goldNegative = true;
        }
    }

    /**
     * Buys an item from a shop.
     *
     * @param item The item the hunter is buying.
     * @param costOfItem The cost of the item.
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem) {
        if (costOfItem == 0 & !item.equalsIgnoreCase("sword") || (gold < costOfItem && !hasSword) || hasItemInKit(item)) {
            return false;
        }
        if (samuraiMode && item.equalsIgnoreCase("sword")) {
            hasSword = true;
            addItem(item);
            return true;
        }
        if (hasSword) {
            addItem(item);
            return true;
        }
        gold -= costOfItem;
        addItem(item);
        return true;
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item The item being sold.
     * @param buyBackPrice the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= 0 || !hasItemInKit(item)) {
            return false;
        }
        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    /**
     * Removes an item from the kit by setting the index of the item to null.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item) {
        int itmIdx = findItemInKit(item);

        // if item is found
        if (itmIdx >= 0) {
            kit[itmIdx] = null;
        }
    }

    /**
     * Checks to make sure that the item is not already in the kit.
     * If not, it assigns the item to an index in the kit with a null value ("empty" position).
     *
     * @param item The item to be added to the kit.
     * @return true if the item is not in the kit and has been added.
     */
    private boolean addItem(String item) {
        if (!hasItemInKit(item)) {
            int idx = emptyPositionInKit();
            kit[idx] = item;
            return true;
        }
        return false;
    }


    /**
     * Checks if the kit Array has the specified item.
     *
     * @param item The search item
     * @return true if the item is found.
     */
    public boolean hasItemInKit(String item) {
        for (String tmpItem : kit) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }

    public boolean hasItemInTreasure(String item) {
        for (String tmpItem : treasureCollection) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }

     /**
     * Returns a printable representation of the inventory, which
     * is a list of the items in kit, with a space between each item.
     *
     * @return The printable String representation of the inventory.
     */
    public String getInventory() {
        String printableKit = "";
        String space = " ";

        for (String item : kit) {
            if (item != null) {
                printableKit += item + space;
            }
        }
        return printableKit;
    }
    public String getTreasureInventory() {
        String printableTreasureList = "";
        String space = " ";

        for (String item: treasureCollection) {
            if (item != null) {
                printableTreasureList += item + space;
            }
        }
        return printableTreasureList;
    }

    /**
     * @return A string representation of the hunter.
     */
    public String infoString() {
        String str = hunterName + " has " + gold + " gold";
        if (!kitIsEmpty()) {
            str += " and " + getInventory();
        }
        return str;
    }
    public String infoTreasureList() {
        String message = "";
        if (treasureIsEmpty()) {
            message += "Treasures found: none";
        } else {
            message += "Treasures found: " + getTreasureInventory();
        }
        return message;
    }

    public String chooseTreasure() {
        String[] gemList = {"Crown", "Trophy", "Gem", "Dust"};
        return gemList[(int) (Math.random() * (4))];
    }

    public void addTreasure() {
        String chosenGem = chooseTreasure();
        isSearched = true;
        if (!hasItemInTreasure(chosenGem)) {
            window.addTextToWindow("\nYou found a " + chosenGem + "!", Color.magenta);
            if (!chosenGem.equalsIgnoreCase("Dust")) {
                int idx = emptyPositionTreasure();
                treasureCollection[idx] = chosenGem;
            }
        } else if (hasItemInTreasure(chosenGem)){
            window.addTextToWindow("\nYou already have " + chosenGem + " in your collection so you don't collect it.", Color.red);
        }
        if (itemsInTreasureList() == 3) {
            foundTreasure = true;
        }
    }


    /**
     * Searches kit Array for the index of the specified value.
     *
     * @param item String to look for.
     * @return The index of the item, or -1 if not found.
     */
    private int findItemInKit(String item) {
        for (int i = 0; i < kit.length; i++) {
            String tmpItem = kit[i];

            if (item.equals(tmpItem)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if the kit is empty - meaning all elements are null.
     *
     * @return true if kit is completely empty.
     */
    private boolean kitIsEmpty() {
        for (String string : kit) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }
    private int itemsInTreasureList() {
        int count = 0;
        for (String string : treasureCollection) {
            if (string != null) {
                count++;
            }
        }
        return count;
    }

    private boolean treasureIsEmpty() {
        for (String string : treasureCollection) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds the first index where there is a null value.
     *
     * @return index of empty index, or -1 if not found.
     */
    private int emptyPositionInKit() {
        for (int i = 0; i < kit.length; i++) {
            if (kit[i] == null) {
                return i;
            }
        }
        return -1;
    }
    private int emptyPositionTreasure() {
        for (int i = 0; i < treasureCollection.length; i++) {
            if (treasureCollection[i] == null) {
                return i;
            }
        }
        return -1;
    }


}