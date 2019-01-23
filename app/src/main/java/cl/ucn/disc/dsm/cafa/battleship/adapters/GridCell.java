package cl.ucn.disc.dsm.cafa.battleship.adapters;

import cl.ucn.disc.dsm.cafa.battleship.enumerations.CellStatus;
import lombok.Getter;
import lombok.Setter;

public class GridCell {

    // TODO: Agregar atributo HIDDEN. Determina si se muestra o no en el tablero.

    @Setter
    @Getter
    private int colorWithShip;

    @Setter
    @Getter
    private CellStatus status;

    @Getter
    private final int xCoord;

    @Getter
    private final int yCoord;

    public GridCell(int x, int y){
        this.status = CellStatus.EMPTY;
        this.xCoord = x;
        this.yCoord = y;
    }

}
