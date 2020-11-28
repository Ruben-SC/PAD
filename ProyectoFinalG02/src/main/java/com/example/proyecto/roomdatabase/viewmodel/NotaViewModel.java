package com.example.proyecto.roomdatabase.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.proyecto.roomdatabase.model.Nota;
import com.example.proyecto.roomdatabase.repositories.NotaRepository;

import java.util.ArrayList;
import java.util.List;

// Se crea esta clase para comunicarse con el repositorio y con el interfaz. Las clases que extienden
// de ViewModel son importantes porque sobreviven a los cambios de los interfaces y los datos que se
// estén manipulando se quedan guardados y no se pierden
public class NotaViewModel extends AndroidViewModel
{
	// Mantener el repositorio en private para encapsular los métodos que se quieran llamar más tarde.
	private NotaRepository notaRepository;
	private LiveData<List<Nota>> allNotas;

	public NotaViewModel(@NonNull Application application)
	{
		super(application);
		// Se obtiene el repositorio
		notaRepository = new NotaRepository(application);

		// La información dentro del repositorio que se quiera obtener al principio
		allNotas = notaRepository.getAllNotas();
	}

	public LiveData<List<Nota>> getAllNotas()
	{
		return allNotas;
	}

	public void insert(Nota nota)
	{
		notaRepository.insert(nota);
	}

	public void deleteByID(int id)
	{
		notaRepository.deleteByID(id);
	}


	public void upload(int id, String newTitle, String newBody, String newCategory, String newDate)
	{
		notaRepository.upload(id, newTitle, newBody, newCategory, newDate);
	}

    public LiveData<List<Nota>> selectByCat(String category) { return notaRepository.selectByCat(category); }

    public LiveData<List<String>> getAllCatASC()	{ return notaRepository.getAllCatASC(); }

	public void deleteCategory(String category)
	{
		notaRepository.deleteCategory(category);
	}
}
