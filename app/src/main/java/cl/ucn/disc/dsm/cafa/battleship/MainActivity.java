package cl.ucn.disc.dsm.cafa.battleship;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridAdapter;
import cl.ucn.disc.dsm.cafa.battleship.adapters.GridCell;
//import lombok.extern.slf4j.Slf4j;

//@Slf4j
public class MainActivity extends AppCompatActivity {

    // Constantes:

    // Dimension del GridView (Numero de columnas = Numero de filas).
    private static final int DIMENSION = 8;


    // Vistas:
    @BindView(R.id.b_ordenar)
    Button bOrdenar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setListeners();
        startGridAdapters();

        // Inicia el juego.
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "On Start", Toast.LENGTH_LONG).show();
    }

    private void init() {

    }

    private void setListeners() {
        this.bOrdenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Ordenar", Toast.LENGTH_SHORT).show();
            }
        });

        this.bComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Comenzar", Toast.LENGTH_SHORT).show();
            }
        });

        this.bReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Reiniciar", Toast.LENGTH_SHORT).show();
            }
        });

        gvPlayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, ""+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startGridAdapters(){
        List<GridCell> cells = new ArrayList<>();

        for (int i = 0; i< DIMENSION; i++){
            for (int j = 0; j< DIMENSION; j++){
                cells.add(new GridCell());
            }
        }

        player1GridAdapter = new GridAdapter(this, cells);
        player2GridAdapter = new GridAdapter(this, cells);

        gvPlayer.setNumColumns(DIMENSION);
        gvPlayer.setAdapter(player1GridAdapter);

        gvRival.setNumColumns(DIMENSION);
        gvRival.setAdapter(player2GridAdapter);
    }

}
