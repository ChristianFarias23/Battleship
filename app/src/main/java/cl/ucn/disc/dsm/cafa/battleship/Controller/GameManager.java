package cl.ucn.disc.dsm.cafa.battleship.Controller;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import cl.ucn.disc.dsm.cafa.battleship.MainActivity;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridAdapter;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridCell;
import cl.ucn.disc.dsm.cafa.battleship.model.CellStatus;
import cl.ucn.disc.dsm.cafa.battleship.model.Ship;
import lombok.Getter;
import lombok.Setter;

import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.DIMENSION;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_BATTLESHIPS;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_CRUISERS;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_SUBMARINES;

public class GameManager {


    // Patron Singleton.
    private static GameManager INSTANCE;

    //Constructor privado.
    private GameManager() {
    }

    public static GameManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameManager();
        }

        return INSTANCE;
    }

    public void setBotGridAdapter(GridAdapter gridAdapter) {
        this.botGridAdapter = gridAdapter;
    }

    public void setPlayerGridAdapter(GridAdapter gridAdapter) {
        this.playerGridAdapter = gridAdapter;
    }

    public void setMessageTextView(TextView messageTextView){
        this.tvMessage = messageTextView;
        setMessage("Posicione sus naves en el tablero.");
    }

    // ....

    private TextView tvMessage;

    private GridAdapter botGridAdapter;

    private GridAdapter playerGridAdapter;


    @Getter
    GameState state = GameState.ARRANGE;


    public void managePlayerGridViewItemClick(GridAdapter adapter, int position) {
        // Ordenar las naves del jugador.
        if (this.state == GameState.ARRANGE) {
            GridCell cell = adapter.getItem(position);

            // TODO: Checkear si se puede poner una nave en esta posicion.

            // Si se puede poner la nave en esta posicion, entonces OK.
            cell.setStatus(CellStatus.USED_BY_PLAYER_1);
            adapter.notifyDataSetChanged();
        }
    }

    private void setMessage(String message){
        this.tvMessage.setText(message);
    }

    private static final int THRESHOLD = DIMENSION * DIMENSION;

    private void arrangeBotGrid() {
        if (botGridAdapter != null) {

            // FIXME: No cubren el caso en que no se puedan poner mas naves. Loop infinito -> Crash.

            int placedBattleships = 0;
            int placedCruisers = 0;
            int placedSubmarines = 0;

            for (int i = 0; i < NUM_BATTLESHIPS; i++) {
                int count = 0;
                while (true) {
                    boolean placed = placeShip(botGridAdapter, Ship.ShipType.BATTLESHIP, Ship.Orientation.VERTICAL, 2);

                    if (placed) {
                        placedBattleships++;
                        break;
                    }

                    if (count >= THRESHOLD) {
                        Log.d("SHIP_PLACEMENT", "Error: No se pudo poner el acorazado. ");
                        break;
                    }

                    count++;
                }
            }

            for (int i = 0; i < NUM_CRUISERS; i++) {
                int count = 0;

                while (true) {
                    boolean placed = placeShip(botGridAdapter, Ship.ShipType.CRUISER, Ship.Orientation.VERTICAL, 2);

                    if (placed) {
                        placedCruisers++;
                        break;
                    }

                    if (count >= THRESHOLD) {
                        Log.d("SHIP_PLACEMENT", "Error: No se pudo poner el crucero. ");
                        break;
                    }

                    count++;
                }
            }

            for (int i = 0; i < NUM_SUBMARINES; i++) {
                int count = 0;

                while (true) {
                    boolean placed = placeShip(botGridAdapter, Ship.ShipType.SUBMARINE, Ship.Orientation.VERTICAL, 2);

                    if (placed) {
                        placedSubmarines++;
                        break;
                    }


                    if (count >= THRESHOLD) {
                        Log.d("SHIP_PLACEMENT", "Error: No se pudo poner el submarino. ");

                        break;
                    }

                    count++;
                }
            }
            if (placedBattleships == NUM_BATTLESHIPS
                    && placedCruisers == NUM_CRUISERS
                    && placedSubmarines == NUM_SUBMARINES) {
                botGridAdapter.notifyDataSetChanged();

                Log.d("SHIP_PLACEMENT", "OK: Se pusieron todas las naves. ");

            } else {
                // Resetear.
                botGridAdapter.setNewEmptyGrid();
                botGridAdapter.notifyDataSetChanged();

                Log.d("SHIP_PLACEMENT", "Error: No se pudieron poner todas las naves. ");
            }
        }
    }

    private boolean placeShip(GridAdapter adapter, Ship.ShipType shipType, Ship.Orientation orientation, int playerNum) {

        int[] randCoords = MainActivity.getCoords((int) (Math.random() * DIMENSION * DIMENSION));

        boolean ok = true;

        // Por cada cantidad de celdas que use esta nave, verificar si esas celdas no estan ocupadas.
        for (int i = 0; i < shipType.getNumCells(); i++) {

            // Obtener la celda en la posicion indicada.
            GridCell cell;

            if (orientation == Ship.Orientation.HORIZONTAL) {
                // Checkear las celdas a la derecha de esta posicion.
                cell = adapter.getItemByCoordinates(randCoords[0] + i, randCoords[1]);

            } else {
                // Checkear las celdas hacia abajo de esta posicion.
                cell = adapter.getItemByCoordinates(randCoords[0], randCoords[1] + i);

            }

            // Si es nula, esta fuera de la matriz, por lo tanto no se puede poner aqui.
            if (cell == null) {
                Log.d("SHIP_PLACEMENT", "Error: Fuera de limites.");
                ok = false;
                break;
            }

            // Si no es nula, pero esta ocupada:
            if (cell.getStatus() == CellStatus.USED_BY_PLAYER_1 || cell.getStatus() == CellStatus.USED_BY_PLAYER_2) {
                Log.d("SHIP_PLACEMENT", "Error: Celda ocupada.");
                ok = false;
                break;
            }
        }

        if (ok) {
            for (int i = 0; i < shipType.getNumCells(); i++) {

                GridCell cell;

                if (orientation == Ship.Orientation.HORIZONTAL) {
                    // Marcar las celdas a la derecha de esta posicion.
                    cell = adapter.getItemByCoordinates(randCoords[0] + i, randCoords[1]);

                } else {
                    // Marcar las celdas hacia abajo de esta posicion.
                    cell = adapter.getItemByCoordinates(randCoords[0], randCoords[1] + i);

                }

                if (playerNum == 1)
                    cell.setStatus(CellStatus.USED_BY_PLAYER_1);
                else
                    cell.setStatus(CellStatus.USED_BY_PLAYER_2);
            }

            return true;
        }

        return false;

    }

    public void manageRivalGridViewItemClick(GridAdapter adapter, int position) {
        // Atacar las naves del rival.
        if (this.state == GameState.BATTLE) {
            GridCell cell = adapter.getItem(position);

            //Checkear si hay o no una nave en esta posicion.

            if (cell.getStatus().equals(CellStatus.USED_BY_PLAYER_2)) {
                cell.setStatus(CellStatus.HIT);
                setMessage("Hit!");
            } else {
                cell.setStatus(CellStatus.MISS);
                setMessage("Miss!");
            }

            adapter.notifyDataSetChanged();
        }
    }

    public void setArrangeState() {
        this.state = GameState.ARRANGE;
    }

    public void setBattleState() {
        // Ordenar las piezas del bot.

        //TODO: Eliminar esto al implementar el modo de 2 jugadores.

        setMessage("El bot esta ordenando sus naves...");

        Log.d("SHIP_PLACEMENT", "El Bot esta ordenando sus naves...");
        arrangeBotGrid();

        setMessage("Batalla iniciada!");
        this.state = GameState.BATTLE;
    }

    public void reset() {
        playerGridAdapter.setNewEmptyGrid();
        playerGridAdapter.notifyDataSetChanged();

        botGridAdapter.setNewEmptyGrid();
        botGridAdapter.notifyDataSetChanged();

        setArrangeState();
    }

    //...

    public enum GameState {
        ARRANGE,
        BATTLE
    }

}


