package com.mrz.gym.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "progresos",
        foreignKeys = {
            @ForeignKey(entity = Usuario.class,
                    parentColumns = "id",
                    childColumns = "usuarioId"),
            @ForeignKey(entity = Ejercicio.class,
                    parentColumns = "id",
                    childColumns = "ejercicioId")
        },
        indices = {
            @Index("usuarioId"),
            @Index("ejercicioId")
        }
)
public class Progreso {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int usuarioId;
    private int ejercicioId;
    private float peso;
    private int repeticiones;
    private int series;
    private Date fecha;

    public Progreso(int usuarioId, int ejercicioId, float peso, int repeticiones, int series, Date fecha) {
        this.usuarioId = usuarioId;
        this.ejercicioId = ejercicioId;
        this.peso = peso;
        this.repeticiones = repeticiones;
        this.series = series;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public int getEjercicioId() { return ejercicioId; }
    public void setEjercicioId(int ejercicioId) { this.ejercicioId = ejercicioId; }
    public float getPeso() { return peso; }
    public void setPeso(float peso) { this.peso = peso; }
    public int getRepeticiones() { return repeticiones; }
    public void setRepeticiones(int repeticiones) { this.repeticiones = repeticiones; }
    public int getSeries() { return series; }
    public void setSeries(int series) { this.series = series; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
} 