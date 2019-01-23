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

import butterknife.BindView;
import butterknife.ButterKnife;
import cl.ucn.disc.dsm.cafa.battleship.Controller.GameManager;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridAdapter;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridCell;
import cl.ucn.disc.dsm.cafa.battleship.model.Ship;
//import lombok.extern.slf4j.Slf4j;

//TODO: Ordenar codigo. Delegar contenido a clases especificas.

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


    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Obtener instancia de GameManager.
        gameManager = GameManager.getInstance();

        setListeners();
        startGridAdapters();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setListeners() {

        this.bComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameManager.getState() == GameManager.GameState.ARRANGE) {
                    if (gameManager.isPlayer1Ready())
                        gameManager.setBattleState();
                }
            }
        });

        this.bReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameManager.reset();
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
                gameManager.setArrangeType(Ship.ShipType.SUBMARINE);
            }
        });

        bCruiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameManager.setArrangeType(Ship.ShipType.CRUISER);
            }
        });

        bBattleship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameManager.setArrangeType(Ship.ShipType.BATTLESHIP);
            }
        });

        toggleVH.setChecked(true);

        toggleVH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gameManager.setArrangeOrientation(Ship.Orientation.VERTICAL);
                } else {
                    gameManager.setArrangeOrientation(Ship.Orientation.HORIZONTAL);
                }
            }
        });
    }

    private void startGridAdapters(){
        List<GridCell> playerCells = new ArrayList<>();
        List<GridCell> rivalCells = new ArrayList<>();

        for (int i = 0; i< DIMENSION; i++){
            for (int j = 0; j< DIMENSION; j++){
                rivalCells.add(new GridCell(j,i));
                playerCells.add(new GridCell(j,i));
            }
        }

        // Propiedades:
        GridAdapter player1GridAdapter = new GridAdapter(this, playerCells);
        GridAdapter player2GridAdapter = new GridAdapter(this, rivalCells);

        gvPlayer.setNumColumns(DIMENSION);
        gvPlayer.setAdapter(player1GridAdapter);

        gvRival.setNumColumns(DIMENSION);
        gvRival.setAdapter(player2GridAdapter);

        gameManager.setBotGridAdapter(player2GridAdapter);
        gameManager.setPlayerGridAdapter(player1GridAdapter);

        gameManager.setMessageTextView(tvMessage);

        gameManager.setSubmarineButton(bSubmarine);
        gameManager.setCruiserButton(bCruiser);
        gameManager.setBattleshipButton(bBattleship);

        gameManager.start();

    }
}
