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

    /**
     * La orientacion de la nave.
     */
    public enum Orientation{
        /**
         * Usa celdas en las mismas coordenadas X.
         */
        VERTICAL,

        /**
         * Usa celdas en las mismas coordenadas Y.
         */
        HORIZONTAL
    }

    /**
     * El tipo de nave.
     */
    public enum ShipType{
        /**
         * Un Submarino. Usa 1 celda en el tablero.
         */
        SUBMARINE (1),

        /**
         * Un Crucero. Usa 2 celdas en el tablero.
         */
        CRUISER (2),

        /**
         * Un Acorazado. Usa 3 celdas en el tablero.
         */
        BATTLESHIP(3);


        /**
         * La cantidad de celdas que usa este tipo de nave.
         */
        @Getter
        private int numCells;

        private ShipType(int numCells){
            this.numCells = numCells;
        }

    }
}
