package br.com.bossini.agendacomfirebasefateccarapicuibanoi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class ListaDeContatosActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference contatosReference;
    public void configuraDatabase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        contatosReference = firebaseDatabase.getReference("contatos");
    }
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
        configuraDatabase();
        configuraCliqueLongoNoItemDaListaListener ();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contatosReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                contatos.clear();
                for (DataSnapshot filho : dataSnapshot.getChildren()){
                    Contato contato = filho.getValue(Contato.class);
                    contato.setId(filho.getKey());
                    contatos.add(contato);
                }
                contatosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListaDeContatosActivity.this,
                        getString(R.string.erro_firebase),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configuraCliqueLongoNoItemDaListaListener (){
        contatosListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder deletarAtualizarDialogoBuilder =
                        new AlertDialog.Builder(ListaDeContatosActivity.this);
                deletarAtualizarDialogoBuilder.
                        setPositiveButton(R.string.deletar_contato,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Contato aRemover = contatos.get(position);
                                        contatosReference.child(aRemover.getId()).removeValue();
                                        Toast.makeText(ListaDeContatosActivity.this,
                                                getString(R.string.contato_removido),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).
                        setNegativeButton(R.string.atualizar_contato,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        AlertDialog.Builder atualizarDialogBuilder =
                                                new AlertDialog.
                                                        Builder(ListaDeContatosActivity.this);
                                        final AlertDialog atualizarDialog =
                                                atualizarDialogBuilder.create();
                                        LayoutInflater inflater =
                                                LayoutInflater.from(ListaDeContatosActivity.this);
                                        View arvore = inflater.
                                                inflate(R.layout.activity_adiciona_contato,
                                                null);
                                        atualizarDialogBuilder.setView(arvore);
                                        final Contato aAtualizar = contatos.get(position);
                                        final EditText nomeEditText =
                                                arvore.findViewById(R.id.nomeEditText);
                                        nomeEditText.setText(aAtualizar.getNome());
                                        final EditText foneEditText =
                                                arvore.findViewById(R.id.foneEditText);
                                        foneEditText.setText(aAtualizar.getFone());
                                        final EditText emailEditText =
                                                arvore.findViewById(R.id.emailEditText);
                                        emailEditText.setText(aAtualizar.getEmail());
                                        FloatingActionButton floatingActionButton =
                                                arvore.findViewById(R.id.fab);
                                        floatingActionButton.setOnClickListener(
                                                new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                aAtualizar.setNome(nomeEditText.getEditableText().toString());
                                                aAtualizar.setFone(foneEditText.getEditableText().toString());
                                                aAtualizar.setEmail(emailEditText.getEditableText().toString());
                                                contatosReference.
                                                        child(aAtualizar.getId()).
                                                        setValue(aAtualizar);
                                                Toast.makeText(ListaDeContatosActivity.this,
                                                        getString(R.string.atualizar_contato),
                                                        Toast.LENGTH_SHORT).show();
                                                atualizarDialog.cancel();
                                            }
                                        });
                                        atualizarDialog.show();
                                    }
                                }).create().show();
                return false;
            }
        });
    }
}
