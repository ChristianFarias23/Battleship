package cl.ucn.disc.dsm.cafa.battleship;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import cl.ucn.disc.dsm.cafa.battleship.Controller.GameManager;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridAdapter;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridCell;
import cl.ucn.disc.dsm.cafa.battleship.enumerations.ShipOrientation;
import cl.ucn.disc.dsm.cafa.battleship.enumerations.ShipType;
//import lombok.extern.slf4j.Slf4j;

//TODO: Ordenar codigo. Delegar contenido a clases especificas.
//TODO: Implementar una vista que indique el estado de las naves del jugador y del enemigo.

//@Slf4j
public class MainActivity extends AppCompatActivity {

    // Constantes: Tenga cuidado al cambiarlas, considere que todas las naves deben caber en DIMENSION^2.

    // Dimension de los tableros (Numero de columnas = Numero de filas).
    public static final int DIMENSION = 6;

    // La cantidad de submarinos.
    public static final int NUM_SUBMARINES = 3;

    // La cantidad de cruceros.
    public static final int NUM_CRUISERS = 2;

    // La cantidad de acorazados.
    public static final int NUM_BATTLESHIPS = 1;


    // Vistas:

    @BindView(R.id.tv_message)
    TextView tvMessage;

    @BindView(R.id.b_comenzar)
    Button bComenzar;

    @BindView(R.id.b_reiniciar)
    Button bReiniciar;

    @BindView(R.id.gv_player)
    GridView gvPlayer;

    @BindView(R.id.gv_rival)
    GridView gvRival;

    @BindView(R.id.b_submarine)
    Button bSubmarine;

    @BindView(R.id.b_cruiser)
    Button bCruiser;

    @BindView(R.id.b_battleship)
    Button bBattleship;

    @BindView(R.id.toggle_vh)
    ToggleButton toggleVH;

    // Colores:

    @BindColor(R.color.bootstrap4Warning)
    int b4Yellow;

    @BindColor(R.color.bootstrap4Fadded)
    int b4White;

    @BindColor(R.color.fadded)
    int fadded;

    /**
     * El controlador del juego.
     */
    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Obtener instancia de GameManager.
        gameManager = GameManager.getInstance();

        // Configurarlo.
        configureGameManager();

        // Asignar listeners.
        setListeners();

        // Iniciar manager.
        gameManager.startManager();
    }

    /**
     * Asigna los listeners a las vistas necesarias.
     */
    private void setListeners() {

        this.bComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameManager.startBattle()){
                    buttonsSetEnabled(false);
                }
            }
        });

        this.bReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameManager.resetManager();
                buttonsSetEnabled(true);
                toggleVH.setChecked(true);
            }
        });

        gvPlayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gameManager.managePlayerGridViewItemClick(position);
            }
        });

        gvRival.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gameManager.manageRivalGridViewItemClick(position);
            }
        });

        bSubmarine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameManager.setArrangeType(ShipType.SUBMARINE);

                bBattleship.setTextColor(b4White);
                bCruiser.setTextColor(b4White);
                bSubmarine.setTextColor(b4Yellow);
            }
        });

        bCruiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameManager.setArrangeType(ShipType.CRUISER);

                bBattleship.setTextColor(b4White);
                bSubmarine.setTextColor(b4White);
                bCruiser.setTextColor(b4Yellow);
            }
        });

        bBattleship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameManager.setArrangeType(ShipType.BATTLESHIP);

                bCruiser.setTextColor(b4White);
                bSubmarine.setTextColor(b4White);
                bBattleship.setTextColor(b4Yellow);
            }
        });

        toggleVH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gameManager.setArrangeShipOrientation(ShipOrientation.VERTICAL);
                } else {
                    gameManager.setArrangeShipOrientation(ShipOrientation.HORIZONTAL);
                }
            }
        });

        bSubmarine.performClick();
        toggleVH.setChecked(true);

    }

    /**
     * Configura el controlador del juego.
     */
    private void configureGameManager(){

        GridAdapter player1GridAdapter = new GridAdapter(this);
        GridAdapter player2GridAdapter = new GridAdapter(this);

        gvPlayer.setNumColumns(DIMENSION);
        gvPlayer.setAdapter(player1GridAdapter);

        gvRival.setNumColumns(DIMENSION);
        gvRival.setAdapter(player2GridAdapter);

        gameManager.setMessageTextView(tvMessage);
        gameManager.setBotGridAdapter(player2GridAdapter);
        gameManager.setPlayerGridAdapter(player1GridAdapter);
        gameManager.setSubmarineButton(bSubmarine);
        gameManager.setCruiserButton(bCruiser);
        gameManager.setBattleshipButton(bBattleship);
    }

    /**
     * Activa o desactiva los botones.
     * @param bool
     */
    private void buttonsSetEnabled(boolean bool){
        if (!bool){
            bSubmarine.setTextColor(fadded);
            bCruiser.setTextColor(fadded);
            bBattleship.setTextColor(fadded);
        } else {
            bSubmarine.setTextColor(b4Yellow);
            bCruiser.setTextColor(b4White);
            bBattleship.setTextColor(b4White);
        }
        bSubmarine.setEnabled(bool);
        bCruiser.setEnabled(bool);
        bBattleship.setEnabled(bool);
        bComenzar.setEnabled(bool);
        toggleVH.setEnabled(bool);
    }
}
