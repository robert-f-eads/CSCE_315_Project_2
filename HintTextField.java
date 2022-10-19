import javax.swing.JTextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.*;


/**
 * @author Robert Eads
 */
public class HintTextField extends JTextField implements FocusListener {
    private String myHint;

    /**
     * Parametrized constructor for a TextField that acts like it has a hint 
     * @param hint the hint to be displayed
     * @param width the width of the text field
     * @param height the height of the text field
     */
    public HintTextField(String hint, int width, int height) {
        super(hint);
        myHint = hint;
        this.setSize(width, height);
        this.addFocusListener(this);
        this.setForeground(Color.lightGray);
    }

    /**
     * Clears the hint to prepare the text field for input
     * @param e the event which causes the text field to gain focus
     */
    @Override
    public void focusGained(FocusEvent e) {
        if(this.getText().equals(myHint)) {
            this.setForeground(Color.black);
            this.setText("");
        }
    }

    /**
     * Resets the text field to display the hint, grayed out
     * @param e the event which causes the text field to lose focus
     */
    @Override
    public void focusLost(FocusEvent e) {
        if(this.getText().equals("")) {
            this.setForeground(Color.lightGray);
            this.setText(myHint);
        }
    }

    public void resetText() {
        this.setText(myHint);
    }

    public String getHint() {
        return myHint;
    }
}
