package cl.ucn.disc.dsm.cafa.battleship.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.List;

import cl.ucn.disc.dsm.cafa.battleship.R;
import lombok.Getter;

public class GridAdapter extends BaseAdapter {
    private LayoutInflater inflater;

    private List<GridCell> cells;

    /**
     * Constructor del adaptador.
     * @param context
     * @param cells
     */
    public GridAdapter(Context context, List<GridCell> cells){
        this.inflater = LayoutInflater.from(context);
        this.cells = cells;
    }

    @Override
    public int getCount() {
        if (this.cells != null)
            return this.cells.size();
        return 0;
    }

    @Override
    public GridCell getItem(int position) {
        if (this.cells != null)
            return this.cells.get(position);
        return null;    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Obtener el tamanio de las columnas.
        GridView grid = (GridView)parent;
        int size = grid.getColumnWidth();

        // Patron viewholder.
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridcell, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GridCell cell = getItem(position);
        holder.getButton().setBackgroundColor(Color.BLUE);

        // Permite que las celdas sean cuadradas.
        convertView.setLayoutParams(new GridView.LayoutParams(size,size));

        return convertView;
    }

    class ViewHolder{
        @Getter
        final Button button;

        public ViewHolder(final View convertView){
            this.button = convertView.findViewById(R.id.cell_button);
        }
    }

}
