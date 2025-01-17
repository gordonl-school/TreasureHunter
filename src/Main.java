import java.awt.Color;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OutputWindow window = new OutputWindow(); // only want one OutputWindow object
        Scanner scan = new Scanner(System.in);

        window.addTextToWindow("Enter next player's name ", Color.cyan);
        String name = scan.nextLine();
        Player player = new Player(name, window); // pass window to Player as parameter
        player.welcome();
        Color orange = new Color(240, 80, 20); // RGB!
        window.addTextToWindow("\n\nPress enter to continue...", orange);
        scan.nextLine();

        String choice = "";
        while (!choice.equals("q")) {
            window.clear();
            window.addTextToWindow("MAIN MENU ", Color.black);
            window.addTextToWindow("\n(a)ttack", Color.black);
            window.addTextToWindow("\n(d)odge", Color.black);
            window.addTextToWindow("\n(q)uit", Color.black);
            window.addTextToWindow("\n\nEnter your move: ", Color.black);
            choice = scan.nextLine();
            if (choice.equals("a")) {
                window.clear();
                window.addTextToWindow("YOU ATTACK FOR 10!", Color.red);
                window.addTextToWindow("\n\nPress enter to continue...", Color.black);
                scan.nextLine();
            } else if (choice.equals("d")) {
                window.clear();
                window.addTextToWindow("YOU DODGE THE ATTACK!\nTHE DRAGON IS ENRAGED!", Color.orange);
                window.addTextToWindow("\n\nPress enter to continue...", Color.black);
                scan.nextLine();
            }
        }

        window.clear();
        window.addTextToWindow("Goodbye! Close window to exit.", Color.black);
    }
}
