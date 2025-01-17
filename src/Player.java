import java.awt.Color;

public class Player {
    private String name;
    private OutputWindow window;

    public Player(String name, OutputWindow window) {
        this.name = name;
        this.window = window;
    }

    public void welcome() {
        window.clear();
        window.addTextToWindow("Welcome " + name, Color.blue);
    }
}
