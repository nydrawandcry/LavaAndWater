package View.gamefieldView;

import Model.events.cell.CellActionEvent;
import Model.events.cell.CellActionListener;
import Model.events.liquids.LiquidAppearanceInCellEvent;
import Model.events.liquids.LiquidAppearanceInCellListener;
import Model.gamefield.Cell;
import Model.units.Unit;
import Model.units.liquids.Lava;
import Model.units.liquids.LiquidSystem;
import Model.units.liquids.Water;
import View.liquidSystemView.LavaWidget;
import View.liquidSystemView.WaterWidget;
import View.unitView.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CellWidget extends JPanel implements CellActionListener, LiquidAppearanceInCellListener {

    public static int CELL_SIZE = 50;

    private final Cell _cell;

    private JLayeredPane _layeredPane;

    private JPanel _unitLayer;

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
        initializeLayers();
        //также тут нужна инициализация жидкостями отдельно потому что это не юниты
        updateLiquidDisplay();
        addUnitWidgets(); //пускай будет тут

    }

    private void initializeLayers() {
        //первоначальная инициализация pane (порождение и установка размера)
        _layeredPane = new JLayeredPane();
        _layeredPane.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        _layeredPane.setSize(CELL_SIZE, CELL_SIZE);
        _layeredPane.setLayout(null);
        _layeredPane.setBounds(0,0, CELL_SIZE, CELL_SIZE);

        _lavaWidget = new LavaWidget();
        _lavaWidget.setBounds(0,0,CELL_SIZE,CELL_SIZE);

        _waterWidget = new WaterWidget();
        _waterWidget.setBounds(0,0,CELL_SIZE,CELL_SIZE); //я б еще это оптимизировала, это че каждую жидкость слоем добавлять

        _unitLayer = new JPanel();
        _unitLayer.setLayout(null);
        _unitLayer.setOpaque(false);
        _unitLayer.setBounds(0, 0, CELL_SIZE, CELL_SIZE);

        _layeredPane.add(_lavaWidget, JLayeredPane.DEFAULT_LAYER);
        _layeredPane.add(_waterWidget, JLayeredPane.DEFAULT_LAYER);
        _layeredPane.add(_unitLayer, JLayeredPane.DRAG_LAYER);

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
        _unitLayer.removeAll();
        _unitWidgets.clear();

        for (Unit u : _cell.getUnits()) {
            addUnitWidget(u);
        }

        revalidate();
        repaint();
    }

    public void addUnitWidget(Unit u) {
        if(_unitWidgets.containsKey(u)){
            return;
        }

        UnitWidget widget = UnitWidgetFactory.create(u);

        if(widget == null) {
            return;
        }

        widget.setBounds(2,2,CELL_SIZE - 4, CELL_SIZE - 4);

        _unitLayer.add(widget);
        _unitWidgets.put(u,widget);

        repaint();
    }

    public void removeUnitWidget(Unit u) {
        UnitWidget widget = _unitWidgets.remove(u);

        if(widget == null) {
            return;
        }

        _unitLayer.remove(widget);

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
