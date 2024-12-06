package com.mrz.gym;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mrz.gym.adapters.MetaAdapter;
import com.mrz.gym.data.GymDatabase;
import com.mrz.gym.data.entity.Meta;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProgresoMetasActivity extends AppCompatActivity implements MetaAdapter.OnMetaClickListener {
    private RecyclerView metasRecyclerView;
    private MetaAdapter metaAdapter;
    private ExecutorService executorService;
    private GymDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progreso_metas);

        database = GymDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        metasRecyclerView = findViewById(R.id.metasRecyclerView);
        metasRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        metaAdapter = new MetaAdapter(null, this);
        metasRecyclerView.setAdapter(metaAdapter);

        // Cargar todas las metas
        loadAllMetas();
    }

    private void loadAllMetas() {
        executorService.execute(() -> {
            List<Meta> metas = database.metaDao().getAllMetas();
            runOnUiThread(() -> {
                metaAdapter.updateMetas(metas);
            });
        });
    }

    @Override
    public void onMetaClick(Meta meta) {
        // Implementar la actualización de meta si es necesario
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
} 