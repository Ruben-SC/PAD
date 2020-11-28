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

public class RVAdapterRoom extends RecyclerView.Adapter<RVAdapterRoom.RoomViewHolder>
{
	private LayoutInflater inflater;
	private List<Nota> notas;
	private Context cont;

	RVAdapterRoom(Context context)
	{
		cont=context;
		inflater = LayoutInflater.from(context);
	}

	@NonNull
	@Override
	public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		View view = inflater.inflate(R.layout.custom_grid_layout, parent, false);
		return new RoomViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull RoomViewHolder holder, int position)
	{
		if (notas != null)
		{
			Nota nota = notas.get(position);
			holder.title.setText(nota.getTitle());
			holder.body.setText(nota.getBody());
			holder.notaID = nota.getNotaid();
            holder.category = nota.getCategory();
			if(!nota.getDate().equals("00/00/0000")) {
				holder.date.setVisibility(View.VISIBLE);
				holder.date.setText(nota.getDate());
			}
			else
				holder.date.setVisibility(View.INVISIBLE);
		}
		else
		{
			holder.title.setText("Título vacío");
			holder.body.setText("Cuerpo vacío");
			holder.notaID = -1;
		}
	}

	void setNotas(List<Nota> notas)
	{
		this.notas = notas;
		notifyDataSetChanged();
	}

	public void notifyChange(){
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount()
	{
		return notas != null ? notas.size() : 0;
	}

	class RoomViewHolder extends RecyclerView.ViewHolder
	{
		TextView title;
		TextView body;
		TextView date;
		int notaID;
		String category;


		RoomViewHolder(@NonNull View itemView)
		{
			super(itemView);

			title = itemView.findViewById(R.id.tvTitle);
			body = itemView.findViewById(R.id.tvBody);
			date = itemView.findViewById(R.id.tvDate);

			itemView.setOnClickListener(v -> {

				Intent theIntent = new Intent(cont, MuestraNota.class);
				theIntent.putExtra("title", notas.get(getAdapterPosition()).getTitle());
				theIntent.putExtra("body", notas.get(getAdapterPosition()).getBody());
				theIntent.putExtra("notaID", notas.get(getAdapterPosition()).getNotaid());
				theIntent.putExtra("category", notas.get(getAdapterPosition()).getCategory());
				theIntent.putExtra("date", notas.get(getAdapterPosition()).getDate());
				cont.startActivity(theIntent);
            });
		}
	}
}
