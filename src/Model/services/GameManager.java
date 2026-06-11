package Model.services;

import Model.Game;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import Model.units.interactive.Player;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;
import Model.units.solid.Wall;

public abstract class GameManager {
    private Gamefield _field;
    private Game _game;
    private CollisionDetector _detector;

    GameManager(Gamefield field, Game game) {
        if(game == null) {
            throw new IllegalStateException("Игра не может быть null");
        }
        if(field == null) {
            throw new IllegalStateException("Поле не может быть null");
        }
        _field = field;
        _game = game;
        _detector = new CollisionDetector();
    }

    public Gamefield getGamefield() {
        return _field;
    }

    public Game getGame() {
        return _game;
    }

    public CollisionDetector getDetector() {
        return _detector;
    }

    public void start() {
        if(_field.isDestroyed()) {
            return;
        }

        placeBoundaryWalls(_field);
        placeInnerWalls(_field);

        placeIronBlocks(_field);
        Player player = placePlayer(_field, 2, 8);
        Exit exit = placeExit(_field, 14, 3);
        placeExitScores(_field, exit);
        placeBoats(_field);

        Lava lava = new Lava();
        Water water = new Water();

        player.addModelPlayerMovementListener(lava.getPlayerMovementListener());
        player.addModelPlayerMovementListener(water.getPlayerMovementListener());

        placeLavaSources(_field, lava);
        placeWaterSources(_field, water);
    }

    private void placeBoundaryWalls(Gamefield field) {
        int width = _field.getWidth();
        int height = _field.getHeight();

        for (int x = 0; x < width; x++) {
            field.getCell(x, 0).putUnit(new Wall());
            field.getCell(x, height - 1).putUnit(new Wall());
        }

        for (int y = 1; y < height - 1; y++) {
            field.getCell(0, y).putUnit(new Wall());
            field.getCell(width - 1, y).putUnit(new Wall());
        }
    }

    protected abstract void placeInnerWalls(Gamefield field);
    protected abstract void placeIronBlocks(Gamefield field);
    protected abstract Player placePlayer(Gamefield field, int x, int y);
    protected abstract Exit placeExit(Gamefield field, int x, int y);
    protected abstract void placeLavaSources(Gamefield field, Lava lava);
    protected abstract void placeWaterSources(Gamefield field, Water water);
    protected abstract void placeExitScores(Gamefield field, Exit exit);
    protected abstract void placeBoats(Gamefield field);
}
