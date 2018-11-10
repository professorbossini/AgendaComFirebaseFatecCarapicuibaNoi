package br.com.bossini.agendacomfirebasefateccarapicuibanoi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class ListaDeContatosActivity extends AppCompatActivity {

    private List <Contato> contatos;
    private ArrayAdapter <Contato> contatosAdapter;
    private ListView contatosListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_contatos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contatos = new LinkedList<>();
        contatosAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, contatos);
        contatosListView = findViewById(R.id.contatosListView);
        contatosListView.setAdapter(contatosAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =
                        new Intent(ListaDeContatosActivity.this,
                                AdicionaContatoActivity.class);
                startActivity(intent);
            }
        });
    }


}
