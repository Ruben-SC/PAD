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

public class Categorias extends AppCompatActivity {
    // La cantidad de columnas que se vayan a utilizar en el grid
    private static final int SPAN_COUNT = 1;

    private RecyclerView dataList;
    private GridLayoutManager gridLayoutManager;

    // Esto es parte de Room ----------------------------------------------
    private RVAdapterCategorias rvAdapterCategorias;
    private NotaViewModel notaViewModel;
    // Esto es parte de Room ----------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

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

        // RVAdapterCategorias es el adaptador utilizando Room para obtener los datos, necesitas la carpeta de roomdatabase
        dataList.setAdapter(rvAdapterCategorias);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rvAdapterCategorias != null) {
            rvAdapterCategorias.notifyChange();
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.getString("eliminar") != null){
                notaViewModel.deleteCategory(extras.getString("eliminar"));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_categorias, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Hara una accion dependiendo del boton pulsado
        switch(item.getItemId()){

            case R.id.action_catAll:
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                return true;
            case R.id.action_catNon:
                categoryNon();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        dataList = findViewById(R.id.recyclerViewCat);
    }

    private void initAdapters() {
        rvAdapterCategorias = new RVAdapterCategorias(this);
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
        notaViewModel.getAllCatASC().observe(this, notas -> rvAdapterCategorias.setCategorias(notas));
    }

    private void initListeners() {

    }
    // Esto es parte de Room ----------------------------------------------

    private void categoryNon(){
        Intent theIntent = new Intent(getBaseContext(), MainActivity.class);
        theIntent.putExtra("category", "");
        startActivity(theIntent);
    }
}
