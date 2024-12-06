package com.mrz.gym.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.mrz.gym.data.entity.Meta;
import java.util.List;

@Dao
public interface MetaDao {
    @Query("SELECT * FROM metas WHERE dia = :dia")
    List<Meta> getMetasByDia(String dia);

    @Query("SELECT * FROM metas")
    List<Meta> getAllMetas();

    @Insert
    void insert(Meta meta);

    @Update
    void update(Meta meta);

    @Delete
    void delete(Meta meta);
} 