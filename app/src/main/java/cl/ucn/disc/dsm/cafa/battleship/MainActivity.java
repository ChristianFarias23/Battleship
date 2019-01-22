package cl.ucn.disc.dsm.cafa.battleship;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cl.ucn.disc.dsm.cafa.battleship.Controller.GameManager;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridAdapter;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridCell;

import static cl.ucn.disc.dsm.cafa.battleship.Controller.ArrangementValidator.positionToCoordinates;
//import lombok.extern.slf4j.Slf4j;

//TODO: Ordenar codigo. Delegar contenido a clases especificas.

//@Slf4j
public class MainActivity extends AppCompatActivity {

    // Constantes:

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

    // Propiedades:
    private GridAdapter player1GridAdapter;
    private GridAdapter player2GridAdapter;

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
        Toast.makeText(this, "On Start", Toast.LENGTH_LONG).show();
    }

    private void setListeners() {

        this.bComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameManager.getState() == GameManager.GameState.ARRANGE) {
                    Toast.makeText(MainActivity.this, "Comenzar", Toast.LENGTH_SHORT).show();
                    gameManager.setBattleState();
                } else {
                    Toast.makeText(MainActivity.this, "Partida en curso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.bReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Reiniciar", Toast.LENGTH_SHORT).show();
                gameManager.reset();
            }
        });

        gvPlayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                tvMessage.setText("Player Coords: "+ Arrays.toString(positionToCoordinates(position)));

                gameManager.managePlayerGridViewItemClick((GridAdapter) parent.getAdapter(), position);
            }
        });

        gvRival.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                tvMessage.setText("Rival Coords: "+ Arrays.toString(positionToCoordinates(position)));

                gameManager.manageRivalGridViewItemClick((GridAdapter) parent.getAdapter(), position);
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

        player1GridAdapter = new GridAdapter(this, playerCells);
        player2GridAdapter = new GridAdapter(this, rivalCells);

        gvPlayer.setNumColumns(DIMENSION);
        gvPlayer.setAdapter(player1GridAdapter);

        gvRival.setNumColumns(DIMENSION);
        gvRival.setAdapter(player2GridAdapter);

        gameManager.setBotGridAdapter(player2GridAdapter);
        gameManager.setPlayerGridAdapter(player1GridAdapter);
        gameManager.setMessageTextView(tvMessage);

    }
}
