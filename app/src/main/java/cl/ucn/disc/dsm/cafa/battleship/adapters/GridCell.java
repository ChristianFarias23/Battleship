package cl.ucn.disc.dsm.cafa.battleship.adapters;

import cl.ucn.disc.dsm.cafa.battleship.model.CellStatus;

public class GridCell {

    private CellStatus status;

    public GridCell(){
        this.status = CellStatus.UNKNOWN;
    }

    public GridCell(CellStatus status){
        this.status = status;
    }
}
