package View.gamefieldView;

import Model.gamefield.Cell;

import javax.swing.*;
import java.awt.*;

public class CellWidget extends JPanel {

    public static int CELL_SIZE = 50;

    private final Cell _cell;

    private JLayeredPane _layeredPane;

    private JPanel _playerLayer;
    private JPanel _wallLayer;
    private JPanel _exitLayer;
    private JPanel _ironBlockLayer;

    private JPanel _liquidLayer; //todo затравка на жидкости (попозже сделаю их)

    public CellWidget(Cell cell){
        _cell = cell;
        //подписка на события

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        setOpaque(true);

        //инициализация юнитами
        initializeUnitLayers();
    }

    private void initializeUnitLayers() {
        //первоначальная инициализация pane (порождение и установка размера)
        _layeredPane = new JLayeredPane();
        _layeredPane.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));

        _playerLayer = new JPanel();
        _playerLayer.setLayout(null); //по идее игрок у нас просто в клетке стоит?(я пока не разобралась с лэйаутами, попозже разберусь и настрою нормально)
        _playerLayer.setOpaque(false); //че это я тоже не ебу
        _playerLayer.setBounds(2,2, CELL_SIZE, CELL_SIZE);

        _wallLayer = new JPanel();
        _wallLayer.setLayout(null);
        _wallLayer.setOpaque(false);
        _wallLayer.setBounds(2,2, CELL_SIZE, CELL_SIZE);

        _ironBlockLayer = new JPanel();
        _ironBlockLayer.setLayout(null);
        _ironBlockLayer.setOpaque(false);
        _ironBlockLayer.setBounds(2,2, CELL_SIZE, CELL_SIZE);

        _exitLayer = new JPanel();
        _exitLayer.setLayout(null);
        _exitLayer.setOpaque(false);
        _exitLayer.setBounds(2,2, CELL_SIZE, CELL_SIZE);

        _layeredPane.add(_playerLayer, JLayeredPane.DEFAULT_LAYER);
        _layeredPane.add(_wallLayer, JLayeredPane.DEFAULT_LAYER);
        _layeredPane.add(_ironBlockLayer, JLayeredPane.DEFAULT_LAYER);
        _layeredPane.add(_exitLayer, JLayeredPane.DEFAULT_LAYER); //todo я пересмотрю такую архитектуру, дублирование кода.надо переделать

        add(_layeredPane, BorderLayout.CENTER);
    }

    void changeColor(Color c) {
        setBackground(c);
        repaint();
    }
}
