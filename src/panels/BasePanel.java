package panels;

import database.Help;
import frames.BarFrame;

import javax.swing.*;
import java.awt.Color;


public class BasePanel extends JPanel {
    public final BarFrame frame;
    Help help;
    public BasePanel(BarFrame frame){
        this.frame = frame;
        this.help = frame.help;
        setLayout(null);
        setBackground(Color.lightGray);
    }

    public boolean showQuestionPopup(String message) {
        int result = JOptionPane.showConfirmDialog(null, message);
        return result == JOptionPane.YES_OPTION;
    }
}
