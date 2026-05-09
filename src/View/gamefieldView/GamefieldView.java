package View.gamefieldView;

import Model.gamefield.Cell;
import Model.gamefield.Gamefield;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class GamefieldView extends JPanel {

    private final Gamefield _field;
    private HashMap<Cell, CellWidget> _cells = new HashMap<>();

    public GamefieldView(Gamefield field) {
        if(field == null) {
            throw new NullPointerException("Поле не может быть null");
        }
        setFocusable(true);

        removeAll();

        _field = field;
        //тут подписать на события? я пока не оч понимаю кто на кого и как события будут работать

        removeAll();

        setLayout(new GridLayout(_field.getHeight(), _field.getWidth(), 3,3));

        Dimension fieldDimension = new Dimension(
                CellWidget.CELL_SIZE*_field.getWidth(),
                CellWidget.CELL_SIZE*_field.getHeight()
        );
        setPreferredSize(fieldDimension);

        //тут должна быть инициализация клетками (ну скорее объявление виджетов для каждой клетки и соответсвенно юнитов в них)

        revalidate();
        repaint();
    }
}
