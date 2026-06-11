package Model.events.cell;

import java.util.EventListener;

public interface CellActionListener extends EventListener {

    void unitPlaced(CellActionEvent e);

    void unitExtracted(CellActionEvent e);
}
