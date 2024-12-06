package com.mrz.gym.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "metas")
public class Meta {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String ejercicio;
    private float pesoInicial;
    private float pesoObjetivo;
    private float pesoActual;
    private String fecha;
    private String dia;

    public Meta(String ejercicio, float pesoInicial, float pesoObjetivo, float pesoActual, String fecha, String dia) {
        this.ejercicio = ejercicio;
        this.pesoInicial = pesoInicial;
        this.pesoObjetivo = pesoObjetivo;
        this.pesoActual = pesoActual;
        this.fecha = fecha;
        this.dia = dia;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEjercicio() { return ejercicio; }
    public float getPesoInicial() { return pesoInicial; }
    public float getPesoObjetivo() { return pesoObjetivo; }
    public float getPesoActual() { return pesoActual; }
    public void setPesoActual(float pesoActual) { this.pesoActual = pesoActual; }
    public String getFecha() { return fecha; }
    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }
} 