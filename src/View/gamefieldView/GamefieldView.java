package View.gamefieldView;

import Model.Game;
import Model.gamefield.Cell;
import Model.gamefield.Direction;
import Model.gamefield.Gamefield;
import Model.units.interactive.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class GamefieldView extends JPanel {

    private final Gamefield _field;
    private HashMap<Cell, CellWidget> _cells = new HashMap<>();
    private final Game _game;

    public GamefieldView(Gamefield field, Game game) {
        if(field == null) {
            throw new NullPointerException("Поле не может быть null");
        }
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        addKeyListener(new KeyController());
        
        removeAll();

        _field = field;
        //тут подписать на события? я пока не оч понимаю кто на кого и как события будут работать

        _game = game;

        removeAll();

        setLayout(new GridLayout(_field.getHeight(), _field.getWidth(), 3,3));

        Dimension fieldDimension = new Dimension(
                CellWidget.CELL_SIZE*_field.getWidth(),
                CellWidget.CELL_SIZE*_field.getHeight()
        );
        setPreferredSize(fieldDimension);

        //тут должна быть инициализация клетками (ну скорее объявление виджетов для каждой клетки и соответсвенно юнитов в них)
        for(Cell c : _field) {
            CellWidget w = new CellWidget(c);
            _cells.put(c, w);

            add(w);
        }

        revalidate();
        repaint();
    }

    private class KeyController implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            //не надо
        }

        @Override
        public void keyPressed(KeyEvent e) {
            Player player = _game.getPlayer();
            if(player == null || _game.isOver()) {
                return;
            }

            Direction dir = null;

            switch(e.getKeyCode()) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    dir = Direction.NORTH;
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    dir = Direction.SOUTH;
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    dir = Direction.WEST;
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    dir = Direction.EAST;
                    break;
            }

            if(dir != null) {
                player.moveTo(dir);
                repaint(); //на всякий
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //не надо
        }
    }

    protected Color getActiveColor(){
        return new Color(73, 50, 70);
    }

    protected Color getInactiveColor() {
        return new Color(23, 4, 25);
    }

    protected void changeColor(Color c){
        for(CellWidget w : _cells.values()) {
            w.changeColor(c);
        }
    }

    protected void changeColorByActivity(){
        if(_field != null) {
            changeColor(getActiveColor());
        } else {
            changeColor(getInactiveColor());
        }
    }

}
