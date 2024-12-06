package com.mrz.gym.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.mrz.gym.R;
import com.mrz.gym.data.entity.Ejercicio;
import java.util.List;

public class EjercicioAdapter extends RecyclerView.Adapter<EjercicioAdapter.ViewHolder> {
    private List<Ejercicio> ejercicios;
    private OnEjercicioClickListener listener;

    public interface OnEjercicioClickListener {
        void onEjercicioClick(Ejercicio ejercicio);
    }

    public EjercicioAdapter(List<Ejercicio> ejercicios, OnEjercicioClickListener listener) {
        this.ejercicios = ejercicios;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ejercicio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ejercicio ejercicio = ejercicios.get(position);
        holder.nombreTextView.setText(ejercicio.getNombre());
        holder.seriesTextView.setText(String.valueOf(ejercicio.getSeries()));
        holder.repeticionesTextView.setText(String.valueOf(ejercicio.getRepeticiones()));
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEjercicioClick(ejercicio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ejercicios != null ? ejercicios.size() : 0;
    }

    public void updateEjercicios(List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView seriesTextView;
        TextView repeticionesTextView;

        ViewHolder(View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            seriesTextView = itemView.findViewById(R.id.seriesTextView);
            repeticionesTextView = itemView.findViewById(R.id.repeticionesTextView);
        }
    }
} 