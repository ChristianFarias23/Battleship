package cl.ucn.disc.dsm.cafa.battleship.model;

import android.graphics.Color;

import lombok.Getter;

/**
 * El estado de una celda en la matriz.
 */
public enum CellStatus {
    /**
     * En esta celda se encontraba una nave o parte de ella.
     */
    HIT(Color.RED),

    /**
     * En esta celda no habia nada.
     */
    MISS(Color.YELLOW),


    /**
     * Celda vacia.
     */
    EMPTY(Color.BLUE),

    /**
     * Celda ocupada por una nave del jugador 1.
     */
    USED_BY_PLAYER_1(Color.CYAN),

    /**
     * Celda ocupada por una nave del jugador 2 o por el BOT.
     * MAGENTA SOLO PARA DESARROLLO. AL PUBLICAR USAR Color.BLUE.
     */
    USED_BY_PLAYER_2(Color.MAGENTA);

    @Getter
    private int color;

    private CellStatus(int color){
        this.color = color;
    }


}
