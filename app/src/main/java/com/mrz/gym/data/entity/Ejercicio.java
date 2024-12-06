package com.mrz.gym.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ejercicios")
public class Ejercicio {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nombre;
    private int series;
    private int repeticiones;
    private String dia;

    public Ejercicio(String nombre, int series, int repeticiones, String dia) {
        this.nombre = nombre;
        this.series = series;
        this.repeticiones = repeticiones;
        this.dia = dia;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getSeries() { return series; }
    public void setSeries(int series) { this.series = series; }
    public int getRepeticiones() { return repeticiones; }
    public void setRepeticiones(int repeticiones) { this.repeticiones = repeticiones; }
    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    @Override
    public String toString() {
        return nombre;
    }
} 