package cl.ucn.disc.dsm.cafa.battleship.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cl.ucn.disc.dsm.cafa.battleship.enumerations.PlayerType;
import lombok.Getter;

import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_BATTLESHIPS;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_CRUISERS;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_SUBMARINES;

public class Player {

    /**
     * La cantidad actual de submarinos de este jugador.
     */
    @Getter
    int numSubmarines;

    /**
     * La cantidad actual de cruceros de este jugador.
     */
    @Getter
    int numCruisers;

    /**
     * La cantidad actual de acorazados de este jugador.
     */
    @Getter
    int numBattleships;

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

    /**
     * Crea un nuevo jugador.
     * @param type: El tipo de jugador.
     */
    public Player(PlayerType type){
        this.type = type;
        this.numSubmarines = NUM_SUBMARINES;
        this.numCruisers = NUM_CRUISERS;
        this.numBattleships = NUM_BATTLESHIPS;
    }

    /**
     * Restaura los valores por defecto de este jugador.
     */
    public void reset(){
        this.numSubmarines = NUM_SUBMARINES;
        this.numCruisers = NUM_CRUISERS;
        this.numBattleships = NUM_BATTLESHIPS;

        this.ships.clear();
    }

    /**
     * Verifica si el jugador ha puesto todas sus naves.
     * @return
     */
    public boolean isReady(){
        return (this.numSubmarines == 0 && this.numCruisers == 0 && this.numBattleships == 0);
    }

    /**
     * Verifica si el jugador ha perdido.
     * @return
     */
    public boolean hasLost(){

        // Basta con que una nave esta viva para retornar falso.
        for (Ship ship : this.ships){
            if (!ship.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    public void substractSubmarine(){
        if (this.numSubmarines>0)
            this.numSubmarines--;
    }

    public void substractCruiser(){
        if (this.numCruisers>0)
            this.numCruisers--;
    }

    public void substractBattleship(){
        if (this.numBattleships>0)
            this.numBattleships--;
    }
}
