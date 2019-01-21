package cl.ucn.disc.dsm.cafa.battleship.adapters;

import android.graphics.Color;

import cl.ucn.disc.dsm.cafa.battleship.model.CellStatus;
import lombok.Getter;

public class GridCell {

    @Getter
    private int color = Color.BLUE;

    @Getter
    private CellStatus status;

    public GridCell(){
        this.status = CellStatus.UNKNOWN;
    }

    public GridCell(CellStatus status){
        this.status = status;
    }

}
