package Model.events.game;

import java.util.EventListener;

public interface GameActionListener extends EventListener {

    void gameIsOver(); //сделаю пока событие об успешно пройденном уровне и все (подразумеваю, что при смерти игрока поражения не вылезает, просто блокируются действия)
}
