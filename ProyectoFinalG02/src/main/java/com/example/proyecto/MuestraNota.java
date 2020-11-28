package com.example.proyecto;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyecto.roomdatabase.model.Nota;
import com.example.proyecto.roomdatabase.viewmodel.NotaViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MuestraNota extends AppCompatActivity
{

    private String category;
    private ArrayAdapter<String> adp;
    private NotaViewModel notaViewModel;
    private EditText etTitle;
    private EditText etBody;
    private int notaID;
    private String date;
    private Spinner spCategory;
    private EditText etCategory;

    private Switch anyadirCal;
    private CalendarView calendario;
    private Button abrirCalendario;

    private  AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);
        Bundle extras = getIntent().getExtras();
        adp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViewModels();
        initViews();
        initListeners();
        initExtras(extras);
        initCalendar();
        initObservers();

        spCategory.setAdapter(adp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_etiqueta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //Hara una accion dependiendo del boton pulsado
        switch (item.getItemId()) {

            //Guarda la nota
            case R.id.action_save:
                if (notaID != -1) {
                    saveEdit();
                } else {
                    saveNew();
                }
                return true;
            //Borra la nota
            case R.id.action_delete:
                notaViewModel.deleteByID(notaID);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                if (!date.equals("00/00/0000")) {
                    Toast.makeText(getApplicationContext(), "Eliminar del calendario", Toast.LENGTH_SHORT)
                            .show();
                    try {
                        Long antiguo = sdf.parse(date).getTime();

                        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                        builder.appendPath("time");
                        ContentUris.appendId(builder, antiguo);
                        Intent intent = new Intent(Intent.ACTION_VIEW)
                                .setData(builder.build());
                        startActivity(intent);
                    } catch (ParseException e) {
                    }
                }
                finish();
                return true;
            case R.id.action_category:
                startActivity(new Intent(getBaseContext(), Categorias.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        etTitle = findViewById(R.id.etTitleInsert);
        etBody = findViewById(R.id.etBodyInsert);
        spCategory = findViewById(R.id.spCategoryInsert);
        etCategory = findViewById(R.id.etCategoryInsert);
        calendario = findViewById(R.id.calendar);
        anyadirCal = findViewById(R.id.addDate);
        abrirCalendario = findViewById(R.id.openCalendar);
        calendario.setVisibility(View.GONE);
        date = "00/00/0000";

    }

    private void initExtras(Bundle extras)
    {
        if (extras != null)
        {
            etTitle.setText(extras.getString("title"));
            etBody.setText(extras.getString("body"));
            notaID = (extras.getInt("notaID"));
            date = (extras.getString("date"));
            etCategory.setText(extras.getString("category"));
            category = extras.getString("category");
        }
        else
        {
            notaID = -1;
        }
    }

    private void initViewModels()
    {
        notaViewModel = new ViewModelProvider(this).get(NotaViewModel.class);
    }

    private void initObservers()
    {
        notaViewModel.getAllCatASC().observe(this, new Observer<List<String>>()
        {
            @Override
            public void onChanged(List<String> lista)
            {
                adp.addAll(lista);
                spCategory.setSelection((adp.getPosition(category)));
            }
        });
    }

    private void initListeners()
    {
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                etCategory.setText(spCategory.getAdapter().getItem(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        abrirCalendario.setOnClickListener(y -> {
            if(calendario.getVisibility()== View.VISIBLE)
                calendario.setVisibility(View.GONE);
            else
                calendario.setVisibility(View.VISIBLE);
        });

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = String.valueOf(dayOfMonth)+ "/" + String.valueOf(month+1)+ "/" +String.valueOf(year);
                try {
                    calendario.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(selectedDate).getTime(), true, true);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void saveNew()
    {
        if (etTitle.getText().length() > 0 && etBody.getText().length() > 0) {
            if (anyadirCal.isChecked()) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                date = sdf.format(calendario.getDate());

                Nota nota = new Nota(etTitle.getText().toString(), etBody.getText()
                        .toString(), etCategory.getText()
                        .toString()
                        .toLowerCase(), date);

                notaViewModel.insert(nota);
                Toast.makeText(getApplicationContext(), "Introducir evento en el calendario", Toast.LENGTH_SHORT)
                        .show();
                //Introducimos en el calendario:
                Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setData(CalendarContract.Events.CONTENT_URI);
                calIntent.putExtra(CalendarContract.Events.TITLE, etTitle.getText().toString());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calendario.getDate());
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calendario.getDate()+1);
                startActivity(calIntent);
                finish();
            }
            else {

                Nota nota = new Nota(etTitle.getText().toString(), etBody.getText()
                        .toString(), etCategory.getText()
                        .toString()
                        .toLowerCase(), "00/00/0000");
                notaViewModel.insert(nota);
                finish();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No has escrito nada", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void saveEdit() {

        String antiguaFecha = date;
        if (etTitle.getText().length() > 0 && etBody.getText().length() > 0)
        {
            if(anyadirCal.isChecked()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                date = sdf.format(calendario.getDate());

                notaViewModel.upload(notaID, etTitle.getText().toString(), etBody.getText()
                        .toString(), etCategory
                        .getText()
                        .toString()
                        .toLowerCase(), date);
            }
            else{
                date = "00/00/0000";
                notaViewModel.upload(notaID, etTitle.getText().toString(), etBody.getText()
                        .toString(), etCategory
                        .getText()
                        .toString()
                        .toLowerCase(), date);
            }

            //Mete la nueva fecha en el calendario
            if(!antiguaFecha.equals(date)){
                if(!date.equals("00/00/0000")) {
                    Toast.makeText(getApplicationContext(), "Introducir en la nueva fecha", Toast.LENGTH_SHORT)
                            .show();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    try {

                        Date fecha = sdf.parse(date);
                        long nuevo = fecha.getTime();

                        Intent calIntent = new Intent(Intent.ACTION_INSERT);
                        calIntent.setData(CalendarContract.Events.CONTENT_URI);
                        calIntent.putExtra(CalendarContract.Events.TITLE, etTitle.getText().toString());
                        calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                nuevo);
                        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                                nuevo + 1);
                        startActivity(calIntent);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                //Si la antigua fecha tenía cosas
                if(!(antiguaFecha.equals("00/00/0000"))) {

                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Cambio de fecha")
                            .setMessage("¿Quieres acceder a la fecha asociada anteriormente a esta nota?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                        Long antiguo = sdf.parse(antiguaFecha).getTime();
                                        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                                        builder.appendPath("time");
                                        ContentUris.appendId(builder, antiguo);
                                        Intent intent = new Intent(Intent.ACTION_VIEW)
                                                .setData(builder.build());
                                        startActivity(intent);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });

                    AlertDialog dialog = builder.create();

                    dialog.show();
                }
                else
                    finish();


            }
            else{
                finish();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No has escrito nada", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void initCalendar() {
        if(!date.equals("00/00/0000")){
            try {
                this.anyadirCal.setChecked(true);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date fecha = sdf.parse(date);
                Long nueva = fecha.getTime();
                calendario.setDate(nueva);
            }
            catch(ParseException e){}
        }
    }
}
