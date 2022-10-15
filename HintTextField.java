import javax.swing.JTextField;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.*;


public class HintTextField extends JTextField implements FocusListener {
    private String myHint;

    public HintTextField(String hint, int width, int height) {
        super(hint);
        myHint = hint;
        this.setSize(width, height);
        this.addFocusListener(this);
        this.setForeground(Color.lightGray);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(this.getText().equals(myHint)) {
            this.setForeground(Color.black);
            this.setText("");
        }
    }

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
