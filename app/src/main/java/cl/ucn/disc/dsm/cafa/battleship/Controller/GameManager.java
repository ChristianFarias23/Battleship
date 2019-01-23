package cl.ucn.disc.dsm.cafa.battleship.Controller;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

import cl.ucn.disc.dsm.cafa.battleship.adapters.GridAdapter;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridCell;
import cl.ucn.disc.dsm.cafa.battleship.model.CellStatus;
import cl.ucn.disc.dsm.cafa.battleship.model.Player;
import cl.ucn.disc.dsm.cafa.battleship.model.Ship;
import lombok.Getter;
import lombok.Setter;

import static cl.ucn.disc.dsm.cafa.battleship.Controller.ArrangementValidator.placeBotShips;
import static cl.ucn.disc.dsm.cafa.battleship.Controller.ArrangementValidator.placeShip;
import static cl.ucn.disc.dsm.cafa.battleship.Controller.ArrangementValidator.positionToCoordinates;
import static cl.ucn.disc.dsm.cafa.battleship.Controller.ArrangementValidator.randomAttack;
import static cl.ucn.disc.dsm.cafa.battleship.Controller.ArrangementValidator.setShipGridCellsColor;
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
    }

    // ....

    private TextView tvMessage;

    private Button bSubmarine, bCruiser, bBattleship;

    private GridAdapter botGridAdapter;

    private GridAdapter playerGridAdapter;


    @Getter
    GameState state = GameState.ARRANGE;

    @Getter
    GameTurn turn = GameTurn.PLAYER_1;

    @Getter
    private Ship.ShipType arrangeType = Ship.ShipType.SUBMARINE;

    @Getter
    private Ship.Orientation arrangeOrientation = Ship.Orientation.VERTICAL;


    private Player player1 = new Player(Player.PlayerType.HUMAN);
    private Player player2 = new Player(Player.PlayerType.BOT);


    public void setArrangeType(Ship.ShipType arrangeType){
        this.arrangeType = arrangeType;
        int numCell = this.arrangeType.getNumCells();
        setMessage("Esta nave utiliza " + numCell + (numCell == 1 ? " celda." : " celdas."));
    }

    public void setArrangeOrientation(Ship.Orientation arrangeOrientation){
        this.arrangeOrientation = arrangeOrientation;
    }

    public void setSubmarineButton(Button button){
        this.bSubmarine = button;
    }

    public void setCruiserButton(Button button){
        this.bCruiser = button;
    }

    public void setBattleshipButton(Button button){
        this.bBattleship = button;
    }


    public void managePlayerGridViewItemClick(int position) {
        // Ordenar las naves del jugador.
        if (this.state == GameState.ARRANGE) {
            Ship ship = new Ship(this.arrangeType, this.arrangeOrientation);

            // TODO: Contador de naves restantes.
            placePlayer1Ship(position, ship);
        }
    }

    private void placePlayer1Ship(int position, Ship ship){

        // Verifica que se puedan poner mas naves.
        if (arrangeType == Ship.ShipType.SUBMARINE && player1.getNumSubmarines() <= 0 ){
            setMessage("Limite de submarinos alcanzado!");
            return;
        }

        if (arrangeType == Ship.ShipType.CRUISER && player1.getNumCruisers() <= 0 ){
            setMessage("Limite de cruceros alcanzado!");
            return;
        }

        if (arrangeType == Ship.ShipType.BATTLESHIP && player1.getNumBattleships() <= 0 ){
            setMessage("Limite de acorazados alcanzado!");
            return;
        }

        if (placeShip(playerGridAdapter, player1, position, ship)){
            player1.getShips().add(ship);
            setShipGridCellsColor(playerGridAdapter, ship);

            switch (arrangeType){
                case SUBMARINE:
                    player1.substractSubmarine();
                    bSubmarine.setText("Submarino ("+player1.getNumSubmarines()+")");
                    break;
                case CRUISER:
                    player1.substractCruiser();
                    bCruiser.setText("Crucero ("+player1.getNumCruisers()+")");
                    break;
                case BATTLESHIP:
                    player1.substractBattleship();
                    bBattleship.setText("Acorazado ("+player1.getNumBattleships()+")");
                    break;
            }
            setMessage("Nave posicionada correctamente en " + Arrays.toString(positionToCoordinates(position))+".");
        } else {
            setMessage("No se puede poner la nave aqui, posicion invalida! " + Arrays.toString(positionToCoordinates(position)));
        }
    }

    private void setMessage(String message){
        this.tvMessage.setText(message);
    }

    public void manageRivalGridViewItemClick(int position) {
        // Atacar las naves del rival.
        if (this.state == GameState.BATTLE && this.turn == GameTurn.PLAYER_1) {
            GridCell cell = botGridAdapter.getItem(position);

            //Checkear si hay o no una nave en esta posicion.

            if (cell.getStatus().equals(CellStatus.USED_BY_PLAYER_2)) {
                // Hit.
                cell.setStatus(CellStatus.HIT);
                setMessage("Hit!");
            } else if (cell.getStatus().equals(CellStatus.HIT) || cell.getStatus().equals(CellStatus.MISS) ){
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

        if (this.state == GameState.BATTLE && player2.hasLost()) {
            setMessage("Has ganado!");
            this.turn = GameTurn.END;
        }

        // Bot ataca al jugador.
        if (this.state == GameState.BATTLE && this.turn == GameTurn.PLAYER_2) {
            // Atacar al jugador.
            botAttacks();

            // Cambiar de turno.
            this.turn = GameTurn.PLAYER_1;
        }

        if (this.state == GameState.BATTLE && player1.hasLost()){
            setMessage("Has perdido!");
            this.turn = GameTurn.END;
        }
    }


    private void botAttacks(){
        //La unica forma en que retorne falso es cuando ambos jugadores hayan llenado _todo_ el tablero.
        if (!randomAttack(playerGridAdapter)){
            Log.d("<<<<<<<<<<<<", "Tableros llenos.");
        }
    }

    public boolean isPlayer1Ready(){
        if (player1.isReady()){
            return true;
        }

        setMessage("Necesita posicionar todas sus naves antes de comenzar!");
        return false;
    }

    public void start() {
        this.state = GameState.ARRANGE;
        this.arrangeType = Ship.ShipType.SUBMARINE;
        this.arrangeOrientation = Ship.Orientation.VERTICAL;

        this.player1.reset();

        bSubmarine.setText("Submarino ("+player1.getNumSubmarines()+")");
        bCruiser.setText("Crucero ("+player1.getNumCruisers()+")");
        bBattleship.setText("Acorazado ("+player1.getNumBattleships()+")");

        setMessage("Posicione sus naves en el tablero.");
        this.turn = GameTurn.PLAYER_1;
        this.state = GameState.ARRANGE;
    }

    public void setBattleState() {
        // Ordenar las piezas del bot.

        setMessage("El bot esta ordenando sus naves...");

        Log.d("SHIP_PLACEMENT", "El Bot esta ordenando sus naves...");
        placeBotShips(botGridAdapter, player2);

        setMessage("Batalla iniciada!\nAtaque al enemigo!");
        this.state = GameState.BATTLE;
    }

    public void reset() {
        playerGridAdapter.setNewEmptyGrid();
        playerGridAdapter.notifyDataSetChanged();

        botGridAdapter.setNewEmptyGrid();
        botGridAdapter.notifyDataSetChanged();

        start();
    }

    //...

    public enum GameState {
        ARRANGE,
        BATTLE
    }

    public enum GameTurn{
        PLAYER_1,
        PLAYER_2,
        END
    }
}


