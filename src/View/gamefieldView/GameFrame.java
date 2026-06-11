package View.gamefieldView;

import Model.Game;
import Model.events.game.GameActionListener;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private Game _game;
    //private GameFactory _factory;
    private GamefieldView _fieldView;

    private final GameActionListener _gameListener = new GameActionHandler();

    public GameFrame() {
        super("Lava & Water");

        //_factory = new GameFactory();
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
        //_game = _factory.createGame();
        _game.addGameActionListener(_gameListener);

        _fieldView = new GamefieldView(_game.getField(), _game);
        setLayout(new BorderLayout());
        add(_fieldView, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private class GameActionHandler implements GameActionListener {
        @Override
        public void gameIsWon() {
            JOptionPane.showMessageDialog(
                    GameFrame.this,
                    "Вы дошли до выхода!",
                    "Победа!!!",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        }

        @Override
        public void gameIsLost() {
            String[] options = {
                    "Попробовать снова",
                    "Выйти"
            };

            int res = JOptionPane.showOptionDialog(
                    GameFrame.this,
                    "Вы проиграли!\nХотите попробовать снова?",
                    "Поражение :(",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if(res == 0) {
                restartGame();
            }
            else {
                System.exit(0);
            }
        }
    }

    private void restartGame() {
        _game.removeGameActionListener(_gameListener);
        remove(_fieldView);
        _game = null;

        startNewGame();

        pack();

        revalidate();
        repaint();
    }
}
