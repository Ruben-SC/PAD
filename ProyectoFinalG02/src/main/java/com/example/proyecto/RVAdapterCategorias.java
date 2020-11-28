package com.example.proyecto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto.roomdatabase.model.Nota;

import java.util.List;

public class RVAdapterCategorias extends RecyclerView.Adapter<RVAdapterCategorias.RoomViewHolder>
{
	private LayoutInflater inflater;
	private List<String> categorias;
	private Context cont;

	RVAdapterCategorias(Context context)
	{
		cont=context;
		inflater = LayoutInflater.from(context);
	}

	@NonNull
	@Override
	public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		View view = inflater.inflate(R.layout.custom_grid_layout_categorias, parent, false);
		return new RoomViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RoomViewHolder holder, int position)
	{
		if (categorias != null)
		{
			String categoria = categorias.get(position);
			holder.category.setText(categoria);
		}
		else
		{
			holder.category.setText("Categoría vacía");
		}
	}

	void setCategorias(List<String> categorias)
	{
		this.categorias = categorias;
		this.categorias.remove("");
		notifyDataSetChanged();
	}

	public List<String> getCategorias(){
	    return this.categorias;
}

	public void notifyChange(){
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount()
	{
		return categorias != null ? categorias.size() : 0;
	}

	class RoomViewHolder extends RecyclerView.ViewHolder
	{
		TextView category;

		RoomViewHolder(@NonNull View itemView)
		{
			super(itemView);

            category = itemView.findViewById(R.id.tvCategory);

			itemView.findViewById(R.id.tvCategory).setOnClickListener(v -> {
				Intent theIntent = new Intent(cont, MainActivity.class);
				theIntent.putExtra("category", category.getText().toString());
				cont.startActivity(theIntent);
			});
			itemView.findViewById(R.id.tvDelete).setOnClickListener(v -> {
				Intent theIntent = new Intent(cont, Categorias.class);
				theIntent.putExtra("eliminar", category.getText().toString());
				cont.startActivity(theIntent);
			});
		}
	}
}
