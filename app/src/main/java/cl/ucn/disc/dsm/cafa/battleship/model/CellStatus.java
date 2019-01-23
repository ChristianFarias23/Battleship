package cl.ucn.disc.dsm.cafa.battleship.model;

import android.graphics.Color;

import cl.ucn.disc.dsm.cafa.battleship.MainActivity;
import cl.ucn.disc.dsm.cafa.battleship.R;
import lombok.Getter;

/**
 * El estado de una celda en la matriz.
 */

public enum CellStatus {

    /**
     * En esta celda se encontraba una nave o parte de ella.
     */
    HIT(Color.parseColor("#D9534F")),

    /**
     * En esta celda no habia nada.
     */
    MISS(Color.parseColor("#F0AD4E")),

    /**
     * Celda vacia.
     */
    EMPTY(Color.parseColor("#0A2239")),

    /**
     * Celda ocupada por una nave del jugador 1.
     */
    USED_BY_PLAYER_1(Color.CYAN),

    /**
     * Celda ocupada por una nave del jugador 2 o por el BOT.
     */
    USED_BY_PLAYER_2(Color.parseColor("#0A2239"));

    @Getter
    private int color;

    private CellStatus(int color){
        this.color = color;
    }


}
