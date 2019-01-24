package cl.ucn.disc.dsm.cafa.battleship.Controller;

import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

import cl.ucn.disc.dsm.cafa.battleship.adapters.GridAdapter;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridCell;
import cl.ucn.disc.dsm.cafa.battleship.enumerations.*;
import cl.ucn.disc.dsm.cafa.battleship.model.Player;
import cl.ucn.disc.dsm.cafa.battleship.model.Ship;
import lombok.Getter;
import lombok.Setter;

import static cl.ucn.disc.dsm.cafa.battleship.Controller.ArrangementValidation.*;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.DEFAULT_HP;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_BATTLESHIPS;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_CRUISERS;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_SUBMARINES;

public class GameManager {


    // Patron Singleton.
    private static GameManager INSTANCE = new GameManager();

    //Constructor privado.
    private GameManager() {
    }

    public static GameManager getInstance() {
        return INSTANCE;
    }

    @Setter
    private TextView tvMessage;

    @Setter
    private TextView tvPlayerHP;

    @Setter
    private TextView tvEnemyHP;

    @Setter
    private Button bSubmarine, bCruiser, bBattleship;

    @Setter
    private GridAdapter botGridAdapter;

    @Setter
    private GridAdapter playerGridAdapter;

    /**
     * El estado del juego.
     */
    @Getter
    private GameState state = GameState.ARRANGE;

    /**
     * El turno actual.
     */
    @Getter
    private GameTurn turn = GameTurn.PLAYER_1;

    /**
     * El tipo de nave a poner.
     */
    @Getter
    private ShipType arrangeType = ShipType.SUBMARINE;

    /**
     * La orientacion de la nave a poner.
     */
    @Getter
    private ShipOrientation arrangeShipOrientation = ShipOrientation.VERTICAL;

    /**
     * El primer jugador; Tu.
     */
    private Player player1 = new Player(PlayerType.HUMAN);

    /**
     * El segundo jugador; el Enemigo.
     */
    private Player player2 = new Player(PlayerType.BOT);

    /**
     * Cambia el tipo de nave a poner.
     *
     * @param arrangeType: El tipo de nave.
     */
    public void setArrangeType(ShipType arrangeType) {
        this.arrangeType = arrangeType;
        int numCell = this.arrangeType.getNumCells();
        setMessage("Esta nave utiliza " + numCell + (numCell == 1 ? " celda." : " celdas."));
    }

    /**
     * Cambia la orientacion de la nave a poner.
     *
     * @param arrangeShipOrientation: La orientacion.
     */
    public void setArrangeShipOrientation(ShipOrientation arrangeShipOrientation) {
        this.arrangeShipOrientation = arrangeShipOrientation;
    }


    /**
     * Delega el toque de una celda del tablero del jugador 1.
     *
     * @param position: La posicion de la celda que se toco.
     */
    public void managePlayerGridViewItemClick(int position) {
        if (this.state == GameState.ARRANGE) {

            // Crea una nueva nave con la configuracion actual.
            Ship ship = new Ship(this.arrangeType, this.arrangeShipOrientation);

            // Intenta poner la nave.
            placePlayer1Ship(position, ship);
        }
    }

    /**
     * Se encarga de poner una nave en el tablero del jugador 1 y actualizar la vista.
     *
     * @param position: La posicion donde se intentara poner la nave.
     * @param ship:     La nave.
     */
    private void placePlayer1Ship(int position, Ship ship) {

        // Verifica que se puedan poner mas naves.
        if (arrangeType == ShipType.SUBMARINE && player1.getNumSubmarines() <= 0) {
            setMessage("Limite de submarinos alcanzado!");
            return;
        }

        if (arrangeType == ShipType.CRUISER && player1.getNumCruisers() <= 0) {
            setMessage("Limite de cruceros alcanzado!");
            return;
        }

        if (arrangeType == ShipType.BATTLESHIP && player1.getNumBattleships() <= 0) {
            setMessage("Limite de acorazados alcanzado!");
            return;
        }

        // Si se pudo poner la nave...
        if (placeShip(playerGridAdapter, player1, position, ship)) {

            // Agregarla a la lista de naves del jugador 1.
            player1.getShips().add(ship);

            // "Pinta" las celdas de la nave.
            setShipGridCellsColor(playerGridAdapter, ship);

            // Actualiza el texto del boton de la nave que se puso.
            switch (arrangeType) {
                case SUBMARINE:
                    player1.substractSubmarine();
                    bSubmarine.setText("Submarino (" + player1.getNumSubmarines() + ")");
                    break;
                case CRUISER:
                    player1.substractCruiser();
                    bCruiser.setText("Crucero (" + player1.getNumCruisers() + ")");
                    break;
                case BATTLESHIP:
                    player1.substractBattleship();
                    bBattleship.setText("Acorazado (" + player1.getNumBattleships() + ")");
                    break;
            }

            setMessage("Nave posicionada correctamente en " + Arrays.toString(positionToCoordinates(position)) + ".");
        } else {
            setMessage("No se puede poner la nave aqui, posicion invalida! " + Arrays.toString(positionToCoordinates(position)));
        }
    }

    /**
     * Escribe un mensaje para notificar al jugador.
     *
     * @param message: El mensaje.
     */
    private void setMessage(String message) {
        this.tvMessage.setText(message);
    }

    /**
     * Delega el toque de una celda del tablero del jugador 2.
     *
     * @param position: La posicion de la celda que se toco.
     */
    public void manageRivalGridViewItemClick(int position) {

        // Si el juego ya ha iniciado...
        if (this.state == GameState.BATTLE) {

            // Turno del jugador 1.

            if (this.turn == GameTurn.PLAYER_1) {
                GridCell cell = botGridAdapter.getItem(position);

                //Checkear si hay o no una nave en esta posicion.
                if (cell.getStatus().equals(CellStatus.USED_BY_PLAYER_2)) {
                    // Hit.
                    cell.setStatus(CellStatus.HIT);
                    setMessage("Hit!");
                } else if (cell.getStatus().equals(CellStatus.HIT) || cell.getStatus().equals(CellStatus.MISS)) {
                    // Posicion ya atacada. Volver.
                    setMessage("...");
                    return;
                } else {
                    // Empty -> Miss.
                    cell.setStatus(CellStatus.MISS);
                    setMessage("Miss!");
                }

                // Notificar al adaptador.
                botGridAdapter.notifyDataSetChanged();

                // Cambiar de turno.
                this.turn = GameTurn.PLAYER_2;
            }

            // Verificar si el jugador 2 perdio.

            if (player2.hasLost()) {
                setMessage("Has ganado!");
                this.turn = GameTurn.END;
            }

            // Actualizar vista de hp del jugador 2.
            tvEnemyHP.setText("HP: " + player2.getHP());

            // Turno del jugador 2.

            if (this.turn == GameTurn.PLAYER_2) {

                // Atacar al jugador.
                randomAttack(playerGridAdapter);

                // Cambiar de turno.
                this.turn = GameTurn.PLAYER_1;
            }

            // Verificar si el jugador 1 perdio.

            if (player1.hasLost()) {
                setMessage("Has perdido!");
                this.turn = GameTurn.END;
            }

            // Actualizar vista de hp del jugador 1.
            tvPlayerHP.setText("HP: " + player1.getHP());

        }

    }

    /**
     * Verifica si el jugador 1 ha puesto todas sus naves. Muestra un mensaje si no es asi.
     *
     * @return
     */
    public boolean isPlayer1Ready() {
        if (player1.isReady()) {
            return true;
        }

        setMessage("Necesita posicionar todas sus naves antes de comenzar!");
        return false;
    }

    /**
     * Inicia este game manager.
     */
    public void startManager() {
        this.state = GameState.ARRANGE;
        this.arrangeType = ShipType.SUBMARINE;
        this.arrangeShipOrientation = ShipOrientation.VERTICAL;

        this.player1.reset();

        bSubmarine.setText("Submarino (" + player1.getNumSubmarines() + ")");
        bCruiser.setText("Crucero (" + player1.getNumCruisers() + ")");
        bBattleship.setText("Acorazado (" + player1.getNumBattleships() + ")");

        tvPlayerHP.setText("HP: " + DEFAULT_HP);
        tvEnemyHP.setText("HP: " + DEFAULT_HP);

        setMessage("Posicione sus naves en el tablero.");
        this.turn = GameTurn.PLAYER_1;
        this.state = GameState.ARRANGE;
    }



    /**
     * Si el jugador 1 puso todas sus naves, iniciar la partida.
     *
     * @return
     */
    public boolean startBattle() {
        if (this.state == GameState.ARRANGE) {
            if (isPlayer1Ready()) {
                // Posiciona las naves del bot
                placeBotShips(botGridAdapter, player2);

                setMessage("Batalla iniciada!\nAtaque al enemigo!");

                tvPlayerHP.setText("HP: " + player1.getHP());
                tvEnemyHP.setText("HP: " + player2.getHP());

                this.state = GameState.BATTLE;
                return true;
            }
        }

        return false;
    }


    public void resetManager() {
        playerGridAdapter.setNewEmptyGrid();
        playerGridAdapter.notifyDataSetChanged();

        botGridAdapter.setNewEmptyGrid();
        botGridAdapter.notifyDataSetChanged();

        startManager();
    }

}


