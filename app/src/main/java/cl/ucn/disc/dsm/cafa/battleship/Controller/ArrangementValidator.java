package cl.ucn.disc.dsm.cafa.battleship.Controller;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cl.ucn.disc.dsm.cafa.battleship.adapters.GridAdapter;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridCell;
import cl.ucn.disc.dsm.cafa.battleship.model.CellStatus;
import cl.ucn.disc.dsm.cafa.battleship.model.Player;
import cl.ucn.disc.dsm.cafa.battleship.model.Ship;

import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.DIMENSION;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_BATTLESHIPS;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_CRUISERS;
import static cl.ucn.disc.dsm.cafa.battleship.MainActivity.NUM_SUBMARINES;

public final class ArrangementValidator {

    private static final Random rand = new Random();

    /**
     * Convierte una posicion a coordenadas en el tablero.
     * @param position la posicion de la celda.
     * @return coordenadas de la celda.
     */
    public static int[] positionToCoordinates(int position){

        int counter = 0;
        for (int i = 0; i < DIMENSION; i++){
            for (int j = 0; j < DIMENSION; j++){
                if (position == counter){
                    return new int[]{j, i};
                }
                counter++;
            }
        }
        return new int[]{0,0};
    }

    /**
     * Verifica si la celda en la posicion indicada esta vacia.
     * @param adapter
     * @param position
     * @return
     */
    public static boolean isGridCellEmpty(GridAdapter adapter, int position){
        GridCell cell = adapter.getItem(position);
        return cell != null && cell.getStatus() == CellStatus.EMPTY;
    }

    public static boolean isGridCellEmpty(GridAdapter adapter, int xCoord, int yCoord){
        GridCell cell = adapter.getItemByCoordinates(xCoord, yCoord);
        return cell != null && cell.getStatus() == CellStatus.EMPTY;
    }


    /**
     * Verifica si se puede poner una nave en la posicion indicada.
     * Las naves se posicionan hacia la derecha/abajo de la posicion.
     * @param adapter
     * @param position
     * @param shipSize
     * @param orientation
     * @return
     */
    public static boolean canPlaceShip(GridAdapter adapter, int position, int shipSize, Ship.Orientation orientation){

        int [] coords = positionToCoordinates(position);

        // Revisa cada celda que usara la nave. Basta con que una no sea valida para retornar falso.
        for (int i = 0; i < shipSize; i++){
            if (orientation == Ship.Orientation.HORIZONTAL) {
                if (!isGridCellEmpty(adapter, coords[0] + i, coords[1])) {
                    return false;
                }
            } else {
                if (!isGridCellEmpty(adapter, coords[0], coords[1] + i)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Pone una nave en el tablero. Le asigna a la nave las celdas que le corresponden.
     * @param adapter
     * @param position
     * @param ship
     */
    public static boolean placeShip(GridAdapter adapter, Player player, int position, Ship ship){

        int [] coords = positionToCoordinates(position);

        if (!canPlaceShip(adapter, position, ship.getType().getNumCells(), ship.getOrientation())){
            return false;
        }

        // Asigna a la nave cada celda que le corresponde.
        for (int i = 0; i < ship.getType().getNumCells(); i++){

            GridCell cell;

            if (ship.getOrientation() == Ship.Orientation.HORIZONTAL) {
                cell = adapter.getItemByCoordinates(coords[0] + i, coords[1]);
            } else {
                cell = adapter.getItemByCoordinates(coords[0], coords[1] + i);
            }

            ship.getCells().add(i, cell);

            if (player.getType() == Player.PlayerType.HUMAN) {
                cell.setStatus(CellStatus.USED_BY_PLAYER_1);
            } else {
                cell.setStatus(CellStatus.USED_BY_PLAYER_2);
            }
        }

        // Notifica al adaptador.
        adapter.notifyDataSetChanged();

        return true;
    }

    public static void placeBotShips(GridAdapter adapter, Player botPlayer){

        for (int i = 0; i < NUM_BATTLESHIPS; i++) {
            placeBotShip(adapter, Ship.ShipType.BATTLESHIP, botPlayer);
        }
        for (int i = 0; i < NUM_CRUISERS; i++){
            placeBotShip(adapter, Ship.ShipType.CRUISER, botPlayer);

        }

        for (int i = 0; i < NUM_SUBMARINES; i++){
            placeBotShip(adapter, Ship.ShipType.SUBMARINE, botPlayer);
        }

        Log.d("BOT_SHIPS", "-------------------------");
        for (Ship ship : botPlayer.getShips()) {
            Log.d("BOT_SHIP", ship.getType() + " - " + ship.getOrientation() + " - " + ship.getCells().get(0).getXCoord());
            Log.d("BOT_SHIPS", "-------------------------");
        }

    }

    /**
     * Intenta poner una nave generada al azar en
     * @param adapter
     * @return
     */
    public static void placeBotShip(GridAdapter adapter, Ship.ShipType type, Player botPlayer){
        int intentos = DIMENSION;
        while (intentos > 0){

            // Valores al azar.
            Ship.Orientation orientation = randomEnum(Ship.Orientation.class);
            int position = (int) (Math.random() * DIMENSION * DIMENSION);

            Ship ship = new Ship(type, orientation);

            // Si se pudo poner la nave, agregarla a la lista de naves del bot.

            if (placeShip(adapter, botPlayer, position, ship)){
                return;
            }

            // Si no, intentar de nuevo.
            intentos--;
        }

    }


    /**
     * Obtiene un valor de enum aleatorio.
     * SRC: https://stackoverflow.com/a/14257525
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = rand.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }



}
