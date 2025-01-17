import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class OutputWindow {

    private StyledDocument doc;
    private Style style;
    private JTextPane textPane;

    public OutputWindow() {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // causes program to end when window is X'd out
        frame.setSize(500, 400); // window size
        frame.setLocation(300, 50); // where on screen window appears
        textPane = new JTextPane(); // panel that can handle custom text
        textPane.setEditable(false); // prevents user from typing into window
        doc = textPane.getStyledDocument(); // call getter method for panel's style doc
        style = doc.addStyle("my style", null); // add a custom style to the doc
        StyleConstants.setFontSize(style, 25); // apply font size to custom style
        frame.add(textPane); // add the panel to the frame
        frame.setVisible(true); // display the frame on screen
    }

    public void addTextToWindow(String text, Color color) {
        StyleConstants.setForeground(style, color); // apply color to custom style
        try {
            doc.insertString(doc.getLength(), text, style); } // insert text at end the panel
        catch (Exception e) { }
    }

    public void clear() {
        textPane.setText("");  // set panel's text to empty string to "reset it"
    }
}