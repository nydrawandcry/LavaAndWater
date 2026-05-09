package View.gamefieldView;

import javax.swing.*;
import java.awt.*;

public class CellWidget extends JPanel {

    public static int CELL_SIZE = 50;


    void changeColor(Color c) {
        setBackground(c);
        repaint();
    }
}
