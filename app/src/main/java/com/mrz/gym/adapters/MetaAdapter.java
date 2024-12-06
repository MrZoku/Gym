package com.mrz.gym.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mrz.gym.R;
import com.mrz.gym.data.entity.Meta;
import java.util.List;

public class MetaAdapter extends RecyclerView.Adapter<MetaAdapter.MetaViewHolder> {
    private List<Meta> metas;
    private OnMetaClickListener listener;

    public interface OnMetaClickListener {
        void onMetaClick(Meta meta);
    }

    public MetaAdapter(List<Meta> metas, OnMetaClickListener listener) {
        this.metas = metas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meta, parent, false);
        return new MetaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MetaViewHolder holder, int position) {
        Meta meta = metas.get(position);
        holder.ejercicioNombre.setText(meta.getEjercicio());
        holder.pesoMeta.setText(String.format("%.1f kg → %.1f kg", meta.getPesoActual(), meta.getPesoObjetivo()));
        
        float progreso = ((meta.getPesoActual() - meta.getPesoInicial()) /
                (meta.getPesoObjetivo() - meta.getPesoInicial())) * 100;
        progreso = Math.max(0, Math.min(100, progreso));
        
        holder.progressBar.setProgress((int) progreso);
        holder.progressText.setText(String.format("%.1f%%", progreso));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMetaClick(meta);
            }
        });
    }

    @Override
    public int getItemCount() {
        return metas != null ? metas.size() : 0;
    }

    public void updateMetas(List<Meta> newMetas) {
        this.metas = newMetas;
        notifyDataSetChanged();
    }

    static class MetaViewHolder extends RecyclerView.ViewHolder {
        TextView ejercicioNombre;
        TextView pesoMeta;
        ProgressBar progressBar;
        TextView progressText;

        MetaViewHolder(View itemView) {
            super(itemView);
            ejercicioNombre = itemView.findViewById(R.id.ejercicioNombre);
            pesoMeta = itemView.findViewById(R.id.pesoMeta);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressText = itemView.findViewById(R.id.progressText);
        }
    }
} 