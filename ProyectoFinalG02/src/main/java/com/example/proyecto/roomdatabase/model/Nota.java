package com.example.proyecto.roomdatabase.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Cada Entity representa una tabla dentro de la base de datos.
@Entity(tableName = "nota_table")
public class Nota
{
	// Autogenerate sirve para cuando se quiere crear una id que se autoincremente
	@PrimaryKey(autoGenerate = true)
	private int notaid;

	// Las anotaciones son bastante descriptivas
	@NonNull
	@ColumnInfo(name = "title")
	private String title;

	@ColumnInfo(name = "body")
	private String body;

	@ColumnInfo(name = "category")
	private String category;

    @ColumnInfo(name = "date")
    private String date;

	public Nota (@NonNull String title, String body, String category, String date)
	{
		this.title = title;
		this.body = body;
		this.category = category;
        this.date = date;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body) {this.body = body;	}

	public String getCategory()	{ return category;	}

	public void setCategory(String category) {this.category = category;	}

	public String getDate()	{ return date;	}

	public void setDate(String date) {this.date = date;	}

	public int getNotaid() { return notaid;	}

	public void setNotaid(int notaid)
	{
		this.notaid = notaid;
	}
}
