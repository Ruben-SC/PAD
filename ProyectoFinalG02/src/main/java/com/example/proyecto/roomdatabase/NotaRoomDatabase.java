package com.example.proyecto.roomdatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.proyecto.roomdatabase.model.Nota;
import com.example.proyecto.roomdatabase.dao.NotaDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Se utiliza la anotación Database para crear la base de datos y las tablas que va a contener

// Es necesario que la clase que contiene la base de datos sea Abstracta y que extienda de RoomDatabase.
@Database(entities = {Nota.class}, version = 1, exportSchema = false)
public abstract class NotaRoomDatabase extends RoomDatabase
{
	public abstract NotaDao notaDao();

	// Se pone en private para crear un SINGLETON con esta clase y solo se pueda instanciar una vez.
	private static volatile NotaRoomDatabase INSTANCE;

	private static final int NUM_OF_THREADS = 4;

	// Con esto se crea un pool de hilos (thread pool) que realizarán operaciones en la base de datos
	// en un hilo de fondo (background) de manera asíncrona.
	public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_OF_THREADS);

	// Devuelve el SINGLETON. Creará la base de datos la primera vez que se llame. Con el
	// databasebuilder de Room se creará un objeto de RoomDatabase en el contexto de la aplicación.
	public static NotaRoomDatabase getInstance(final Context context)
	{
		if (INSTANCE == null)
		{
			synchronized (NotaRoomDatabase.class)
			{
				if (INSTANCE == null)
				{
					// Guardar el nombre de la base de datos en res/values/strings o un lugar más visible
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(), NotaRoomDatabase.class, "nota_database").build();
				}
			}
		}

		return INSTANCE;
	}
}
