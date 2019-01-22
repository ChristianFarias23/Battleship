package cl.ucn.disc.dsm.cafa.battleship.model;

import java.util.ArrayList;
import java.util.List;

import cl.ucn.disc.dsm.cafa.battleship.adapters.GridCell;
import lombok.Getter;


/**
 * La nave.
 * Por defecto, su pivote sera la esquina superior izquierda.
 */
public class Ship {

    /**
     * El tipo de nave.
     */
    @Getter
    ShipType type = ShipType.SUBMARINE;

    /**
     * La orientacion de la nave.
     */
    @Getter
    Orientation orientation = Orientation.VERTICAL;

    /**
     *
     */
    @Getter
    List<GridCell> cells;

    public Ship(ShipType type, Orientation orientation) {
        this.type = type;
        this.orientation = orientation;
        this.cells = new ArrayList<>(type.getNumCells());
    }

    public enum Orientation{
        VERTICAL,
        HORIZONTAL
    }

    public enum ShipType{
        SUBMARINE (1),
        CRUISER (2),
        BATTLESHIP(3);

        @Getter
        private int numCells;

        private ShipType(int numCells){
            this.numCells = numCells;
        }

    }
}
