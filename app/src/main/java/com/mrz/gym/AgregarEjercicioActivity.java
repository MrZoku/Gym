package com.mrz.gym;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.mrz.gym.data.GymDatabase;
import com.mrz.gym.data.entity.Ejercicio;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AgregarEjercicioActivity extends AppCompatActivity {
    private TextInputEditText nombreInput;
    private TextInputEditText seriesInput;
    private TextInputEditText repeticionesInput;
    private Spinner diaSpinner;
    private Button guardarButton;
    private ExecutorService executorService;
    private GymDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ejercicio);

        database = GymDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        nombreInput = findViewById(R.id.nombreInput);
        seriesInput = findViewById(R.id.seriesInput);
        repeticionesInput = findViewById(R.id.repeticionesInput);
        diaSpinner = findViewById(R.id.diaSpinner);
        guardarButton = findViewById(R.id.guardarButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dias_semana, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diaSpinner.setAdapter(adapter);

        guardarButton.setOnClickListener(v -> {
            String nombre = nombreInput.getText().toString().trim();
            String seriesStr = seriesInput.getText().toString().trim();
            String repeticionesStr = repeticionesInput.getText().toString().trim();
            String dia = diaSpinner.getSelectedItem().toString();

            if (nombre.isEmpty() || seriesStr.isEmpty() || repeticionesStr.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int series = Integer.parseInt(seriesStr);
            int repeticiones = Integer.parseInt(repeticionesStr);

            executorService.execute(() -> {
                Ejercicio ejercicio = new Ejercicio(nombre, series, repeticiones, dia);
                database.ejercicioDao().insert(ejercicio);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Ejercicio guardado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
} 