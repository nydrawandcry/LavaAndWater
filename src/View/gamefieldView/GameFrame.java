package View.gamefieldView;

import Model.Game;

import javax.swing.*;

public class GameFrame extends JFrame {

    private Game _game;
    private GamefieldView _fieldView;

    public GameFrame() {
        super("Lava & Water");

        startNewGame();

        pack();

        setResizable(true);
        setLocationRelativeTo(null);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void startNewGame() {
        if(_game != null) {
            return; //пока не знаю что сюда надо добавить
        }

        //тут создание игры и подписка GameFrame на события Game (и еще манипуляции с gamefieldView)

        revalidate();
        repaint();
    }
}
