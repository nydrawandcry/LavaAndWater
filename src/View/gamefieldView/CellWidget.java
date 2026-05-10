package View.gamefieldView;

import Model.gamefield.Cell;
import Model.units.Exit;
import Model.units.Unit;
import Model.units.moving.IronBlock;
import Model.units.moving.Player;
import Model.units.solid.Wall;
import View.unitView.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CellWidget extends JPanel {

    public static int CELL_SIZE = 50;

    private final Cell _cell;

    private JLayeredPane _layeredPane;

    private JPanel _playerLayer;
    private JPanel _wallLayer;
    private JPanel _exitLayer;
    private JPanel _ironBlockLayer;

    private HashMap<Unit, UnitWidget> _unitWidgets = new HashMap<>();

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

    public void addUnitWidgets() {
        _playerLayer.removeAll();
        _wallLayer.removeAll();
        _ironBlockLayer.removeAll();
        _exitLayer.removeAll();
        _unitWidgets.clear();

        //todo пример установки стен в клетки (я не хочу делать цикл под каждый тип юнита, это же пиздец не масштабируемо)
        for(Unit u : _cell.getUnits(Wall.class)) { //todo и че такие циклы ставить на каждый юнит если их много по полю? надо унифицировать
            Wall w = (Wall) u;
            WallWidget widget = new WallWidget(w);
            widget.setBounds(1,1, CELL_SIZE,CELL_SIZE);
            _wallLayer.add(widget);
            _unitWidgets.put(w, widget);
        }

        //тут должны быть еще циклы на добавление остальных юнитов но мне впадлу
        //также надо учесть, что не все юниты могут быть на одной клетке разом.
        //например, на одной клетке из юнитов могут располагаться только player+exit. остальные не могут.
        //если Unit у нас имплементирует интерфейс solid, то он автоматом не может вместе с другими юнитами на клетке находиться.
        //я хочу унифицировать добавление юнитов + проводить валидацию объектов. однако ответственна ли UI за эту валидацию? все уже решено в модели?
        //разберусь

        revalidate();
        repaint();
    }

    public void addUnitWidget(Unit u) {
        if(_unitWidgets.containsKey(u)){
            return;
        }

        UnitWidget w = null;

        if(u instanceof Player player) {
            PlayerWidget playerWidget = new PlayerWidget(player);
            //тут надо подписку на события для контроля плеера клавой (чтоб пользователь управлял короче)
            playerWidget.setBounds(1,1, CELL_SIZE, CELL_SIZE);
            w = playerWidget;

            _playerLayer.add(w);
        }

        if(u instanceof Wall wall) {
            WallWidget wallWidget = new WallWidget(wall);
            wallWidget.setBounds(1,1, CELL_SIZE, CELL_SIZE);

            w = wallWidget;
            _wallLayer.add(w);
        }

        if(u instanceof IronBlock ironBlock) {
            IronBlockWidget ironBlockWidget = new IronBlockWidget(ironBlock);
            ironBlockWidget.setBounds(1,1, CELL_SIZE, CELL_SIZE);

            w = ironBlockWidget;
            _ironBlockLayer.add(w);
        }

        if(u instanceof Exit exit) { //это пиздец
            ExitWidget exitWidget = new ExitWidget(exit);
            exitWidget.setBounds(1,1,CELL_SIZE, CELL_SIZE);

            w = exitWidget;
            _exitLayer.add(w);
        }

        if(w != null) {
            _unitWidgets.put(u, w);
            revalidate();
            repaint();
        }
    }

    public void removeUnitWidget(Unit u) {
        UnitWidget widget = _unitWidgets.remove(u);

        if(widget == null) {
            return;
        }

        _playerLayer.remove(widget);
        _wallLayer.remove(widget);
        _ironBlockLayer.remove(widget);
        _exitLayer.remove(widget);

        revalidate();
        repaint();
    }

    void changeColor(Color c) {
        setBackground(c);
        repaint();
    }
}
