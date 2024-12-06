package com.mrz.gym;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mrz.gym.adapters.EjercicioAdapter;
import com.mrz.gym.data.GymDatabase;
import com.mrz.gym.data.entity.Ejercicio;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RutinasActivity extends AppCompatActivity implements EjercicioAdapter.OnEjercicioClickListener {
    private RecyclerView ejerciciosRecyclerView;
    private EjercicioAdapter ejercicioAdapter;
    private ExecutorService executorService;
    private GymDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinas);

        database = GymDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        ejerciciosRecyclerView = findViewById(R.id.ejerciciosRecyclerView);
        ejerciciosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ejercicioAdapter = new EjercicioAdapter(null, this);
        ejerciciosRecyclerView.setAdapter(ejercicioAdapter);

        loadAllEjercicios();
    }

    private void loadAllEjercicios() {
        executorService.execute(() -> {
            List<Ejercicio> ejercicios = database.ejercicioDao().getAllEjercicios();
            runOnUiThread(() -> {
                ejercicioAdapter.updateEjercicios(ejercicios);
            });
        });
    }

    @Override
    public void onEjercicioClick(Ejercicio ejercicio) {
        // Implementar si necesitas editar el ejercicio
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
} 