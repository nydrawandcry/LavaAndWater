package View.gamefieldView;

import Model.Game;
import Model.events.game.GameActionListener;

import javax.swing.*;

public class GameFrame extends JFrame implements GameActionListener {

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
