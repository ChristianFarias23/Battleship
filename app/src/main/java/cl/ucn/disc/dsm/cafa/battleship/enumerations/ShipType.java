package cl.ucn.disc.dsm.cafa.battleship.enumerations;

import android.graphics.Color;

import lombok.Getter;

/**
 * El tipo de nave.
 */
public enum ShipType{
    /**
     * Un Submarino. Usa 1 celda en el tablero.
     */
    SUBMARINE (1, Color.parseColor("#176087")),

    /**
     * Un Crucero. Usa 2 celdas en el tablero.
     */
    CRUISER (2, Color.parseColor("#4B8F8C")),

    /**
     * Un Acorazado. Usa 3 celdas en el tablero.
     */
    BATTLESHIP(3, Color.parseColor("#6C756B"));

    /**
     * La cantidad de celdas que usa este tipo de nave.
     */
    @Getter
    private int numCells;

    /**
     * El color de este tipo de nave.
     */
    @Getter
    private int color;

    private ShipType(int numCells, int color){
        this.numCells = numCells;
        this.color = color;
    }

}
