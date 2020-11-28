package com.example.proyecto.roomdatabase.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.proyecto.roomdatabase.NotaRoomDatabase;
import com.example.proyecto.roomdatabase.dao.NotaDao;
import com.example.proyecto.roomdatabase.model.Nota;

import java.util.ArrayList;
import java.util.List;

// Un repositorio maneja las consultas y te da la posibilidad de manejar multiples backends.
public class NotaRepository
{
	private NotaDao notaDao;
	private LiveData<List<Nota>> allNotas;

	public NotaRepository(Application application)
	{
		// Obtienes la instancia de la base de datos
		NotaRoomDatabase db = NotaRoomDatabase.getInstance(application);

		// Obtienes el DAO de la tabla que quieres utilizar
		notaDao = db.notaDao();

		// Obtienes la lista del contenido de la tabla
		allNotas = notaDao.getAllNotasASC();
	}


	// Se devuelve un LiveData porque la lista se creó a partir de un LiveData
	public LiveData<List<Nota>> getAllNotas()
	{
		return allNotas;
	}

	// Al realizar un insert, delete o update es necesario que esas operaciones se hagan en un hilo
	// diferente al hilo principal
	public void insert(Nota nota)
	{
		NotaRoomDatabase.databaseWriteExecutor.execute(() -> {
			notaDao.insert(nota);
		});
	}

	public void deleteByID(int id)
	{
		NotaRoomDatabase.databaseWriteExecutor.execute(() -> {
			notaDao.delete(id);
		});
	}

	public void upload(int id, String newTitle, String newBody, String newCategory, String newDate)
	{
		NotaRoomDatabase.databaseWriteExecutor.execute(() -> {
			notaDao.upload(id, newTitle, newBody, newCategory, newDate);
		});
	}

	public LiveData<List<Nota>> selectByCat(String category) { return notaDao.selectByCat(category); }

	public LiveData<List<String>> getAllCatASC()	{ return notaDao.getAllCatASC(); }

	public void deleteCategory(String category)
	{
		NotaRoomDatabase.databaseWriteExecutor.execute(() -> {
			notaDao.deleteCategory(category);
		});
	}
}
