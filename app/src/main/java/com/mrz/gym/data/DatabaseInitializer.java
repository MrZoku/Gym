package com.mrz.gym.data;

import android.content.Context;
import com.mrz.gym.data.entity.Ejercicio;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseInitializer {
    public static void initializeDb(Context context) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        GymDatabase database = GymDatabase.getDatabase(context);

        executorService.execute(() -> {
            if (database.ejercicioDao().getAllEjercicios().isEmpty()) {
                // Lunes - Pecho y Tríceps
                database.ejercicioDao().insert(new Ejercicio(
                    "Press de Banca",
                    4, 12,
                    "Lunes"
                ));
                database.ejercicioDao().insert(new Ejercicio(
                    "Press Inclinado con Mancuernas",
                    3, 15,
                    "Lunes"
                ));
                database.ejercicioDao().insert(new Ejercicio(
                    "Extensiones de Tríceps",
                    4, 15,
                    "Lunes"
                ));

                // Martes - Espalda y Bíceps
                database.ejercicioDao().insert(new Ejercicio(
                    "Dominadas",
                    4, 10,
                    "Martes"
                ));
                database.ejercicioDao().insert(new Ejercicio(
                    "Remo con Barra",
                    3, 12,
                    "Martes"
                ));
                database.ejercicioDao().insert(new Ejercicio(
                    "Curl de Bíceps",
                    4, 12,
                    "Martes"
                ));

                // Miércoles - Piernas
                database.ejercicioDao().insert(new Ejercicio(
                    "Sentadillas",
                    5, 10,
                    "Miércoles"
                ));
                database.ejercicioDao().insert(new Ejercicio(
                    "Peso Muerto",
                    4, 8,
                    "Miércoles"
                ));
                database.ejercicioDao().insert(new Ejercicio(
                    "Extensiones de Cuádriceps",
                    3, 15,
                    "Miércoles"
                ));

                // Jueves - Hombros
                database.ejercicioDao().insert(new Ejercicio(
                    "Press Militar",
                    4, 12,
                    "Jueves"
                ));
                database.ejercicioDao().insert(new Ejercicio(
                    "Elevaciones Laterales",
                    3, 15,
                    "Jueves"
                ));
                database.ejercicioDao().insert(new Ejercicio(
                    "Face Pull",
                    3, 15,
                    "Jueves"
                ));

                // Viernes - Full Body
                database.ejercicioDao().insert(new Ejercicio(
                    "Burpees",
                    5, 20,
                    "Viernes"
                ));
                database.ejercicioDao().insert(new Ejercicio(
                    "Fondos en Paralelas",
                    3, 12,
                    "Viernes"
                ));
                database.ejercicioDao().insert(new Ejercicio(
                    "Plancha",
                    3, 60,
                    "Viernes"
                ));
            }
        });

        executorService.shutdown();
    }
} 