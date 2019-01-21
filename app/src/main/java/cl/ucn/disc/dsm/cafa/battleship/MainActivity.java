package cl.ucn.disc.dsm.cafa.battleship;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
//import lombok.extern.slf4j.Slf4j;

//@Slf4j
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.b_ordenar) Button bOrdenar;
    @BindView(R.id.b_comenzar) Button bComenzar;
    @BindView(R.id.b_reiniciar) Button bReiniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bOrdenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Ordenar", Toast.LENGTH_SHORT).show();
            }
        });

        bComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Comenzar", Toast.LENGTH_SHORT).show();
            }
        });

        bReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Reiniciar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "On Start", Toast.LENGTH_LONG).show();
    }
}
