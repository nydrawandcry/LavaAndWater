package View.gamefieldView;

import Model.Game;
import Model.events.game.GameActionListener;
import Model.services.GameFactory;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame implements GameActionListener {

    private Game _game;
    private GameFactory _factory;
    private GamefieldView _fieldView;

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
        _game.addGameActionListener(this);

        _fieldView = new GamefieldView(_game.getField());
        setLayout(new BorderLayout());
        add(_fieldView, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

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
