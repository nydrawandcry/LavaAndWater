package View.gamefieldView;

import Model.Game;
import Model.events.game.GameActionListener;
import Model.services.GameFactory;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private Game _game;
    private GameFactory _factory;
    private GamefieldView _fieldView;

    private final GameActionListener _gameListener = new GameActionHandler();

    public GameFrame() {
        super("Lava & Water");

        _factory = new GameFactory();
        startNewGame();

        pack();

        setResizable(false);
        setLocationRelativeTo(null);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void startNewGame() {
        if(_game != null) {
            return; //пока не знаю что сюда надо добавить
        }

        //тут создание игры и подписка GameFrame на события Game (и еще манипуляции с gamefieldView)
        _game = _factory.createGame();
        _game.addGameActionListener(_gameListener);

        _fieldView = new GamefieldView(_game.getField(), _game);
        setLayout(new BorderLayout());
        add(_fieldView, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private class GameActionHandler extends JFrame implements GameActionListener {
        @Override
        public void gameIsOver() {
            JOptionPane.showMessageDialog(
                    this,
                    "Вы дошли до выхода!",
                    "Победа!!!",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
