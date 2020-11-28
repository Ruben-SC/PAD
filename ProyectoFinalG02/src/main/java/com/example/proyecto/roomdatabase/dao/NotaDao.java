package com.example.proyecto.roomdatabase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.proyecto.roomdatabase.model.Nota;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// El DAO (interfaz) sirve para comunicar el modelo/entidad/tabla con las diferentes clases que vayan a utilizarlo.
@Dao
public interface NotaDao
{
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	void insert(Nota nota);

	// Las anotaciones Query puede hacer lo que un comand SQL normal. Ten cuidado con el DELETE FROM SIN WHERE!! XD
	@Query("DELETE FROM nota_table")
	void deleteAll();

	// En esta anotación se realiza un simple select de toda la tabla ordenado por nota_id en ascendente
	@Query("SELECT * FROM nota_table ORDER BY notaid ASC")
    LiveData<List<Nota>> getAllNotasASC();

	@Query("DELETE FROM nota_table WHERE notaid = :id")
	void delete(int id);

    @Query("UPDATE nota_table SET title = :newTitle, body = :newBody, category = :newCategory, date = :newDate WHERE notaid = :id")
    void upload(int id, String newTitle, String newBody, String newCategory, String newDate);

    @Query("SELECT * FROM nota_table WHERE category = :category")
	LiveData<List<Nota>> selectByCat(String category);

	@Query("SELECT category FROM nota_table GROUP BY category ORDER BY category ASC")
	LiveData<List<String>> getAllCatASC();

	@Query("UPDATE nota_table SET category = '' WHERE category = :category")
	void deleteCategory(String category);
}
