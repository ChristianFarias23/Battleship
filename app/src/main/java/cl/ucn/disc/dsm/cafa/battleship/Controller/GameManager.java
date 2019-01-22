package cl.ucn.disc.dsm.cafa.battleship.Controller;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import cl.ucn.disc.dsm.cafa.battleship.MainActivity;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridAdapter;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridCell;
import cl.ucn.disc.dsm.cafa.battleship.model.CellStatus;
import cl.ucn.disc.dsm.cafa.battleship.model.Player;
import cl.ucn.disc.dsm.cafa.battleship.model.Ship;
import lombok.Getter;
import lombok.Setter;

import static cl.ucn.disc.dsm.cafa.battleship.Controller.ArrangementValidator.placeBotShips;
import static cl.ucn.disc.dsm.cafa.battleship.Controller.ArrangementValidator.positionToCoordinates;
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

    private Player player1 = new Player(Player.PlayerType.HUMAN);
    private Player player2 = new Player(Player.PlayerType.BOT);


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

    public void manageRivalGridViewItemClick(GridAdapter adapter, int position) {
        // Atacar las naves del rival.
        if (this.state == GameState.BATTLE) {
            GridCell cell = adapter.getItem(position);

            //Checkear si hay o no una nave en esta posicion.

            if (cell.getStatus().equals(CellStatus.USED_BY_PLAYER_2)) {
                // Hit.
                cell.setStatus(CellStatus.HIT);
                setMessage("Hit!");
            } else if (cell.getStatus().equals(CellStatus.HIT) || cell.getStatus().equals(CellStatus.MISS) ){
                // Posicion ya atacada.
                setMessage("...");
            } else {
                // Miss.
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
        placeBotShips(botGridAdapter, player2);

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


