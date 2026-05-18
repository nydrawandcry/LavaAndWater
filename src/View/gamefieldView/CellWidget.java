package View.gamefieldView;

import Model.events.cell.CellActionEvent;
import Model.events.cell.CellActionListener;
import Model.events.liquids.LiquidAppearanceInCellEvent;
import Model.events.liquids.LiquidAppearanceInCellListener;
import Model.gamefield.Cell;
import Model.units.Unit;
import Model.units.liquids.LiquidSystem;
import View.liquidSystemView.LiquidSystemWidget;
import View.liquidSystemView.LiquidSystemWidgetFactory;
import View.unitView.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CellWidget extends JPanel{

    public static int CELL_SIZE = 50;

    private final Cell _cell;

    private JLayeredPane _layeredPane;

    private HashMap<Unit, UnitWidget> _unitWidgets = new HashMap<>();

    private LiquidSystemWidget _liquidWidget;

    private CellActionListener _cellListener = new CellActionHandler();
    private LiquidAppearanceInCellListener _liquidListener = new LiquidLiquidAppearanceInCellHandler();

    public CellWidget(Cell cell){
        _cell = cell;
        _cell.addCellActionListener(_cellListener);
        _cell.addLiquidAppearanceInCellListener(_liquidListener);

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
        add(_layeredPane, BorderLayout.CENTER);
    }

    private void updateLiquidDisplay() {
        LiquidSystem liquid = _cell.getLiquidSystem();

        if(liquid == null) { //если жидкости больше нет в клетке
            if (_liquidWidget != null) {
                _liquidWidget.setVisibleLiquid(false);
            }
            repaint();
            return;
        }

        if(_liquidWidget == null) { //если жидкость есть в клетке но она еще не отрисована
            _liquidWidget = LiquidSystemWidgetFactory.create(liquid);
            _liquidWidget.setBounds(0, 0, CELL_SIZE, CELL_SIZE);

            _layeredPane.add(_liquidWidget, Integer.valueOf(0));
        }

        _liquidWidget.setVisibleLiquid(true);
        repaint();
    }

    public void addUnitWidgets() {
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

        _layeredPane.add(
                widget,
                Integer.valueOf(widget.getRenderPriority())
        );
        _unitWidgets.put(u,widget);

        repaint();
    }

    public void removeUnitWidget(Unit u) {
        UnitWidget widget = _unitWidgets.remove(u);

        if(widget == null) {
            return;
        }
        _layeredPane.remove(widget);

        revalidate();
        repaint();
    }

    void changeColor(Color c) {
        setBackground(c);
        repaint();
    }

    private class CellActionHandler implements CellActionListener {
        @Override
        public void unitPlaced(CellActionEvent e) {
            addUnitWidget(e.getUnit());
        }

        @Override
        public void unitExtracted(CellActionEvent e) {
            removeUnitWidget(e.getUnit());
        }
    }

    private class LiquidLiquidAppearanceInCellHandler implements LiquidAppearanceInCellListener {
        @Override
        public void liquidAdded(LiquidAppearanceInCellEvent e) {
            updateLiquidDisplay();
        }

        @Override
        public void liquidRemoved(LiquidAppearanceInCellEvent e) {
            updateLiquidDisplay();
        }
    }
}
