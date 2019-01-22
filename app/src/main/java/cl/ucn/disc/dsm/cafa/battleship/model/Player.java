package cl.ucn.disc.dsm.cafa.battleship.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Player {

    /**
     * Tipo de jugador.
     */
    @Getter
    PlayerType type = PlayerType.HUMAN;

    /**
     * Las naves del jugador.
     */
    @Getter
    List<Ship> ships = new ArrayList<>();

    public Player(PlayerType type){
        this.type = type;
    }


    public enum PlayerType{
        HUMAN,
        BOT
    }
}
