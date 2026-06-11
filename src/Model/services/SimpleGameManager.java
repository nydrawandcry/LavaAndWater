package Model.services;

import Model.Game;
import Model.gamefield.Gamefield;
import Model.units.Exit;
import Model.units.interactive.Player;
import Model.units.liquids.Lava;
import Model.units.liquids.Water;

public class SimpleGameManager extends GameManager{

    public SimpleGameManager(Gamefield field, Game game) {
        super(field, game);
    }

    @Override
    protected void placeInnerWalls(Gamefield field) {

    }

    @Override
    protected void placeIronBlocks(Gamefield field) {

    }

    @Override
    protected Player placePlayer(Gamefield field, int x, int y) {
        return null;
    }

    @Override
    protected Exit placeExit(Gamefield field, int x, int y) {
        return null;
    }

    @Override
    protected void placeLavaSources(Gamefield field, Lava lava) {

    }

    @Override
    protected void placeWaterSources(Gamefield field, Water water) {

    }

    @Override
    protected void placeExitScores(Gamefield field, Exit exit) {

    }

    @Override
    protected void placeBoats(Gamefield field) {

    }
}
