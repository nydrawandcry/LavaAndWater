package View.gamefieldView;

import Model.events.cell.CellActionEvent;
import Model.events.cell.CellActionListener;
import Model.events.liquids.LiquidAppearanceInCellEvent;
import Model.events.liquids.LiquidAppearanceInCellListener;
import Model.gamefield.Cell;
import Model.units.Exit;
import Model.units.Unit;
import Model.units.liquids.Lava;
import Model.units.liquids.LiquidSystem;
import Model.units.liquids.Water;
import Model.units.moving.IronBlock;
import Model.units.moving.Player;
import Model.units.solid.Wall;
import View.liquidSystemView.LavaWidget;
import View.liquidSystemView.LiquidSystemWidget;
import View.liquidSystemView.WaterWidget;
import View.unitView.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CellWidget extends JPanel implements CellActionListener, LiquidAppearanceInCellListener {

    public static int CELL_SIZE = 50;

    private final Cell _cell;

    private JLayeredPane _layeredPane;

    private JPanel _playerLayer;
    private JPanel _wallLayer;
    private JPanel _exitLayer;
    private JPanel _ironBlockLayer;

    private HashMap<Unit, UnitWidget> _unitWidgets = new HashMap<>();

    private LavaWidget _lavaWidget;
    private WaterWidget _waterWidget;

    public CellWidget(Cell cell){
        _cell = cell;
        _cell.addCellActionListener(this);
        _cell.addLiquidAppearanceInCellListener(this);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        setOpaque(true);
        setBackground(new Color(191,191,191));

        //инициализация юнитами
        initializeUnitLayers();
        //также тут нужна инициализация жидкостями отдельно потому что это не юниты
        updateLiquidDisplay();
        addUnitWidgets(); //пускай будет тут

    }

    private void initializeUnitLayers() {
        //первоначальная инициализация pane (порождение и установка размера)
        _layeredPane = new JLayeredPane();
        _layeredPane.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        _layeredPane.setSize(CELL_SIZE, CELL_SIZE);
        _layeredPane.setLayout(null);
        _layeredPane.setBounds(0,0, CELL_SIZE, CELL_SIZE);

        _lavaWidget = new LavaWidget();
        _lavaWidget.setBounds(0,0,CELL_SIZE,CELL_SIZE);

        _waterWidget = new WaterWidget();
        _waterWidget.setBounds(0,0,CELL_SIZE,CELL_SIZE);

        _exitLayer = new JPanel();
        _exitLayer.setLayout(null);
        _exitLayer.setOpaque(false);
        _exitLayer.setBounds(0,0, CELL_SIZE, CELL_SIZE);

        _wallLayer = new JPanel();
        _wallLayer.setLayout(null);
        _wallLayer.setOpaque(false);
        _wallLayer.setBounds(0,0, CELL_SIZE, CELL_SIZE);

        _ironBlockLayer = new JPanel();
        _ironBlockLayer.setLayout(null);
        _ironBlockLayer.setOpaque(false);
        _ironBlockLayer.setBounds(0,0, CELL_SIZE, CELL_SIZE);

        _playerLayer = new JPanel();
        _playerLayer.setLayout(null);
        _playerLayer.setOpaque(false);
        _playerLayer.setBounds(0,0, CELL_SIZE, CELL_SIZE);

        _layeredPane.add(_lavaWidget, JLayeredPane.DEFAULT_LAYER);
        _layeredPane.add(_waterWidget, JLayeredPane.DEFAULT_LAYER);
        _layeredPane.add(_exitLayer, JLayeredPane.DEFAULT_LAYER + 50);
        _layeredPane.add(_wallLayer, JLayeredPane.DEFAULT_LAYER + 100);
        _layeredPane.add(_ironBlockLayer, JLayeredPane.DEFAULT_LAYER + 150);
        _layeredPane.add(_playerLayer, JLayeredPane.DRAG_LAYER);

        add(_layeredPane, BorderLayout.CENTER);
    }

    private void updateLiquidDisplay() {
        _lavaWidget.setVisibleLiquid(false);
        _waterWidget.setVisibleLiquid(false);

        LiquidSystem liquid = _cell.getLiquidSystem();

        if (liquid instanceof Lava) {
            _lavaWidget.setVisibleLiquid(true);
        } else if (liquid instanceof Water) {
            _waterWidget.setVisibleLiquid(true);
        }
    }

    public void addUnitWidgets() {
        _playerLayer.removeAll();
        _wallLayer.removeAll();
        _ironBlockLayer.removeAll();
        _exitLayer.removeAll();
        _unitWidgets.clear();

        for (Unit u : _cell.getUnits(Wall.class)) {
            addUnitWidget(u);
        }
        for (Unit u : _cell.getUnits(IronBlock.class)) {
            addUnitWidget(u);
        }
        for (Unit u : _cell.getUnits(Exit.class)) {
            addUnitWidget(u);
        }
        for (Unit u : _cell.getUnits(Player.class)) {
            addUnitWidget(u);
        }
        revalidate();
        repaint();
    }

    public void addUnitWidget(Unit u) {
        if(_unitWidgets.containsKey(u)){
            return;
        }

        UnitWidget widget = null;
        JPanel targetLayer = null;

        if (u instanceof Player player) {
            PlayerWidget pw = new PlayerWidget(player, new Color(242, 209, 180));
            widget = pw;
            targetLayer = _playerLayer;
        } else if (u instanceof Wall wall) {
            WallWidget ww = new WallWidget(wall, new Color(76, 76, 76));
            widget = ww;
            targetLayer = _wallLayer;
        } else if (u instanceof IronBlock ironBlock) {
            IronBlockWidget ibw = new IronBlockWidget(ironBlock, new Color(150, 150, 150));
            widget = ibw;
            targetLayer = _ironBlockLayer;
        } else if (u instanceof Exit exit) {
            ExitWidget ew = new ExitWidget(exit, new Color(185, 128, 229));
            widget = ew;
            targetLayer = _exitLayer;
        }

        if (widget != null && targetLayer != null) {
            widget.setBounds(2, 2, CELL_SIZE - 4, CELL_SIZE - 4);
            targetLayer.add(widget);
            _unitWidgets.put(u, widget);
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

    @Override
    public void unitPlaced(CellActionEvent e) {
        addUnitWidget(e.getUnit());
    }

    @Override
    public void unitExtracted(CellActionEvent e) {
        removeUnitWidget(e.getUnit());
    }

    @Override
    public void liquidAdded(LiquidAppearanceInCellEvent e) {
        updateLiquidDisplay();
    }

    @Override
    public void liquidRemoved(LiquidAppearanceInCellEvent e) {
        updateLiquidDisplay();
    }

}
