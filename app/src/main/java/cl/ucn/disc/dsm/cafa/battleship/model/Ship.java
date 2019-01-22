package cl.ucn.disc.dsm.cafa.battleship.model;

import lombok.Getter;


/**
 * La nave.
 * Por defecto, su pivote sera la esquina superior izquierda.
 */
public class Ship {

    /**
     * El tipo de nave.
     */
    ShipType type = ShipType.SUBMARINE;

    /**
     * La orientacion de la nave.
     */
    Orientation orientation = Orientation.VERTICAL;

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
