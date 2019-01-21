package cl.ucn.disc.dsm.cafa.battleship.model;

/**
 * El estado de una celda en la matriz.
 */
public enum CellStatus {
    /**
     * En esta celda se encontraba una nave o parte de ella.
     */
    HIT,

    /**
     * En esta celda no habia nada.
     */
    MISS,

    /**
     * No se sabe aun que hay en esta celda.
     */
    UNKNOWN
}
