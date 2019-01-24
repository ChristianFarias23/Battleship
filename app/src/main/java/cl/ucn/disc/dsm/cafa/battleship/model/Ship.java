package cl.ucn.disc.dsm.cafa.battleship.model;

import java.util.ArrayList;
import java.util.List;

import cl.ucn.disc.dsm.cafa.battleship.adapters.GridCell;
import cl.ucn.disc.dsm.cafa.battleship.enumerations.CellStatus;
import cl.ucn.disc.dsm.cafa.battleship.enumerations.ShipOrientation;
import cl.ucn.disc.dsm.cafa.battleship.enumerations.ShipType;
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
    ShipOrientation shipOrientation = ShipOrientation.VERTICAL;

    /**
     *
     */
    @Getter
    List<GridCell> cells;

    public Ship(ShipType type, ShipOrientation shipOrientation) {
        this.type = type;
        this.shipOrientation = shipOrientation;
        this.cells = new ArrayList<>(type.getNumCells());
    }

    public boolean isDestroyed(){
        int damage = 0;
        for (GridCell cell: this.cells){
            if (cell.getStatus() == CellStatus.HIT){
                damage++;
            }
        }

        return damage == this.type.getNumCells();
    }

    public int getHP(){
        int hp = this.type.getNumCells();
        for (GridCell cell: this.cells){
            if (cell.getStatus() == CellStatus.HIT){
                hp--;
            }
        }
        return hp;
    }



}
