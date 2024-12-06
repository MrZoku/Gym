package com.mrz.gym.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import com.mrz.gym.data.entity.Ejercicio;
import java.util.List;

@Dao
public interface EjercicioDao {
    @Query("SELECT * FROM ejercicios")
    List<Ejercicio> getAllEjercicios();

    @Query("SELECT * FROM ejercicios WHERE dia = :dia")
    List<Ejercicio> getEjerciciosPorDia(String dia);

    @Insert
    void insert(Ejercicio ejercicio);

    @Update
    void update(Ejercicio ejercicio);

    @Delete
    void delete(Ejercicio ejercicio);
} 