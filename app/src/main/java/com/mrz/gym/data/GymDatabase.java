package com.mrz.gym.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.mrz.gym.data.dao.UsuarioDao;
import com.mrz.gym.data.entity.Usuario;
import com.mrz.gym.data.entity.Ejercicio;
import com.mrz.gym.data.entity.Progreso;
import com.mrz.gym.data.dao.EjercicioDao;
import com.mrz.gym.data.dao.MetaDao;
import com.mrz.gym.data.entity.Meta;

@Database(entities = {Usuario.class, Ejercicio.class, Progreso.class, Meta.class}, version = 6, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class GymDatabase extends RoomDatabase {
    private static volatile GymDatabase INSTANCE;

    public abstract UsuarioDao usuarioDao();
    public abstract EjercicioDao ejercicioDao();
    public abstract MetaDao metaDao();

    public static GymDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GymDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GymDatabase.class, "gym_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
} 