package com.example.proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.roomdatabase.viewmodel.NotaViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // La cantidad de columnas que se vayan a utilizar en el grid
    private static final int SPAN_COUNT = 2;

    private RecyclerView dataList;
    private List<String> titles;
    private List<String> bodies;
    private GridLayoutManager gridLayoutManager;
    private String category;

    // Esto es parte de Room ----------------------------------------------
    private RVAdapterRoom rvAdapterRoom;
    private NotaViewModel notaViewModel;
    private Button btnInsert;
    private EditText etTitle;
    private EditText etBody;
    private FloatingActionButton fabCreate;
    // Esto es parte de Room ----------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicio los views
        initViews();

        // Esto es parte de Room ----------------------------------------------
        // Inicia los view models
        initViewModels();

        // Inicia los observers
        initObservers();

        // Inicia los listeners
        initListeners();
        // Esto es parte de Room ----------------------------------------------

        // Inicio de adaptadores
        initAdapters();

        // Inicio de layout managers
        initLayoutManagers();

        // Establece el layout manager al RecyclerView y después se estable el adaptador del RecyclerView (el
        // adaptador es el que maneja la parte visual de la lista, todos los cambios que se realicen
        // en la lista hay que notificárselo al adaptador.
        dataList.setLayoutManager(gridLayoutManager);

        // RVAdapterRoom es el adaptador utilizando Room para obtener los datos, necesitas la carpeta de roomdatabase
        dataList.setAdapter(rvAdapterRoom);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Hara una accion dependiendo del boton pulsado
        switch(item.getItemId()){

            case R.id.action_category:
                startActivity(new Intent(getBaseContext(), Categorias.class));
                return true;
            case R.id.action_catAll:
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rvAdapterRoom != null) {
            rvAdapterRoom.notifyChange();
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            category = (extras.getString("category"));
        }
        else{
            category = null;
        }
        initObservers();
        rvAdapterRoom.notifyChange();
    }

    private void initViews() {
        dataList = findViewById(R.id.recyclerView);
        etTitle = findViewById(R.id.etTitleInsert);
        etBody = findViewById(R.id.etBodyInsert);
        fabCreate = findViewById(R.id.create);
    }

    private void initAdapters() {
        rvAdapterRoom = new RVAdapterRoom(this);
    }

    private void initLayoutManagers() {
        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT, GridLayoutManager.VERTICAL, false);
    }


    // Esto es parte de Room ----------------------------------------------
    private void initViewModels() {
        notaViewModel = new ViewModelProvider(this).get(NotaViewModel.class);
    }

    private void initObservers() {
        // Cuando se realicen cambios dentro de la lista,
        // esta lista se actualizará con la base de datos y después el adaptador también lo hará.
        if(category != null){
            notaViewModel.selectByCat(category).observe(this, notas -> rvAdapterRoom.setNotas(notas));
        }
        else{
            notaViewModel.getAllNotas().observe(this, notas -> rvAdapterRoom.setNotas(notas));
        }
    }

    private void initListeners() {
        fabCreate.setOnClickListener(view -> {
            Intent creaNota = new Intent(getBaseContext(), MuestraNota.class);
            startActivity(creaNota);
        });
    }
    // Esto es parte de Room ----------------------------------------------
}
