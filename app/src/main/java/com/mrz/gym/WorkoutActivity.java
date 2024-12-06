package com.mrz.gym;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mrz.gym.adapters.EjercicioAdapter;
import com.mrz.gym.adapters.MetaAdapter;
import com.mrz.gym.data.GymDatabase;
import com.mrz.gym.data.entity.Ejercicio;
import com.mrz.gym.data.entity.Meta;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkoutActivity extends AppCompatActivity implements EjercicioAdapter.OnEjercicioClickListener, MetaAdapter.OnMetaClickListener {
    private CalendarView calendarView;
    private TextView workoutTitle;
    private RecyclerView exerciseList;
    private RecyclerView metasRecyclerView;
    private EjercicioAdapter ejercicioAdapter;
    private ExecutorService executorService;
    private GymDatabase database;
    private MetaAdapter metaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        database = GymDatabase.getDatabase(this);
        executorService = Executors.newSingleThreadExecutor();

        calendarView = findViewById(R.id.calendarView);
        workoutTitle = findViewById(R.id.workoutTitle);
        exerciseList = findViewById(R.id.exerciseList);
        metasRecyclerView = findViewById(R.id.metasRecyclerView);

        // Configurar RecyclerView de ejercicios
        exerciseList.setLayoutManager(new LinearLayoutManager(this));
        ejercicioAdapter = new EjercicioAdapter(null, this);
        exerciseList.setAdapter(ejercicioAdapter);

        // Configurar RecyclerView de metas
        metasRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        metaAdapter = new MetaAdapter(null, this);
        metasRecyclerView.setAdapter(metaAdapter);

        // Cargar metas
        loadMetas();

        Button addMetaButton = findViewById(R.id.addMetaButton);
        addMetaButton.setOnClickListener(v -> showAddMetaDialog());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            
            updateWorkoutForDay(getDiaSemana(dayOfWeek));
        });

        // Cargar ejercicios iniciales
        updateWorkoutForDay(getDiaSemana(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));

        FloatingActionButton fabAgregarEjercicio = findViewById(R.id.fabAgregarEjercicio);
        fabAgregarEjercicio.setOnClickListener(v -> {
            startActivity(new Intent(this, AgregarEjercicioActivity.class));
        });

        Button verProgresoButton = findViewById(R.id.verProgresoButton);
        verProgresoButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ProgresoMetasActivity.class));
        });
    }

    private String getDiaSemana(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY: return "Lunes";
            case Calendar.TUESDAY: return "Martes";
            case Calendar.WEDNESDAY: return "Miércoles";
            case Calendar.THURSDAY: return "Jueves";
            case Calendar.FRIDAY: return "Viernes";
            default: return "";
        }
    }

    private void updateWorkoutForDay(String dia) {
        if (dia.isEmpty()) {
            workoutTitle.setText("Día de descanso");
            ejercicioAdapter.updateEjercicios(null);
            return;
        }

        workoutTitle.setText(dia + " - Rutina");
        
        executorService.execute(() -> {
            List<Ejercicio> ejercicios = database.ejercicioDao().getEjerciciosPorDia(dia);
            runOnUiThread(() -> {
                ejercicioAdapter.updateEjercicios(ejercicios);
            });
        });
    }

    @Override
    public void onEjercicioClick(Ejercicio ejercicio) {
        // Mostrar diálogo de edición cuando se hace clic en un ejercicio
        showEditDialog(ejercicio);
    }

    private void showEditDialog(Ejercicio ejercicio) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_ejercicio, null);
        
        TextInputEditText nombreInput = view.findViewById(R.id.nombreInput);
        TextInputEditText seriesInput = view.findViewById(R.id.seriesInput);
        TextInputEditText repeticionesInput = view.findViewById(R.id.repeticionesInput);
        Spinner diaSpinner = view.findViewById(R.id.diaSpinner);
        
        // Configurar el spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dias_semana, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diaSpinner.setAdapter(adapter);
        
        // Establecer valores actuales
        nombreInput.setText(ejercicio.getNombre());
        seriesInput.setText(String.valueOf(ejercicio.getSeries()));
        repeticionesInput.setText(String.valueOf(ejercicio.getRepeticiones()));
        int position = adapter.getPosition(ejercicio.getDia());
        diaSpinner.setSelection(position);

        builder.setView(view)
                .setTitle("Editar Ejercicio")
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = nombreInput.getText().toString().trim();
                    String seriesStr = seriesInput.getText().toString().trim();
                    String repeticionesStr = repeticionesInput.getText().toString().trim();
                    String dia = diaSpinner.getSelectedItem().toString();

                    if (!nombre.isEmpty() && !seriesStr.isEmpty() && !repeticionesStr.isEmpty()) {
                        int series = Integer.parseInt(seriesStr);
                        int repeticiones = Integer.parseInt(repeticionesStr);

                        ejercicio.setNombre(nombre);
                        ejercicio.setSeries(series);
                        ejercicio.setRepeticiones(repeticiones);
                        ejercicio.setDia(dia);
                        
                        executorService.execute(() -> {
                            database.ejercicioDao().update(ejercicio);
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Ejercicio actualizado", Toast.LENGTH_SHORT).show();
                                updateWorkoutForDay(getDiaSemana(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));
                            });
                        });
                    }
                })
                .setNegativeButton("Cancelar", null)
                .setNeutralButton("Eliminar", (dialog, which) -> {
                    executorService.execute(() -> {
                        database.ejercicioDao().delete(ejercicio);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Ejercicio eliminado", Toast.LENGTH_SHORT).show();
                            updateWorkoutForDay(getDiaSemana(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));
                        });
                    });
                });

        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private void showAddMetaDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_meta, null);

        Spinner ejercicioSpinner = view.findViewById(R.id.ejercicioSpinner);
        TextInputEditText pesoInicialInput = view.findViewById(R.id.pesoInicialInput);
        TextInputEditText pesoObjetivoInput = view.findViewById(R.id.pesoObjetivoInput);

        // Cargar ejercicios en el spinner
        executorService.execute(() -> {
            List<Ejercicio> ejercicios = database.ejercicioDao().getAllEjercicios();
            runOnUiThread(() -> {
                ArrayAdapter<Ejercicio> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    ejercicios);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ejercicioSpinner.setAdapter(adapter);
            });
        });

        builder.setView(view)
               .setTitle("Nueva Meta")
               .setPositiveButton("Guardar", (dialog, which) -> {
                   String pesoInicialStr = pesoInicialInput.getText().toString();
                   String pesoObjetivoStr = pesoObjetivoInput.getText().toString();
                   Ejercicio ejercicio = (Ejercicio) ejercicioSpinner.getSelectedItem();

                   if (!pesoInicialStr.isEmpty() && !pesoObjetivoStr.isEmpty() && ejercicio != null) {
                       float pesoInicial = Float.parseFloat(pesoInicialStr);
                       float pesoObjetivo = Float.parseFloat(pesoObjetivoStr);
                       
                       Meta meta = new Meta(ejercicio.getNombre(), pesoInicial, pesoObjetivo, pesoInicial,
                           new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()),
                           getDiaSemana(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));
                       
                       executorService.execute(() -> {
                           database.metaDao().insert(meta);
                           runOnUiThread(() -> {
                               Toast.makeText(this, "Meta agregada exitosamente", Toast.LENGTH_SHORT).show();
                               loadMetas();
                           });
                       });
                   } else {
                       Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                   }
               })
               .setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    private void loadMetas() {
        Calendar calendar = Calendar.getInstance();
        String[] diasSemana = getResources().getStringArray(R.array.dias_semana);
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        // Ajustar al formato de días que usamos (Lunes = 0, Domingo = 6)
        int diaIndex = (diaSemana + 5) % 7;
        String diaActual = diasSemana[diaIndex];

        executorService.execute(() -> {
            List<Meta> metas = database.metaDao().getMetasByDia(diaActual);
            runOnUiThread(() -> {
                metaAdapter.updateMetas(metas);
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMetas(); // Recargar metas cuando se vuelve a la actividad
    }

    @Override
    public void onMetaClick(Meta meta) {
        showUpdateMetaDialog(meta);
    }

    private void showUpdateMetaDialog(Meta meta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_update_meta, null);

        TextInputEditText pesoActualInput = view.findViewById(R.id.pesoActualInput);
        pesoActualInput.setText(String.valueOf(meta.getPesoActual()));

        TextView infoText = view.findViewById(R.id.infoText);
        infoText.setText(String.format("Meta para %s\nPeso inicial: %.1f kg\nPeso objetivo: %.1f kg",
                meta.getEjercicio(), meta.getPesoInicial(), meta.getPesoObjetivo()));

        builder.setView(view)
               .setTitle("Actualizar Progreso")
               .setPositiveButton("Guardar", (dialog, which) -> {
                   String pesoActualStr = pesoActualInput.getText().toString();
                   if (!pesoActualStr.isEmpty()) {
                       float pesoActual = Float.parseFloat(pesoActualStr);
                       meta.setPesoActual(pesoActual);
                       
                       executorService.execute(() -> {
                           database.metaDao().update(meta);
                           runOnUiThread(() -> {
                               Toast.makeText(this, "Progreso actualizado", Toast.LENGTH_SHORT).show();
                               loadMetas();
                           });
                       });
                   }
               })
               .setNegativeButton("Cancelar", null)
               .setNeutralButton("Eliminar Meta", (dialog, which) -> {
                   executorService.execute(() -> {
                       database.metaDao().delete(meta);
                       runOnUiThread(() -> {
                           Toast.makeText(this, "Meta eliminada", Toast.LENGTH_SHORT).show();
                           loadMetas();
                       });
                   });
               });

        builder.create().show();
    }
} 