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

    @Getter
    int numSubmarines;

    @Getter
    int numCruisers;

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

    public Player(PlayerType type){
        this.type = type;
        this.numSubmarines = NUM_SUBMARINES;
        this.numCruisers = NUM_CRUISERS;
        this.numBattleships = NUM_BATTLESHIPS;
    }

    public void reset(){
        this.numSubmarines = NUM_SUBMARINES;
        this.numCruisers = NUM_CRUISERS;
        this.numBattleships = NUM_BATTLESHIPS;

        this.ships.clear();
    }

    public boolean isReady(){
        return (this.numSubmarines == 0 && this.numCruisers == 0 && this.numBattleships == 0);
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

    public boolean hasLost(){
        /*
        int destroyedShips = 0;

        for (Ship ship : this.ships){
            if (ship.isDestroyed()) {
                destroyedShips++;
            }
        }

        if (destroyedShips == NUM_BATTLESHIPS + NUM_CRUISERS + NUM_SUBMARINES){
            return true;
        }

        return false;
        */

        for (Ship ship : this.ships){
            if (ship.isDestroyed()) {
                Log.d("--->>> --->>>", "Nave destruida ");
            } else {
                Log.d("--->>> --->>>", "Nave viva ");
            }
        }

        for (Ship ship : this.ships){
            if (!ship.isDestroyed()) {
                return false;
            }
        }
        return true;
    }
}
